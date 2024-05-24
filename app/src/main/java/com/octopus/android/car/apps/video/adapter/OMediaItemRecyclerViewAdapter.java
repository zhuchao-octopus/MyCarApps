package com.octopus.android.car.apps.video.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuchao.android.video.OMedia;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OMediaItemRecyclerViewAdapter extends RecyclerView.Adapter<OMediaItemRecyclerViewAdapter.ViewHolder> {

    private List<OMedia> mValues;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OMediaItemRecyclerViewAdapter(List<OMedia> items) {
        mValues = items;
    }

    public void setData(List<OMedia> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(com.octopus.android.car.apps.databinding.FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        ///holder.mIdView.setText(mValues.get(position).id);
        holder.mTitleView.setText(mValues.get(position).getName());
        holder.mSubTitleView.setText(mValues.get(position).getPathName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                int position = holder.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Perform action with the clicked item
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, mValues.get(position));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mSubTitleView;
        public OMedia mItem;

        public ViewHolder(com.octopus.android.car.apps.databinding.FragmentItemBinding binding) {
            super(binding.getRoot());
            ///mIdView = binding.itemNumber;
            mImageView = binding.imageView;
            mTitleView = binding.textViewTitle;
            mSubTitleView = binding.textViewSubtitle;
        }

        public OMedia getItem() {
            return mItem;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mSubTitleView.getText() + "'";
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, OMedia oMedia);
    }

}