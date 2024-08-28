package com.octopus.android.car.apps.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.bluetooth.adapter.BtPhoneBookAdapter;
import com.octopus.android.car.apps.bluetooth.bean.PhoneBookBean;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothPhoneLogBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BluetoothPhoneLogFragment extends BaseViewBindingFragment<FragmentBluetoothPhoneLogBinding> implements View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private final String TAG = "BluetoothPhoneLogFragment";
    private BtPhoneBookAdapter btPhoneBookAdapter;
    private List<PhoneBookBean> callIn = new ArrayList<>();
    private List<PhoneBookBean> callOut = new ArrayList<>();
    private List<PhoneBookBean> callMiss = new ArrayList<>();
    private int sPhoneState;

    public BluetoothPhoneLogFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothPhoneLogFragment newInstance(int columnCount) {
        BluetoothPhoneLogFragment fragment = new BluetoothPhoneLogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentBluetoothPhoneLogBinding onCreateViewBinding() {
        return FragmentBluetoothPhoneLogBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        binding.ivInCallLog.setOnClickListener(this);
        binding.ivOutCallLog.setOnClickListener(this);
        binding.ivMissCallLog.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        btPhoneBookAdapter = new BtPhoneBookAdapter(new ArrayList<>());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycleView.setAdapter(btPhoneBookAdapter);
        callIn.clear();
        callOut.clear();
        callMiss.clear();

    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.UPDATE_MISS_CALL_LOG,//未接电话
                ApiBt.UPDATE_IN_CALL_LOG,//打入电话
                ApiBt.UPDATE_OUT_CALL_LOG,//拨打电话
                ApiBt.UPDATE_PHONE_STATE,//蓝牙状态
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
            case ApiBt.UPDATE_IN_CALL_LOG:
                updatePhone(params, 0);
                break;
            case ApiBt.UPDATE_OUT_CALL_LOG:
                updatePhone(params, 1);
                break;
            case ApiBt.UPDATE_MISS_CALL_LOG:
                updatePhone(params, 2);
                break;
            case ApiBt.UPDATE_PHONE_STATE:
                sPhoneState = params.getInt("value");
                break;
        }
    }

    /**
     * @param params 数据源
     * @param type   0 拨入 1 呼出 2未接
     */
    private void updatePhone(Bundle params, int type) {
        String name = params.getString("name");
        String number = params.getString("number");

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
            return;
        }
        if (TextUtils.isEmpty(name)) {
            name = "未知";
        }
        PhoneBookBean phoneBookBean = new PhoneBookBean();
        phoneBookBean.setNumber(number);
        phoneBookBean.setName(name);
        phoneBookBean.setTime(params.getLong("time"));
        if (type == 0) {
            callIn.add(phoneBookBean);

        } else if (type == 1) {
            callOut.add(phoneBookBean);
            btPhoneBookAdapter.setDataItem(phoneBookBean);
        } else if (type == 2) {
            callMiss.add(phoneBookBean);
        }
        Log.d(TAG, "通话联系人 name = " + phoneBookBean.toString());
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivInCallLog) {
            btPhoneBookAdapter.setData(callIn);
        } else if (v.getId() == R.id.ivOutCallLog) {
            btPhoneBookAdapter.setData(callOut);
        } else if (v.getId() == R.id.ivMissCallLog) {
            btPhoneBookAdapter.setData(callMiss);
        } else if (v.getId() == R.id.ivDelete) {
            //删除记录
            List<PhoneBookBean> bookBeanList = btPhoneBookAdapter.getBookDate();
            if (!bookBeanList.isEmpty()) {
                Log.d(TAG, "onClick: " + bookBeanList.size());
                ApiBt.deleteContact(bookBeanList.get(0).getName(), bookBeanList.get(0).getNumber());
                btPhoneBookAdapter.removeData(0);
            }
        }

    }

}