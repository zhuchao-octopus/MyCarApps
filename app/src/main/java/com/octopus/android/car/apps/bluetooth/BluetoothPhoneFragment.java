package com.octopus.android.car.apps.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.bluetooth.adapter.BtPhoneBookAdapter;
import com.octopus.android.car.apps.bluetooth.bean.PhoneBookBean;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothPhoneBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class BluetoothPhoneFragment extends BaseViewBindingFragment<FragmentBluetoothPhoneBinding> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private final String TAG = "BluetoothPhoneFragment";
    private BtPhoneBookAdapter btPhoneBookAdapter;
    private List<PhoneBookBean> listTemp = new ArrayList<>();

    public BluetoothPhoneFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothPhoneFragment newInstance(int columnCount) {
        BluetoothPhoneFragment fragment = new BluetoothPhoneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentBluetoothPhoneBinding onCreateViewBinding() {
        return FragmentBluetoothPhoneBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        listTemp.clear();
        btPhoneBookAdapter = new BtPhoneBookAdapter(new ArrayList<>());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycleView.setAdapter(btPhoneBookAdapter);
        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PhoneBookBean> bookBeanList = btPhoneBookAdapter.getBookDate();
                if (!bookBeanList.isEmpty()) {
                    Log.d(TAG, "onClick: " + bookBeanList.size());
                    ApiBt.deleteContact(bookBeanList.get(0).getName(), bookBeanList.get(0).getNumber());
                    btPhoneBookAdapter.removeData(0);
                }
            }
        });
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 执行搜索操作
                    Log.d(TAG, "onEditorAction: " + v.getText().toString().trim());

                    String textInput = v.getText().toString().trim();
                    List<PhoneBookBean> result = listTemp.stream().filter(item -> item.getName().contains(textInput) || item.getNumber().contains(textInput)).collect(Collectors.toList());
                    btPhoneBookAdapter.setData(result);
                    // 隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        binding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiBt.downloadBook();
            }
        });
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.UPDATE_BOOK,//电话本
                ApiBt.UPDATE_PBAP_STATE,//电话本下载状态
                ApiBt.UPDATE_SEARCH_LIST,//电话本下载状态
        }, iCallback, true);
    }

    @Override
    public void onUpdate(Bundle params) {
        if (binding == null) {
            return;
        }
        if (params == null) return;
        String id = params.getString("id");
        if (id == null) return;
        switch (id) {
            case ApiBt.UPDATE_BOOK:
                //电话本信息
                String name = params.getString("name");
                String number = params.getString("number");
                Log.d(TAG, "联系人 name = " + name + " number = " + number);
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    return;
                }
                PhoneBookBean phoneBookBean = new PhoneBookBean();
                phoneBookBean.setName(name);
                phoneBookBean.setNumber(number);
                btPhoneBookAdapter.setDataItem(phoneBookBean);
                listTemp.add(phoneBookBean);
                break;
            case ApiBt.UPDATE_PBAP_STATE:
                //电话本下载状态
                int state = params.getInt("value");
                //ApiBt.PBAP_STATE_LOAD 下载中；ApiBt.PBAP_STATE_CONNECTED 下载完成
                Log.d(TAG, "电话本下载状态 state = " + state);
                break;
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }
}