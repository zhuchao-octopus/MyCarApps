package com.octopus.android.car.apps.video.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.video.activity.VideoPlayingActivity;
import com.octopus.android.car.apps.video.adapter.FolderItemRecyclerViewAdapter;
import com.zhuchao.android.fbase.DataID;
import com.zhuchao.android.fbase.FileUtils;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.MessageEvent;
import com.zhuchao.android.fbase.MethodThreadMode;
import com.zhuchao.android.fbase.ObjectList;
import com.zhuchao.android.fbase.TCourierSubscribe;
import com.zhuchao.android.fbase.TTask;
import com.zhuchao.android.fbase.ThreadUtils;
import com.zhuchao.android.fbase.bean.FolderBean;
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseFragment;
import com.zhuchao.android.video.OMedia;
import com.zhuchao.android.video.VideoList;

/**
 * A fragment representing a list of Items.
 */
public class FolderItemFragment extends BaseFragment {
    private final String TAG = "FolderItemFragment";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FolderItemRecyclerViewAdapter mFolderItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private ObjectList mFolderList = new ObjectList();
    private VideoList mVideoList;
    private final TTask tTask = new TTask("Scanning.folder.video");

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FolderItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FolderItemFragment newInstance(int columnCount) {
        FolderItemFragment fragment = new FolderItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        tTask.invoke(tag -> mFolderList = FileUtils.getAllSubDirList2(getContext(), "/", DataID.MEDIA_TYPE_ID_AllDIR, DataID.MEDIA_TYPE_ID_VIDEO));
        tTask.callbackHandler(this::onEventTaskFinished);
        tTask.startAgain();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mFolderItemRecyclerViewAdapter = new FolderItemRecyclerViewAdapter(mFolderList.toList());
        mRecyclerView.setAdapter(mFolderItemRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateData(null);
        mFolderItemRecyclerViewAdapter.setOnItemClickListener(new FolderItemRecyclerViewAdapter.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(int position, FolderBean folderBean) {
                if (folderBean.isFileBean()) {
                    if (mVideoList != null) {
                        mVideoList.loadFromStringList(folderBean.getParent().getFileList());
                        Cabinet.getPlayManager().addOnePlayList(mVideoList);
                        OMedia oMedia = mVideoList.findByPath(folderBean.getPathName());/// Cabinet.getPlayManager().getOMediaFromPlayLists(folderBean.getPathName());
                        Cabinet.getPlayManager().setMediaToPlay(oMedia);
                        openLocalActivity(VideoPlayingActivity.class);
                    }
                } else {
                    mVideoList = new VideoList(folderBean.getPathName());
                    updateData(folderBean);
                }
            }
        });
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
        updateData(null);
    }

    private void checkIfEmpty() {
        if (mFolderItemRecyclerViewAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(FolderBean folderBean) {
        if (mRecyclerView == null) return;
        if (folderBean == null || folderBean.getName().equals("..")) {//根目录
            mFolderItemRecyclerViewAdapter.setData(mFolderList.toList());
            mRecyclerView.setAdapter(mFolderItemRecyclerViewAdapter);
            mFolderItemRecyclerViewAdapter.notifyDataSetChanged();
            checkIfEmpty();
        } else if (folderBean.getSubItemCount() > 0) {
            mFolderItemRecyclerViewAdapter.setData(folderBean.fileListToFolderBean());
            mFolderItemRecyclerViewAdapter.notifyDataSetChanged();
        }
        ///mFolderList.printAll();
    }

    private void onEventTaskFinished(Object obj, int status) {
        ThreadUtils.runOnMainUiThread(() -> updateData(null));
    }

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    public boolean onTCourierSubscribeEvent(EventCourierInterface eventCourierInterface) {
        switch (eventCourierInterface.getId()) { ///加载外部数据
            case MessageEvent.MESSAGE_EVENT_USB_UNMOUNT:
            case MessageEvent.MESSAGE_EVENT_USB_MOUNTED:
                MMLog.d(TAG, eventCourierInterface.toStr());
                tTask.startAgain();
                break;
            case MessageEvent.MESSAGE_EVENT_LOCAL_VIDEO:
            case MessageEvent.MESSAGE_EVENT_USB_VIDEO:
            case MessageEvent.MESSAGE_EVENT_SD_VIDEO:
                break;
        }
        return true;
    }
}