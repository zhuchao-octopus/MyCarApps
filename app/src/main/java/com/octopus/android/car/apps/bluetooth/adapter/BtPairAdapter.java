package com.octopus.android.car.apps.bluetooth.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.car.api.ApiBt;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.bluetooth.bean.BTDevice;
import com.octopus.android.car.apps.databinding.ItemPairListBinding;

import java.util.List;

/**
 * 蓝牙列表
 */
public class BtPairAdapter extends RecyclerView.Adapter<BtPairAdapter.ViewHolder> {

    private List<BTDevice> mItemList;
    private OnItemClickListener onItemClickListener;

    public BtPairAdapter(List<BTDevice> items, OnItemClickListener listener) {
        this.mItemList = items;
        this.onItemClickListener = listener;
    }

    public void setConnectMac(String mac) {
        int index = 0;
        for (int i = 0; i < mItemList.size(); i++) {
            if (mItemList.get(i).getDeviceMacAddress().equals(mac)) {
                mItemList.get(i).setPairState(true);
                mItemList.get(i).setConnectMac(true);
                index = i;
                Log.d("BluetoothPairFragment", "setConnectMac: " + i);
            } else {
                mItemList.get(i).setConnectMac(false);
            }
        }
        notifyDataSetChanged();

        notifyItemMoved(index, 0);
    }

    public List<BTDevice> getData() {
        return mItemList;
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
        return new ViewHolder(ItemPairListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItemList.get(position);
        ///holder.mIdView.setText(mValues.get(position).id);
        holder.textView.setText(mItemList.get(position).getDeviceName());
        BTDevice btDevice = mItemList.get(position);
        if (btDevice.isPairState()) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        int index = position;
        if (btDevice.isShowDelete()) {
            holder.delete_iv.setVisibility(View.VISIBLE);
        } else {
            holder.delete_iv.setVisibility(View.INVISIBLE);
        }
        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除蓝牙设备
                mItemList.remove(btDevice);
                onItemClickListener.onDeleteItem(btDevice);
                notifyItemRemoved(index);
            }
        });
        if (btDevice.isConnectMac()) {
            holder.imageView.setBackgroundResource(R.mipmap.bt_pair_connect);
        } else {
            holder.imageView.setBackgroundResource(R.mipmap.bt_pair_disconnect);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选中请求连接蓝牙
                ApiBt.connectDevice(btDevice.getDeviceMacAddress());
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
        public BTDevice mItem;
        private TextView textView;
        private ImageView imageView;
        private ImageView delete_iv;

        public ViewHolder(ItemPairListBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
            imageView = binding.imageView;
            delete_iv = binding.deleteIv;
        }

        public BTDevice getItem() {
            return mItem;
        }

    }

}