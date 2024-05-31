package com.octopus.android.car.apps.audio.fragment;

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
 * A fragment representing a list of Items.
 */
public class AlbumsItemFragment extends BaseFragment implements CommonItemRecyclerViewAdapter.OnItemClickListener<Object>, CommonItemRecyclerViewAdapter.OnBindViewHolderListener<Object> {
    private static final String TAG = "AlbumsItemFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ALBUM_TAG = "media.album.tag.";
    public static final String ARTIST_TAG = "media.artist.tag.";
    private int mColumnCount = 1;
    private CommonItemRecyclerViewAdapter<Object> mCommonItemRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private final TMediaMetadataManager tTMediaMetadataManager = Cabinet.getPlayManager().getMediaMetadataManager();
    private VideoList mVideoList;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumsItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AlbumsItemFragment newInstance(int columnCount) {
        AlbumsItemFragment fragment = new AlbumsItemFragment();
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
        checkIfEmpty();
    }

    @Override
    public void onItemClick(CommonItemRecyclerViewAdapter.ViewHolder<Object> holder, View v, int position, Object obj) {
        if (obj instanceof MediaMetadata && holder.mItemContentView.equals(v)) {
            MMLog.d(TAG, ((MediaMetadata) obj).toString());
            updateData(obj);
        } else if (obj instanceof OMedia && holder.mItemContentView.equals(v)) {
            MMLog.d(TAG, ((OMedia) obj).getMovie().toString());
            ///Cabinet.getPlayManager().createPlayingListOrder(mVideoList);
            Cabinet.getPlayManager().setMediaToPlay(((OMedia) obj));
            openLocalActivity(MusicPlayingActivity.class);
        } else if (holder.mImageViewTitle.equals(v)) {
            ///MMLog.d(TAG, ((MediaMetadata) obj).toString());
            updateData(obj);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CommonItemRecyclerViewAdapter.ViewHolder<Object> holder, Object item, int position) {
        if (item instanceof MediaMetadata) {
            ///holder.mImageViewTitle.setImageBitmap(AudioMetaFile.);
            holder.mTextViewTitle.setText(((MediaMetadata) item).getAlbum() + " (" + ((MediaMetadata) item).getCount() + ")");
            holder.mTextViewSubTitle.setText(((MediaMetadata) holder.mItem).getDescription());
            if (((MediaMetadata) item).getBitmap() != null)
                holder.mImageViewTitle.setImageBitmap(((MediaMetadata) item).getBitmap());
            else
                holder.mImageViewTitle.setImageResource(R.mipmap.ic_albums);
        }
        else if (item instanceof OMedia) {
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
            // = Cabinet.getPlayManager().getAllMusic();
            VideoList videoList = Cabinet.getPlayManager().getAllMusic();
            mVideoList = videoList.getMusicByAlbum(((MediaMetadata) obj).getAlbum());
            mCommonItemRecyclerViewAdapter.setDataAndNotify(mVideoList.toList());
        } else {
            mCommonItemRecyclerViewAdapter.setData(tTMediaMetadataManager.getTAlbums());
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