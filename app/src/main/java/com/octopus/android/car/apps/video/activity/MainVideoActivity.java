package com.octopus.android.car.apps.video.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.ActivityVideoMainBinding;
import com.octopus.android.car.apps.video.fragment.FolderItemFragment;
import com.octopus.android.car.apps.video.fragment.PlayingItemFragment;
import com.octopus.android.car.apps.video.fragment.SDItemFragment;
import com.octopus.android.car.apps.video.fragment.USBItemFragment;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseActivity;

public class MainVideoActivity extends BaseActivity implements View.OnClickListener {
    private final PlayingItemFragment mPlayingItemFragment = new PlayingItemFragment();
    private final SDItemFragment mSDItemFragment = new SDItemFragment();
    private final USBItemFragment mUSBItemFragment = new USBItemFragment();
    private final FolderItemFragment mFolderItemFragment = new FolderItemFragment();
    ///private FrameLayout mFrameViewPlayList;
    ///private FrameLayout mFrameViewSD;
    ///private FrameLayout mFrameViewUSB;
    ///private FrameLayout mFrameViewFolder;
    private ActivityVideoMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化绑定类实例
        binding = ActivityVideoMainBinding.inflate(getLayoutInflater());
        // 设置内容视图为绑定类根视图
        setContentView(binding.getRoot());
        ///setContentView(R.layout.activity_video);
        Cabinet.getPlayManager().updateMoviesToPlayList();
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
        ///mFrameViewPlayList.setOnClickListener(this);
        ///mFrameViewSD.setOnClickListener(this);
        ///mFrameViewUSB.setOnClickListener(this);
        ///mFrameViewFolder.setOnClickListener(this);

        replaceFragment(R.id.fragment_container, mPlayingItemFragment);
        binding.ivPlayList.setOnClickListener(this);
        binding.ivSD.setOnClickListener(this);
        binding.ivUsb.setOnClickListener(this);
        binding.ivFolder.setOnClickListener(this);

        binding.tvPlayList.setOnClickListener(this);
        binding.tvSD.setOnClickListener(this);
        binding.tvFolder.setOnClickListener(this);
        binding.tvUSB.setOnClickListener(this);
        binding.ivPlayList.callOnClick();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Hide all fragments
        if (mPlayingItemFragment.isAdded()) fragmentTransaction.hide(mPlayingItemFragment);
        if (mSDItemFragment.isAdded()) fragmentTransaction.hide(mSDItemFragment);
        if (mUSBItemFragment.isAdded()) fragmentTransaction.hide(mUSBItemFragment);
        if (mFolderItemFragment.isAdded()) fragmentTransaction.hide(mFolderItemFragment);

        // Show the selected fragment
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }
        fragmentTransaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    private void unSelectAllTabView() {
        ///mFrameViewPlayList.setSelected(false);
        ///mFrameViewSD.setSelected(false);
        ///mFrameViewUSB.setSelected(false);
        ///mFrameViewFolder.setSelected(false);
        binding.ivPlayList.setSelected(false);
        binding.ivSD.setSelected(false);
        binding.ivUsb.setSelected(false);
        binding.ivFolder.setSelected(false);
        setColor(binding.tvPlayList, R.color.white);
        setColor(binding.tvSD, R.color.white);
        setColor(binding.tvUSB, R.color.white);
        setColor(binding.tvFolder, R.color.white);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        unSelectAllTabView();
        if (v.getId() == R.id.ivPlayList || v.getId() ==R.id.tvPlayList) {
            replaceFragment(R.id.fragment_container, mPlayingItemFragment);
            v.setSelected(true);
            setColor(binding.tvPlayList, R.color.colorAccent);
        } else if (v.getId() == R.id.ivSD || v.getId() ==R.id.tvSD) {
            replaceFragment(R.id.fragment_container, mSDItemFragment);
            v.setSelected(true);
            setColor(binding.tvSD, R.color.colorAccent);
        } else if (v.getId() == R.id.ivUsb || v.getId() ==R.id.tvUSB) {
            replaceFragment(R.id.fragment_container, mUSBItemFragment);
            v.setSelected(true);
            setColor(binding.tvUSB, R.color.colorAccent);
        } else if (v.getId() == R.id.ivFolder || v.getId() ==R.id.tvFolder) {
            replaceFragment(R.id.fragment_container, mFolderItemFragment);
            v.setSelected(true);
            setColor(binding.tvFolder, R.color.colorAccent);
        }
    }
}