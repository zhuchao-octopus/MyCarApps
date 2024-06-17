package com.octopus.android.car.apps.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothDialBinding;

/**
 * A fragment representing a list of Items.
 */
public class BluetoothDialFragment extends BaseViewBindingFragment<FragmentBluetoothDialBinding> implements View.OnClickListener {
    private final String TAG = "BluetoothDialFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    public BluetoothDialFragment() {
    }

    @Override
    protected FragmentBluetoothDialBinding onCreateViewBinding() {
        return FragmentBluetoothDialBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        int childCount = binding.viewDial.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout child = (LinearLayout) binding.viewDial.getChildAt(i);
            int childLL = child.getChildCount();
            for (int j = 0; j < childLL; j++) {
                TextView childView = (TextView) child.getChildAt(j);
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 处理点击事件
                        if (binding.tvPhoneNumber.getText().toString().length() > 12) {
//                            Toast.makeText(getContext(), "号码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String number = binding.tvPhoneNumber.getText().toString();
                        String numberAll = number + childView.getText().toString();
                        binding.tvPhoneNumber.setText(numberAll);
                    }
                });
            }
        }
        binding.viewDelete.setOnClickListener(this);
        binding.viewCall.setOnClickListener(this);
        binding.viewHung.setOnClickListener(this);
        binding.viewVoice.setOnClickListener(this);
        binding.viewDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                binding.tvPhoneNumber.setText("");
                return false;
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viewDelete) {
            //删除后一个号码
            String number = binding.tvPhoneNumber.getText().toString();
            if (number.length() == 1) {
                binding.tvPhoneNumber.setText("");
                return;
            }
            if (number.isEmpty()) {
                return;
            }
            number = number.substring(0, number.length() - 1);
            binding.tvPhoneNumber.setText(number);
        } else if (v.getId() == R.id.viewCall) {
            //拨打电话
            ApiBt.dial(binding.tvPhoneNumber.getText().toString());
        } else if (v.getId() == R.id.viewHung) {
            //挂断电话
            ApiBt.hang();
        } else if (v.getId() == R.id.viewVoice) {
            //音量调节

        }

    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
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
        if (TextUtils.isEmpty(id)) {
            return;
        }
        switch (id) {
            case ApiBt.UPDATE_PHONE_STATE:
                String phoneStr = getPhoneStateString(params.getInt("value"));
                binding.phoneState.setText(phoneStr);
                break;
        }
    }

    public static String getPhoneStateString(int state) {
        switch (state) {
            //已断开
            case ApiBt.PHONE_STATE_DISCONNECTED:
                return "";
            //连接中
            case ApiBt.PHONE_STATE_LINK:
                return "";
            //已连接
            case ApiBt.PHONE_STATE_CONNECTED:
                return "";
            case ApiBt.PHONE_STATE_DIAL:
                return "拨号中";
            case ApiBt.PHONE_STATE_RING:
                return "响铃中/来电";
            case ApiBt.PHONE_STATE_TALK:
                return "通话中";
            //配对中
            case ApiBt.PHONE_STATE_PAIR:
                return "";
        }
        return "未知";
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }
}