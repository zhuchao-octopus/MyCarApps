package com.octopus.android.car.apps.audio.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.audio.adapter.OMediaItemRecyclerViewAdapter;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseFragment;
import com.zhuchao.android.video.VideoList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsItemFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mColumnCount = 1;
    private OMediaItemRecyclerViewAdapter mOMediaItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private VideoList mVideoList = Cabinet.getPlayManager().getPlayingHistoryList();
    public ArtistsItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsItemFragment newInstance(String param1, String param2) {
        ArtistsItemFragment fragment = new ArtistsItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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