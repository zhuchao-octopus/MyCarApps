package com.octopus.android.car.apps.audio.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zhuchao.android.session.base.BaseFragment;

import java.util.List;

public class MyFragmentAdapter extends FragmentStateAdapter {

    private final List<BaseFragment> fragmentList;

    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> fragmentList) {
        super(fragmentActivity);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
