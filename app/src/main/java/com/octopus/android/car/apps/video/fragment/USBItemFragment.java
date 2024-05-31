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
import com.octopus.android.car.apps.common.adapter.OMediaItemRecyclerViewAdapter;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.MessageEvent;
import com.zhuchao.android.fbase.MethodThreadMode;
import com.zhuchao.android.fbase.TCourierSubscribe;
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseFragment;
import com.zhuchao.android.video.OMedia;
import com.zhuchao.android.video.VideoList;

/**
 * A fragment representing a list of Items.
 */
public class USBItemFragment extends BaseFragment {
    private final String TAG = "USBItemFragment";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OMediaItemRecyclerViewAdapter mOMediaItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private VideoList mVideoList = Cabinet.getPlayManager().getLocalUSBMediaVideos();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public USBItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static USBItemFragment newInstance(int columnCount) {
        USBItemFragment fragment = new USBItemFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        Context context = view.getContext();

        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mOMediaItemRecyclerViewAdapter = new OMediaItemRecyclerViewAdapter(mVideoList.toOMediaList());
        mRecyclerView.setAdapter(mOMediaItemRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOMediaItemRecyclerViewAdapter.setOnItemClickListener(new OMediaItemRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, OMedia oMedia) {
                if (oMedia != null) {
                    Cabinet.getPlayManager().setMediaToPlay(oMedia);
                    openLocalActivity(VideoPlayingActivity.class);
                    ///Cabinet.getPlayManager().printAllPlayLists();
                    ///Cabinet.getPlayManager().getLocalUSBMediaVideos().printAll();
                }
            }
        });
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
        updateData(0);
    }

    private void checkIfEmpty() {
        if (mOMediaItemRecyclerViewAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(int dataId) {
        mVideoList = Cabinet.getPlayManager().getLocalUSBMediaVideos();
        mOMediaItemRecyclerViewAdapter.setData(mVideoList.toOMediaList());
        mOMediaItemRecyclerViewAdapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    public boolean onTCourierSubscribeEvent(EventCourierInterface eventCourierInterface) {
        switch (eventCourierInterface.getId()) { ///加载外部数据
            case MessageEvent.MESSAGE_EVENT_MEDIA_LIBRARY:
            case MessageEvent.MESSAGE_EVENT_OCTOPUS_AIDL_START_REGISTER:
                ///Cabinet.getPlayManager().updateMoviesToPlayList();
                break;
            case MessageEvent.MESSAGE_EVENT_LOCAL_VIDEO:
            case MessageEvent.MESSAGE_EVENT_USB_VIDEO:
            case MessageEvent.MESSAGE_EVENT_SD_VIDEO:
                MMLog.d(TAG, eventCourierInterface.toStr());
                updateData(eventCourierInterface.getId());

                break;
        }
        return true;
    }
}