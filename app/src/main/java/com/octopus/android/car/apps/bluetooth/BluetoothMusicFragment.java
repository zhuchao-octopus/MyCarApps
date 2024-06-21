package com.octopus.android.car.apps.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.car.api.ApiBt;
import com.car.api.ApiMain;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentBluetoothMusicBinding;
import com.octopus.android.car.apps.databinding.FragmentBluetoothPhoneBinding;

/**
 * A fragment representing a list of Items.
 */
public class BluetoothMusicFragment extends BaseViewBindingFragment<FragmentBluetoothMusicBinding> implements View.OnClickListener {
    private final String TAG = "BluetoothMusicFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    public BluetoothMusicFragment() {
    }

    @SuppressWarnings("unused")
    public static BluetoothMusicFragment newInstance(int columnCount) {
        BluetoothMusicFragment fragment = new BluetoothMusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentBluetoothMusicBinding onCreateViewBinding() {
        return FragmentBluetoothMusicBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        //如果需要蓝牙音乐出声音，就需要切蓝牙音乐通道
        ApiMain.appId(ApiMain.APP_ID_BTAV, ApiMain.APP_ID_BTAV);
//        ApiMain.appId(ApiMain.APP_ID_AUDIO_PLAYER, ApiMain.APP_ID_AUDIO_PLAYER);
        binding.viewNext.setOnClickListener(this);
        binding.viewPlay.setOnClickListener(this);
        binding.viewPrev.setOnClickListener(this);

    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.UPDATE_PHONE_STATE,//蓝牙状态
                ApiBt.UPDATE_PLAY_STATE,//音乐状态
                ApiBt.UPDATE_ID3_TITLE, //音乐标题
                ApiBt.UPDATE_ID3_ARTIST,  //音乐作者
                ApiBt.UPDATE_ID3_ALBUM,  //专辑图标
        }, iCallback, true);
    }

    @Override
    public void onUpdate(Bundle params) {
        if (binding == null) {
            return;
        }
        if (params == null) return;
        String id = params.getString("id");
        switch (id) {
            case ApiBt.UPDATE_PHONE_STATE:
                //蓝牙连接状态 0 是未连接  2连接手机
                int blue = params.getInt("value");
                break;
            case ApiBt.UPDATE_ID3_TITLE:
                String title = params.getString("value");
                Log.d(TAG, "onUpdate: " + title);
                binding.tvName.setText(title);
                break;
            case ApiBt.UPDATE_ID3_ARTIST:
                String tvAuthor = params.getString("value");
                binding.tvAuthor.setText(tvAuthor);
                break;
            case ApiBt.UPDATE_PLAY_STATE:
                int musicState = params.getInt("value");
                if (musicState == ApiBt.PLAYSTATE_PAUSE) {
                    binding.ivPlay.setImageResource(R.drawable.selector_play);
                    binding.tvPlay.setText("Play");
                } else if (musicState == ApiBt.PLAYSTATE_PLAY) {
                    binding.ivPlay.setImageResource(R.drawable.selector_stop);
                    binding.tvPlay.setText("Pause");
                }
                break;
            case ApiBt.UPDATE_ID3_ALBUM:
                //sdk没有返回,暂定
                String album = params.getString("value");
                Log.d(TAG, "onUpdate album: " + album);
                break;

        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viewNext) {
            ApiBt.btavNext();
        } else if (v.getId() == R.id.viewPlay) {
            ApiBt.btavPlayPause();
        } else if (v.getId() == R.id.viewPrev) {
            ApiBt.btavPrev();
        }

    }
}