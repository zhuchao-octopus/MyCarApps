package com.octopus.android.car.apps.audio.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.audio.adapter.MyFragmentAdapter;
import com.octopus.android.car.apps.audio.fragment.AlbumsItemFragment;
import com.octopus.android.car.apps.audio.fragment.ArtistsItemFragment;
import com.octopus.android.car.apps.audio.fragment.CollectionItemFragment;
import com.octopus.android.car.apps.audio.fragment.FolderItemFragment;
import com.octopus.android.car.apps.audio.fragment.PlayingItemFragment;
import com.octopus.android.car.apps.databinding.ActivityMusicMainBinding;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseActivity;
import com.zhuchao.android.session.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

public class MainMusicActivity extends BaseActivity implements View.OnClickListener {
    private final PlayingItemFragment mPlayingItemFragment = new PlayingItemFragment();
    private final AlbumsItemFragment mAlbumsItemFragment = new AlbumsItemFragment();
    private final ArtistsItemFragment mArtistsItemFragment = new ArtistsItemFragment();
    private final CollectionItemFragment mCollectionItemFragment = new CollectionItemFragment();
    private final FolderItemFragment mFolderItemFragment = new FolderItemFragment();

    private ActivityMusicMainBinding binding;
    private ViewPager2 viewPager;
    private MyFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化绑定类实例
        binding = ActivityMusicMainBinding.inflate(getLayoutInflater());
        // 设置内容视图为绑定类根视图
        setContentView(binding.getRoot());
        ///setContentView(R.layout.activity_video);
        Cabinet.getPlayManager().updateMoviesToPlayList();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // 强制日间模式
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // 强制夜间模式
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // 跟随系统设置
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        ///replaceFragment(R.id.fragment_container, mPlayingItemFragment);
        viewPager = findViewById(R.id.viewPager);
        binding.ivList.setOnClickListener(this);
        binding.ivPlayList.setOnClickListener(this);
        binding.ivArtists.setOnClickListener(this);
        binding.ivAlbums.setOnClickListener(this);
        binding.ivFolder.setOnClickListener(this);
        binding.ivCollection.setOnClickListener(this);
        binding.ivEq.setOnClickListener(this);
        binding.ivPlayList.callOnClick();

        List<BaseFragment> fragmentList = Arrays.asList(mPlayingItemFragment, mAlbumsItemFragment, mArtistsItemFragment, mCollectionItemFragment, mFolderItemFragment);
        fragmentAdapter = new MyFragmentAdapter(this, fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Handle page selection
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void unSelectAllTabView() {
        binding.ivList.setSelected(false);
        binding.ivPlayList.setSelected(false);
        binding.ivArtists.setSelected(false);
        binding.ivAlbums.setSelected(false);
        binding.ivFolder.setSelected(false);
        binding.ivCollection.setSelected(false);
        binding.ivEq.setSelected(false);

        setColor(binding.tvPlayList, R.color.white);
        setColor(binding.tvArtists, R.color.white);
        setColor(binding.tvAlbums, R.color.white);
        setColor(binding.tvFolder, R.color.white);
        setColor(binding.tvCollection, R.color.white);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        unSelectAllTabView();
        if (v.getId() == R.id.ivPlayList) {
            ///replaceFragment(R.id.fragment_container, mPlayingItemFragment);
            viewPager.setCurrentItem(0);
            v.setSelected(true);
            setColor(binding.tvPlayList, R.color.colorAccent);
        } else if (v.getId() == R.id.ivArtists) {
            ///replaceFragment(R.id.fragment_container, mArtistsItemFragment);
            viewPager.setCurrentItem(1);
            v.setSelected(true);
            setColor(binding.tvArtists, R.color.colorAccent);
        } else if (v.getId() == R.id.ivAlbums) {
            ///replaceFragment(R.id.fragment_container, mAlbumsItemFragment);
            viewPager.setCurrentItem(2);
            v.setSelected(true);
            setColor(binding.tvAlbums, R.color.colorAccent);
        } else if (v.getId() == R.id.viewFolder) {
            ///replaceFragment(R.id.fragment_container, mFolderItemFragment);
            viewPager.setCurrentItem(3);
            v.setSelected(true);
            setColor(binding.tvFolder, R.color.colorAccent);
        } else if (v.getId() == R.id.ivCollection) {
            ///replaceFragment(R.id.fragment_container, mCollectionItemFragment);
            viewPager.setCurrentItem(4);
            v.setSelected(true);
            setColor(binding.tvCollection, R.color.colorAccent);
        }
    }
}