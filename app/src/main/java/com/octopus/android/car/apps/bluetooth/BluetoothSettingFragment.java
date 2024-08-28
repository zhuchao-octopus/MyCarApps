package com.octopus.android.car.apps.bluetooth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothSettingBinding;

/**
 * A fragment representing a list of Items.
 */
public class BluetoothSettingFragment extends BaseViewBindingFragment<FragmentBluetoothSettingBinding> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private final String TAG = "FolderItemFragment";

    public BluetoothSettingFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothSettingFragment newInstance(int columnCount) {
        BluetoothSettingFragment fragment = new BluetoothSettingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentBluetoothSettingBinding onCreateViewBinding() {
        return FragmentBluetoothSettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        binding.ivEditDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(false);
            }
        });
        binding.ivEditPinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(true);
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
                ApiBt.CMD_LOCAL_NAME,//名称
                ApiBt.UPDATE_PIN_CODE,//名称
        }, iCallback, true);

    }

    @Override
    public void onUpdate(Bundle params) {
        if (binding == null) {
            return;
        }
        if (params == null) return;
        String id = params.getString("id");
        if (id != null) {
            switch (id) {
                case ApiBt.CMD_LOCAL_NAME:
                    String deviceName = params.getString("value");
                    Log.d(TAG, "onUpdate: " + deviceName);
                    binding.tvDeviceName.setText(deviceName);
                    break;
                case ApiBt.UPDATE_PIN_CODE:
                    String pincode = params.getString("value");
                    Log.d(TAG, "onUpdate: " + pincode);
                    binding.tvPinCode.setText(pincode);
                    break;

            }
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }

    private void updateName(boolean isPassword) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (isPassword) {
            builder.setTitle("请输入Pin Password");
        } else {
            builder.setTitle("请输入Device name");
        }
        final EditText input = new EditText(getContext());
        builder.setView(input);
        //        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取输入框的内容
                String content = input.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                // 处理输入的内容
                if (isPassword) {
                    if (content.length() > 3) {
                        ApiBt.pinCode(content);
                    } else {
                        Toast.makeText(getContext(), "密码长度最少4位", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ApiBt.localName(content);
                }
                // ...
                builder.create().dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消操作
                builder.create().dismiss();
            }
        });
        builder.create().show(); // 显示对话框
    }
}