package com.octopus.android.car.apps.audio.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.audio.activity.MusicPlayingActivity;
import com.octopus.android.car.apps.common.adapter.CommonItemRecyclerViewAdapter;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.MessageEvent;
import com.zhuchao.android.fbase.MethodThreadMode;
import com.zhuchao.android.fbase.TCourierSubscribe;
import com.zhuchao.android.fbase.bean.MediaMetadata;
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.TMediaMetadataManager;
import com.zhuchao.android.session.base.BaseFragment;
import com.zhuchao.android.video.OMedia;
import com.zhuchao.android.video.VideoList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsItemFragment extends BaseFragment implements CommonItemRecyclerViewAdapter.OnItemClickListener<Object>, CommonItemRecyclerViewAdapter.OnBindViewHolderListener<Object> {
    public static final String ALBUM_TAG = "media.album.tag.";
    public static final String ARTIST_TAG = "media.artist.tag.";
    private static final String TAG = "ArtistsItemFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final TMediaMetadataManager tTMediaMetadataManager = Cabinet.getPlayManager().getMediaMetadataManager();
    private int mColumnCount = 1;
    private CommonItemRecyclerViewAdapter<Object> mCommonItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private VideoList mVideoList;

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
        mCommonItemRecyclerViewAdapter = new CommonItemRecyclerViewAdapter<>();
        mCommonItemRecyclerViewAdapter.setData(tTMediaMetadataManager.getTArtist());
        mRecyclerView.setAdapter(mCommonItemRecyclerViewAdapter);
        checkIfEmpty();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommonItemRecyclerViewAdapter = new CommonItemRecyclerViewAdapter<>();
        mCommonItemRecyclerViewAdapter.setOnItemClickListener(this);
        mCommonItemRecyclerViewAdapter.setOnBindViewHolderListener(this);
        mCommonItemRecyclerViewAdapter.setData(tTMediaMetadataManager.getTAlbums());
        mRecyclerView.setAdapter(mCommonItemRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(CommonItemRecyclerViewAdapter.ViewHolder<Object> holder, View v, int position, Object obj) {
        if (obj instanceof MediaMetadata && holder.mItemContentView.equals(v)) {
            MMLog.d(TAG, ((MediaMetadata) obj).toString());
            updateData(obj);
        } else if (obj instanceof OMedia && holder.mItemContentView.equals(v)) {
            MMLog.d(TAG, ((OMedia) obj).getMovie().toString());
            Cabinet.getPlayManager().createPlayingListOrder(mVideoList);
            Cabinet.getPlayManager().setMediaToPlay(((OMedia) obj));
            openLocalActivity(MusicPlayingActivity.class);
        } else if (holder.mImageViewTitle.equals(v)) {
            updateData(obj);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CommonItemRecyclerViewAdapter.ViewHolder<Object> holder, Object item, int position) {
        if (item instanceof MediaMetadata) {
            ///holder.mImageViewTitle.setImageBitmap(AudioMetaFile.);
            holder.mTextViewTitle.setText(((MediaMetadata) item).getArtist() + " (" + ((MediaMetadata) item).getCount() + ")");
            holder.mTextViewSubTitle.setText(((MediaMetadata) holder.mItem).getDescription());
            holder.mImageViewTitle.setImageResource(R.mipmap.ic_person);
        } else if (item instanceof OMedia) {
            holder.mTextViewTitle.setText(((OMedia) item).getName());
            holder.mTextViewSubTitle.setText(((OMedia) item).getPathName());
            holder.mImageViewTitle.setImageResource(R.mipmap.ic_music);
        }
    }

    private void checkIfEmpty() {
        if (mCommonItemRecyclerViewAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(Object obj) {
        if (obj instanceof MediaMetadata) {
            VideoList videoList = Cabinet.getPlayManager().getAllMusic();
            mVideoList = videoList.getMusicByArtist(((MediaMetadata) obj).getArtist());
            mCommonItemRecyclerViewAdapter.setDataAndNotify(mVideoList.toList());
        } else {
            mCommonItemRecyclerViewAdapter.setData(tTMediaMetadataManager.getTArtist());
            mCommonItemRecyclerViewAdapter.notifyDataSetChanged();
            checkIfEmpty();
        }
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

}