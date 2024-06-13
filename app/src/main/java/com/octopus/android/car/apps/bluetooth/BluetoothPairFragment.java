package com.octopus.android.car.apps.bluetooth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.bluetooth.adapter.BtPairAdapter;
import com.octopus.android.car.apps.bluetooth.bean.BTDevice;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothPairBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 蓝牙配对页面
 */
public class BluetoothPairFragment extends BaseViewBindingFragment<FragmentBluetoothPairBinding> implements BtPairAdapter.OnItemClickListener {
    private final String TAG = "BluetoothPairFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private BtPairAdapter btPairAdapter;
    private Map<String, String> hashMap = new HashMap<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BluetoothPairFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothPairFragment newInstance(int columnCount) {
        BluetoothPairFragment fragment = new BluetoothPairFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentBluetoothPairBinding onCreateViewBinding() {
        return FragmentBluetoothPairBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ApiBt.queryPair(); //搜索周围蓝牙设备
                ApiBt.discover(1); //搜索周围蓝牙设备
            }
        });
        hashMap.clear();
        btPairAdapter = new BtPairAdapter(new ArrayList<>(), this);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycleView.setAdapter(btPairAdapter);
        binding.ivDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiBt.link(0);
                ApiBt.btavLink(0);
            }
        });
        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BTDevice> list = btPairAdapter.getData();
                boolean delete;
                if (binding.ivDelete.isSelected()) {
                    delete = false;
                    binding.ivDelete.setSelected(false);
                } else {
                    binding.ivDelete.setSelected(true);
                    delete = true;
                }
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShowDelete(delete);
                }
                btPairAdapter.notifyDataSetChanged();
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
                ApiBt.UPDATE_PHONE_STATE,//蓝牙状态
                ApiBt.UPDATE_PAIR_LIST,//蓝牙列表
                ApiBt.UPDATE_SEARCH_LIST,//搜索列表
                ApiBt.UPDATE_PHONE_MAC_ADDR,//连接的mac
        }, iCallback, true);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdate(Bundle params) {
        if (params == null) return;
        String id = params.getString("id");
        Log.d(TAG, "onUpdate:  " + id);
        if (id != null) {
            switch (id) {
                case ApiBt.UPDATE_PHONE_STATE:
                    //蓝牙连接状态
                    int value = params.getInt("value");
                    String phoneMacAddress = params.getString("phoneMacAddr");
                    String phoneName = params.getString("phoneName");
                    Log.d(TAG, "onUpdate 蓝牙:  " + "   phoneMacAddress:" + phoneMacAddress + " phonename: " + phoneName + " : " + hashMap.containsKey(phoneMacAddress) + "  : " + value);
                    break;
                case ApiBt.UPDATE_PAIR_LIST:
                    addDevice(params, true);
                    break;
                case ApiBt.UPDATE_SEARCH_LIST:
                    addDevice(params, false);
                    break;
                case ApiBt.UPDATE_PHONE_MAC_ADDR:
                    String macAddr = params.getString("value");
                    Log.d(TAG, "onUpdate: 已连接:  " + macAddr);
                    btPairAdapter.setConnectMac(macAddr);
                    binding.ivConnect.setSelected(!TextUtils.isEmpty(macAddr));
                    break;
            }
        }
    }

    private void addDevice(Bundle params, boolean isPairState) {
        String phoneMacAddress = params.getString("phoneMacAddr");
        String phoneName = params.getString("phoneName");
        //空地址或者空名称去除
        if (TextUtils.isEmpty(phoneMacAddress) || TextUtils.isEmpty(phoneName)) {
            return;
        }
        Log.d(TAG, "onUpdate:  " + "   phoneMacAddress:" + phoneMacAddress + " phonename: " + phoneName + " : " + hashMap.containsKey(phoneMacAddress));
        if (!hashMap.containsKey(phoneMacAddress)) {
            hashMap.put(phoneMacAddress, phoneName);
            BTDevice btDevice = new BTDevice();
            btDevice.setDeviceName(phoneName);
            btDevice.setPairState(isPairState);
            btDevice.setDeviceMacAddress(phoneMacAddress);
            btDevice.setTime(System.currentTimeMillis());
            btPairAdapter.setDataItem(btDevice);
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }

    @Override
    public void onItemClick(int position, BTDevice folderBean) {

    }

    @Override
    public void onDeleteItem(BTDevice folderBean) {
        hashMap.remove(folderBean.getDeviceMacAddress());
        ApiBt.cmd(ApiBt.CMD_REMOVE_BOND, folderBean.getDeviceMacAddress());
    }
}