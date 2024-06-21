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
import com.octopus.android.car.apps.databinding.FragmentCallingBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拨打电话界面
 */
public class BluetoothCallingFragment extends BaseViewBindingFragment<FragmentCallingBinding> {
    private final String TAG = "BluetoothPhoneFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    public BluetoothCallingFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothCallingFragment newInstance(int columnCount) {
        BluetoothCallingFragment fragment = new BluetoothCallingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentCallingBinding onCreateViewBinding() {
        return FragmentCallingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.CMD_HANG,//挂断
                ApiBt.CMD_LINK,//接听
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
            case ApiBt.CMD_HANG:
                //电话本信息
                Log.d(TAG, "onUpdate: " + params.getString("value") + "   id: " + id);
                break;
            case ApiBt.CMD_LINK:
                Log.d(TAG, "onUpdate: " + params.getString("value"));
                break;
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }
}