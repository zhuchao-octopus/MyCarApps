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
import com.octopus.android.car.apps.video.adapter.OMediaItemRecyclerViewAdapter;
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
public class PlayingItemFragment extends BaseFragment {
    private final String TAG = "PlayItemFragment";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OMediaItemRecyclerViewAdapter mOMediaItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private VideoList mVideoList = Cabinet.getPlayManager().getPlayingHistoryList();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayingItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlayingItemFragment newInstance(int columnCount) {
        PlayingItemFragment fragment = new PlayingItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        /// Set the adapter
        ///if (view instanceof RecyclerView)
        ///{
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mOMediaItemRecyclerViewAdapter = new OMediaItemRecyclerViewAdapter(mVideoList.toOMediaList());
        mRecyclerView.setAdapter(mOMediaItemRecyclerViewAdapter);
        ///}
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
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //MMLog.d(TAG, "onResume()");
        onFragmentVisible(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //MMLog.d(TAG, "onHiddenChanged()="+hidden);
        onFragmentVisible(hidden);
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

    private void onFragmentVisible(boolean hidden) {
        //MMLog.d(TAG, "onFragmentVisible()=" + isHidden() + " " + isVisible());
        // 在这里处理Fragment显示的逻辑
        updateData(0);
    }

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    public boolean onTCourierSubscribeEvent(EventCourierInterface eventCourierInterface) {
        switch (eventCourierInterface.getId()) { ///加载外部数据
            case MessageEvent.MESSAGE_EVENT_LOCAL_VIDEO:
            case MessageEvent.MESSAGE_EVENT_USB_VIDEO:
            case MessageEvent.MESSAGE_EVENT_SD_VIDEO:
            case MessageEvent.MESSAGE_EVENT_LOCAL_AUDIO:
            case MessageEvent.MESSAGE_EVENT_USB_AUDIO:
            case MessageEvent.MESSAGE_EVENT_SD_AUDIO:
            case MessageEvent.MESSAGE_EVENT_MEDIA_LIBRARY:
            case MessageEvent.MESSAGE_EVENT_OCTOPUS_AIDL_START_REGISTER:
                updateData(eventCourierInterface.getId());
                break;
        }
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(int dataId) {
        mVideoList = Cabinet.getPlayManager().getPlayingHistoryList();
        mOMediaItemRecyclerViewAdapter.setData(mVideoList.toOMediaList());
        mOMediaItemRecyclerViewAdapter.notifyDataSetChanged();
        checkIfEmpty();
    }
}