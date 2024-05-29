package com.octopus.android.car.apps.audio.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a generic item.
 *
 * @param <T> the type of items managed by this adapter.
 */
public class CommonItemRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonItemRecyclerViewAdapter.ViewHolder<T>> {
    public static final String DATA_TYPE_ALBUM = "album";
    public static final String DATA_TYPE_ARTIST = "artist";
    private List<T> mItemList;
    private OnItemClickListener<T> onItemClickListener;
    private OnBindViewHolderListener<T> onBindViewHolderListener;
    ///private String mDataType;
    ///private FragmentItemBinding mFragmentItemBinding;

    public void setOnBindViewHolderListener(OnBindViewHolderListener<T> onBindViewHolderListener) {
        this.onBindViewHolderListener = onBindViewHolderListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CommonItemRecyclerViewAdapter() {
    }

    public CommonItemRecyclerViewAdapter(List<T> items) {
        mItemList = items;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<T> items) {
        mItemList = items;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataAndNotify(List<T> items) {
        mItemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(com.octopus.android.car.apps.databinding.FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder<T> holder, int position) {
        holder.mItem = mItemList.get(position);
        if (onBindViewHolderListener != null) onBindViewHolderListener.onBindViewHolder(holder, holder.mItem, position);
        holder.mImageViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, v, position, mItemList.get(position));
                }
            }
        });
        holder.mImageViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, v, position, mItemList.get(position));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder, v, position, mItemList.get(position));
                    }
                }
            }
        });
        holder.mItemContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, v, position, mItemList.get(position));
                }
            }
        });
       /*
        // Set your data to the views here, e.g., holder.mTitleView.setText(...);
        if (holder.mItem instanceof MediaMetadata && DATA_TYPE_ALBUM.equals(mDataType)) {
            ///holder.mImageViewTitle.setImageBitmap(AudioMetaFile.);
            holder.mTextViewTitle.setText(((MediaMetadata) holder.mItem).getAlbum() + " (" + ((MediaMetadata) holder.mItem).getCount() + ")");
            holder.mTextViewSubTitle.setText(((MediaMetadata) holder.mItem).getDescription());
        } else if (holder.mItem instanceof MediaMetadata && DATA_TYPE_ARTIST.equals(mDataType)) {
            ///holder.mImageViewTitle.setImageBitmap(AudioMetaFile.);
            holder.mTextViewTitle.setText(((MediaMetadata) holder.mItem).getArtist() + " (" + ((MediaMetadata) holder.mItem).getCount() + ")");
            holder.mTextViewSubTitle.setText(((MediaMetadata) holder.mItem).getDescription());
        }
        */
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public final ImageView mImageViewTitle;
        public final TextView mTextViewTitle;
        public final TextView mTextViewSubTitle;
        public final ImageView mImageViewStatus;
        public final View mItemContentView;
        public T mItem;

        public ViewHolder(com.octopus.android.car.apps.databinding.FragmentItemBinding binding) {
            super(binding.getRoot());
            mImageViewTitle = binding.ivTitle;
            mTextViewTitle = binding.tvTitle;
            mTextViewSubTitle = binding.tvSubtitle;
            mImageViewStatus = binding.ivStatus;
            mItemContentView = binding.itemContentView;
        }

        public T getItem() {
            return mItem;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mTextViewSubTitle.getText() + "'";
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(final ViewHolder<T> holder, View v, int position, T obj);
    }

    public interface OnBindViewHolderListener<T> {
        void onBindViewHolder(ViewHolder<T> holder, T item, int position);
    }
}
