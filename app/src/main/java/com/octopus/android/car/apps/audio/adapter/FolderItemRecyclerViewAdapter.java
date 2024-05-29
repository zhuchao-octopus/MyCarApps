package com.octopus.android.car.apps.audio.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.FragmentItemBinding;
import com.zhuchao.android.fbase.MediaFile;
import com.zhuchao.android.fbase.bean.FolderBean;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FolderItemRecyclerViewAdapter extends RecyclerView.Adapter<FolderItemRecyclerViewAdapter.ViewHolder> {

    private List<FolderBean> mValues;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public FolderItemRecyclerViewAdapter(List<FolderBean> items) {
        mValues = items;
    }

    public void setData(List<FolderBean> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        ///holder.mIdView.setText(mValues.get(position).id);
        if (holder.mItem.getSubItemCount() > 0) holder.mTextViewTitle.setText(holder.mItem.getName() + " (" + holder.mItem.getSubItemCount() + ")");
        else holder.mTextViewTitle.setText(holder.mItem.getName());
        holder.mTextViewSubTitle.setText(holder.mItem.getPathName());

        if (holder.mItem.isFileBean()) {
            if (MediaFile.isVideoFile(holder.mItem.getPathName())) {
                ///Context context = holder.itemView.getContext();
                Bitmap bitmap = holder.mItem.getVideoFileFrame();
                if (bitmap != null) {
                    holder.mImageViewTitle.setImageBitmap(bitmap);
                    holder.mImageViewTitle.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else holder.mImageViewTitle.setImageResource(R.mipmap.ic_video);
            } else if (MediaFile.isAudioFile(holder.mItem.getPathName())) holder.mImageViewTitle.setImageResource(R.mipmap.music_list);
            else holder.mImageViewTitle.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.mImageViewTitle.setImageResource(R.mipmap.folder);
        }

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
        public final ImageView mImageViewTitle;
        public final TextView mTextViewTitle;
        public final TextView mTextViewSubTitle;
        public final ImageView mImageViewStatus;
        public FolderBean mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mImageViewTitle = binding.ivTitle;
            mTextViewTitle = binding.tvTitle;
            mTextViewSubTitle = binding.tvSubtitle;
            mImageViewStatus = binding.ivStatus;
            mImageViewTitle.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        public FolderBean getItem() {
            return mItem;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, FolderBean folderBean);
    }

}