package com.octopus.android.car.apps.audio.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.ActivityMusicPlayingBinding;
import com.zhuchao.android.fbase.DataID;
import com.zhuchao.android.fbase.PlaybackEvent;
import com.zhuchao.android.fbase.PlayerStatusInfo;
import com.zhuchao.android.fbase.eventinterface.PlayerCallback;
import com.zhuchao.android.session.Cabinet;
import com.zhuchao.android.session.base.BaseActivity;
import com.zhuchao.android.video.OMedia;

public class MusicPlayingActivity extends BaseActivity implements PlayerCallback, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = "MusicPlayingActivity";
    private SeekBar mProgressSeekBar;
    private TextView mTextViewCurrentTime;
    private TextView mTextViewTotalTime;
    private ImageView mImageViewPlayPause;
    private ActivityMusicPlayingBinding binding;
    ///private OMedia oMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///setContentView(R.layout.activity_music_playing);
        binding = ActivityMusicPlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvMusicName.setSelected(true);
        mProgressSeekBar = ((SeekBar) findViewById(R.id.musicSeekBar));
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        //        mProgressSeekBar.setThumb(new ColorDrawable(Color.TRANSPARENT));
        mTextViewCurrentTime = findViewById(R.id.tvCurrentTime);
        mTextViewTotalTime = findViewById(R.id.tvTotalTime);
        mImageViewPlayPause = findViewById(R.id.ivPlay);

        binding.ivPlayList.setOnClickListener(this);
        binding.viewMode.setOnClickListener(this);
        binding.viewCollection.setOnClickListener(this);
        binding.viewPlay.setOnClickListener(this);
        binding.viewNext.setOnClickListener(this);
        binding.viewPrev.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(binding.ivPlayList)) {
            finish();
        } else if (v.equals(binding.viewMode)) {
            switch (Cabinet.getPlayManager().getPlayOrder()) {
                case DataID.PLAY_MANAGER_PLAY_ORDER2:
                    Cabinet.getPlayManager().setPlayOrder(DataID.PLAY_MANAGER_PLAY_ORDER4);
                    binding.ivMode.setImageResource(R.mipmap.music_play_mode_loop_single);
                    break;
                case DataID.PLAY_MANAGER_PLAY_ORDER4:
                    Cabinet.getPlayManager().setPlayOrder(DataID.PLAY_MANAGER_PLAY_ORDER5);
                    binding.ivMode.setImageResource(R.mipmap.music_play_mode_shuffle);
                    break;
                default:
                    Cabinet.getPlayManager().setPlayOrder(DataID.PLAY_MANAGER_PLAY_ORDER2);
                    binding.ivMode.setImageResource(R.mipmap.music_play_mode_loop);
                    break;
            }

        } else if (v.equals(binding.viewCollection)) {
            OMedia oMedia = Cabinet.getPlayManager().getPlayingMedia();
            Cabinet.getPlayManager().getFavouriteList().addRow(oMedia);
        } else if (v.equals(binding.viewPlay)) {
            Cabinet.getPlayManager().playPause();
        } else if (v.equals(binding.viewNext)) {
            Cabinet.getPlayManager().playNext();
        } else if (v.equals(binding.viewPrev)) {
            Cabinet.getPlayManager().playPre();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OMedia oMedia = Cabinet.getPlayManager().getPlayingMedia();
        if (oMedia != null) {
            binding.tvMusicName.setText(oMedia.getMovie().getTitle());
            binding.tvAlbumName.setText(oMedia.getMovie().getAlbum());
            binding.tvArtistsName.setText(oMedia.getMovie().getArtist());
        }
        Cabinet.getPlayManager().registerStatusListener(this);
        Cabinet.getPlayManager().startToPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Cabinet.getPlayManager().registerStatusListener(null);
    }

    @Override
    protected void onDestroy() {
        Cabinet.getPlayManager().stopPlay();
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (Cabinet.getPlayManager().getPlayingMedia() == null || seekBar == null) return;
        Cabinet.getPlayManager().getPlayingMedia().setTime(seekBar.getProgress());
    }

    @Override
    public void onEventPlayerStatus(PlayerStatusInfo playerStatusInfo) {
        ///MMLog.d(TAG,playerStatusInfo.toString());
        OMedia oMedia1 = (OMedia) playerStatusInfo.getObj();
        mProgressSeekBar.setMax((int) playerStatusInfo.getLength());
        mProgressSeekBar.setProgress((int) playerStatusInfo.getTimeChanged());
        mTextViewCurrentTime.setText(convertMillisToTime(playerStatusInfo.getTimeChanged()));
        mTextViewTotalTime.setText(convertMillisToTime(playerStatusInfo.getLength()));
        Log.d(TAG, "onEventPlayerStatus: " + (int) playerStatusInfo.getTimeChanged());
        switch (playerStatusInfo.getEventType()) {
            case PlaybackEvent.Status_Opening:
            case PlaybackEvent.Status_Buffering:
                binding.tvMusicName.setText(oMedia1.getMovie().getName());
                binding.tvAlbumName.setText(oMedia1.getMovie().getAlbum());
                binding.tvArtistsName.setText(oMedia1.getMovie().getArtist());
                break;
            case PlaybackEvent.Status_Ended:
                mProgressSeekBar.setProgress(mProgressSeekBar.getMax());
                break;
            case PlaybackEvent.Status_Stopped:
            case PlaybackEvent.Status_NothingIdle:
                finish();
                break;
            case PlaybackEvent.Status_Playing:
                mImageViewPlayPause.setImageResource(R.drawable.selector_stop);
                binding.playPause.setText("Pause");
                break;
            case PlaybackEvent.Status_Paused:
                mImageViewPlayPause.setImageResource(R.drawable.selector_play);
                binding.playPause.setText("Play");
                break;
            default:
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    private String convertMillisToTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = seconds % 3600 / 60;
        int remainingSeconds = seconds % 60;
        if (hours == 0L) {
            return String.format("%02d:%02d", minutes, remainingSeconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}