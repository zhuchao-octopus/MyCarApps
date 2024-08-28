package com.octopus.android.car.apps.audio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.databinding.FragmentPlayingItemBinding;
import com.zhuchao.android.video.OMedia;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class PlayingListAdapter extends RecyclerView.Adapter<PlayingListAdapter.ViewHolder> {

    private List<OMedia> mItemList;
    private OnItemClickListener onItemClickListener;

    public PlayingListAdapter(List<OMedia> items) {
        mItemList = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setData(List<OMedia> items) {
        mItemList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentPlayingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItemList.get(position);
        ///holder.mIdView.setText(mValues.get(position).id);
        holder.mTextViewTitle.setText(mItemList.get(position).getName());
        holder.mTextViewSubTitle.setText(mItemList.get(position).getPathName());
        holder.mTextViewTitle.setSelected(true);
        holder.mImageViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position, mItemList.get(position));
                }
            }
        });
        holder.mImageViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position, mItemList.get(position));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position, mItemList.get(position));
                }
            }
        });
        holder.mItemContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position, mItemList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, OMedia oMedia);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageViewTitle;
        public final TextView mTextViewTitle;
        public final TextView mTextViewSubTitle;
        public final ImageView mImageViewStatus;
        public final View mItemContentView;
        public OMedia mItem;

        public ViewHolder(FragmentPlayingItemBinding binding) {
            super(binding.getRoot());
            ///mIdView = binding.itemNumber;
            mImageViewTitle = binding.ivTitle;
            mTextViewTitle = binding.tvTitle;
            mTextViewSubTitle = binding.tvSubtitle;
            mImageViewStatus = binding.ivStatus;
            mItemContentView = binding.itemContentView;
        }

        public OMedia getItem() {
            return mItem;
        }

    }

}