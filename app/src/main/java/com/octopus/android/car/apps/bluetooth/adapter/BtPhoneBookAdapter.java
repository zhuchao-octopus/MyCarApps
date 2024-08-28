package com.octopus.android.car.apps.bluetooth.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopus.android.car.apps.bluetooth.bean.PhoneBookBean;
import com.octopus.android.car.apps.databinding.ItemPhoneListBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 电话本列表
 */
public class BtPhoneBookAdapter extends RecyclerView.Adapter<BtPhoneBookAdapter.ViewHolder> {

    private List<PhoneBookBean> mItemList;
    private OnItemClickListener onItemClickListener;

    public BtPhoneBookAdapter(List<PhoneBookBean> items) {
        this.mItemList = items;
    }

    public void setData(List<PhoneBookBean> items) {
        mItemList = items;
        notifyDataSetChanged();
    }

    public void removeData(int index) {
        mItemList.remove(index);
        notifyDataSetChanged();
    }

    public void setDataItem(PhoneBookBean items) {
        mItemList.add(items);
        notifyItemChanged(mItemList.size());
    }

    public List<PhoneBookBean> getBookDate() {
        return mItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPhoneListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItemList.get(position);
        PhoneBookBean phoneBookBean = mItemList.get(position);
        holder.name.setText(phoneBookBean.getName());
        holder.number.setText(phoneBookBean.getNumber());
    }

    @SuppressLint("SimpleDateFormat")
    private String timeToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, PhoneBookBean folderBean);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PhoneBookBean mItem;
        private TextView name;
        private TextView number;

        public ViewHolder(ItemPhoneListBinding binding) {
            super(binding.getRoot());
            name = binding.textView1;
            number = binding.numberTv;
        }

        public PhoneBookBean getItem() {
            return mItem;
        }

    }

}