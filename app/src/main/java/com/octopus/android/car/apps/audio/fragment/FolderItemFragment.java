package com.octopus.android.car.apps.audio.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.audio.adapter.OMediaItemRecyclerViewAdapter;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseFragment;
import com.zhuchao.android.video.VideoList;

/**
 * A fragment representing a list of Items.
 */
public class FolderItemFragment extends BaseFragment {

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
        mOMediaItemRecyclerViewAdapter = new OMediaItemRecyclerViewAdapter(mVideoList.toOMediaList());
        mRecyclerView.setAdapter(mOMediaItemRecyclerViewAdapter);
        return view;
    }
}