package com.octopus.android.car.apps.bluetooth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.car.ipc.Connection;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.FragmentBluetoothHomeBinding;
import com.zhuchao.android.session.base.BaseActivity;

public class BluetoothHomeActivity extends BaseActivity implements View.OnClickListener {
    private final BluetoothDialFragment bluetoothDialFragment = new BluetoothDialFragment();
    private final BluetoothPhoneFragment bluetoothPhoneFragment = new BluetoothPhoneFragment();
    private final BluetoothPhoneLogFragment bluetoothPhoneLogFragment = new BluetoothPhoneLogFragment();
    private final BluetoothSettingFragment bluetoothSettingFragment = new BluetoothSettingFragment();
    private final BluetoothPairFragment bluetoothPairFragment = new BluetoothPairFragment();
    private final BluetoothMusicFragment bluetoothMusicFragment = new BluetoothMusicFragment();
    ///private FrameLayout mFrameViewPlayList;
    ///private FrameLayout mFrameViewSD;
    ///private FrameLayout mFrameViewUSB;
    ///private FrameLayout mFrameViewFolder;
    private FragmentBluetoothHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化绑定类实例
        binding = FragmentBluetoothHomeBinding.inflate(getLayoutInflater());
        // 设置内容视图为绑定类根视图
        setContentView(binding.getRoot());
        ///setContentView(R.layout.activity_video);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        /// 强制日间模式
        ///AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /// 强制夜间模式
        ///AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        /// 跟随系统设置
        ///AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        ///mFrameViewPlayList = findViewById(R.id.viewPlayList);
        ///mFrameViewSD = findViewById(R.id.viewSD);
        ///mFrameViewUSB = findViewById(R.id.viewUsb);
        ///mFrameViewFolder = findViewById(R.id.viewFolder);
        /// 设置底部导航栏点击事件

        replaceFragment(R.id.fragment_container, bluetoothDialFragment);
        binding.viewDial.setOnClickListener(this);
        binding.viewPhone.setOnClickListener(this);
        binding.viewCallLog.setOnClickListener(this);
        binding.viewSetting.setOnClickListener(this);
        binding.viewPair.setOnClickListener(this);
        binding.viewMusic.setOnClickListener(this);

        binding.viewDial.setSelected(true);
    }

    @SuppressLint("ResourceAsColor")
    private void unSelectAllTabView() {
        binding.viewDial.setSelected(false);
        binding.viewPhone.setSelected(false);
        binding.viewCallLog.setSelected(false);
        binding.viewSetting.setSelected(false);
        binding.viewPair.setSelected(false);
        binding.viewMusic.setSelected(false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        unSelectAllTabView();
        if (v.getId() == R.id.viewDial) {
            replaceFragment(R.id.fragment_container, bluetoothDialFragment);
            v.setSelected(true);
        } else if (v.getId() == R.id.viewPhone) {
            replaceFragment(R.id.fragment_container, bluetoothPhoneFragment);
            v.setSelected(true);
        } else if (v.getId() == R.id.viewCallLog) {
            replaceFragment(R.id.fragment_container, bluetoothPhoneLogFragment);
            v.setSelected(true);
        } else if (v.getId() == R.id.viewSetting) {
            replaceFragment(R.id.fragment_container, bluetoothSettingFragment);
            v.setSelected(true);
        } else if (v.getId() == R.id.viewPair) {
            replaceFragment(R.id.fragment_container, bluetoothPairFragment);
            v.setSelected(true);
        } else if (v.getId() == R.id.viewMusic) {
            replaceFragment(R.id.fragment_container, bluetoothMusicFragment);
            v.setSelected(true);
        }

    }

}