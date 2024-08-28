package com.octopus.android.car.apps.equalizer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.FragmentEqualizerHomeBinding;
import com.zhuchao.android.session.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class EqualizerHomeActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "EqualizerHomeActivity";
    private FragmentEqualizerHomeBinding binding;
    private String[] titleArr = {"Flat", "Pop", "Mormal", "News", "Jazz"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化绑定类实例
        binding = FragmentEqualizerHomeBinding.inflate(getLayoutInflater());
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

        binding.eqLast.setOnClickListener(this);
        binding.eqNext.setOnClickListener(this);
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < titleArr.length; i++) {
            EqAdjustFragment bluetoothDialFragment = new EqAdjustFragment();
            list.add(bluetoothDialFragment);
        }
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(this, titleArr, list);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setOffscreenPageLimit(list.size());
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titleArr[position]);
                Log.d(TAG, "onConfigureTab: " + position);
            }
        });
        mediator.attach();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.eq_last) {
            int index = binding.tabLayout.getSelectedTabPosition();
            if (index > 0) {
                TabLayout.Tab tab1 = binding.tabLayout.getTabAt(index - 1);
                binding.tabLayout.selectTab(tab1);
            }
        } else if (v.getId() == R.id.eq_next) {
            int index = binding.tabLayout.getSelectedTabPosition();
            if (index < titleArr.length - 1) {
                TabLayout.Tab tab1 = binding.tabLayout.getTabAt(index + 1);
                binding.tabLayout.selectTab(tab1);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}