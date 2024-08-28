package com.octopus.android.car.apps.equalizer;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.bluetooth.bean.BTDevice;
import com.octopus.android.car.apps.databinding.FragmentEqAjustItemBinding;

import java.util.List;

/**
 * 音量调节
 */
public class EqAdjustAdapter extends RecyclerView.Adapter<EqAdjustAdapter.ViewHolder> {

    private List<BTDevice> mItemList;
    private OnItemClickListener onItemClickListener;

    public EqAdjustAdapter(List<BTDevice> items, OnItemClickListener listener) {
        this.mItemList = items;
        this.onItemClickListener = listener;
    }

    public void setData(List<BTDevice> items) {
        mItemList = items;
    }


    public void setDataItem(BTDevice items) {
        mItemList.add(items);
        notifyItemChanged(mItemList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEqAjustItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItemList.get(position);

        holder.seekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("onProgressChanged", "onProgressChanged: " + progress);
                this.progress = progress;
                holder.eq_tv.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BTDevice folderBean);

        void onDeleteItem(BTDevice folderBean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private BTDevice mItem;
        private VerticalSeekBar seekBarVol;
        private TextView eq_tv;

        public ViewHolder(FragmentEqAjustItemBinding binding) {
            super(binding.getRoot());
            this.seekBarVol = binding.seekBarVol;
            this.eq_tv = binding.eqTv;

        }

        public BTDevice getItem() {
            return mItem;
        }

    }

}