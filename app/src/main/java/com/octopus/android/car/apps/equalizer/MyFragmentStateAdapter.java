package com.octopus.android.car.apps.equalizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyFragmentStateAdapter extends FragmentStateAdapter {

    private String[] titleArrStr;
    private List<Fragment> fragments;

    public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, String[] titleArr, List<Fragment> fragment) {
        super(fragmentActivity);
        this.titleArrStr = titleArr;
        this.fragments = fragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return titleArrStr.length;
    }
}