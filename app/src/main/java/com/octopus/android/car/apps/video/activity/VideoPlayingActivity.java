package com.octopus.android.car.apps.video.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.ActivityVideoPlayingBinding;
import com.zhuchao.android.fbase.PlaybackEvent;
import com.zhuchao.android.fbase.PlayerStatusInfo;
import com.zhuchao.android.fbase.eventinterface.PlayerCallback;
import com.zhuchao.android.session.BaseActivity;
import com.zhuchao.android.session.Cabinet;

import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayingActivity extends BaseActivity implements PlayerCallback, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = "VideoPlayingActivity";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private boolean mVisible = true;
    private final Handler mHideHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
    private ActivityVideoPlayingBinding binding;
    private SurfaceView mContentView;
    private View mControlsView;
    private SeekBar mProgressSeekBar;
    private TextView mTextViewCurrentTime;
    private TextView mTextViewTotalTime;
    private ImageView mImageViewPlayPause;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                Objects.requireNonNull(mContentView.getWindowInsetsController()).hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContentSurfaceView;

        Cabinet.getPlayManager().setSurfaceView(binding.fullscreenContentSurfaceView);
        binding.fullscreenContentSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        ///binding.getRoot().setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View v) {
        ///        toggle();
        ///    }
        ///});

        findViewById(R.id.ivList).setOnClickListener(this);
        findViewById(R.id.ivPrev).setOnClickListener(this);
        findViewById(R.id.ivNext).setOnClickListener(this);
        findViewById(R.id.ivPlay).setOnClickListener(this);
        findViewById(R.id.ivEq).setOnClickListener(this);
        mProgressSeekBar = ((SeekBar) findViewById(R.id.ivProgress));
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        mProgressSeekBar.setThumb(new ColorDrawable(Color.TRANSPARENT));
        mTextViewCurrentTime = findViewById(R.id.tvCurrentPlayTime);
        mTextViewTotalTime = findViewById(R.id.tvVideoTotalTime);
        mImageViewPlayPause = findViewById(R.id.ivPlay);
        findViewById(R.id.video_play_controller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mProgressSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Cabinet.getPlayManager().registerStatusListener(this);
        delayedHide(8000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ///MMLog.d(TAG,"onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///MMLog.d(TAG,"onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ///MMLog.d(TAG,"onResume()");
        ///Cabinet.getPlayManager().setSurfaceView(binding.fullscreenContentSurfaceView);
        Cabinet.getPlayManager().restartPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ///MMLog.d(TAG,"onStop()");
        Cabinet.getPlayManager().stopPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ///MMLog.d(TAG,"onDestroy()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ///MMLog.d(TAG,"onBackPressed()");
    }

    @Override
    public void onEventPlayerStatus(PlayerStatusInfo playerStatusInfo) {
        mProgressSeekBar.setMax((int) playerStatusInfo.getLength());
        mProgressSeekBar.setProgress((int) playerStatusInfo.getTimeChanged());
        mTextViewCurrentTime.setText(convertMillisToTime(playerStatusInfo.getTimeChanged()));
        mTextViewTotalTime.setText(convertMillisToTime(playerStatusInfo.getLength()));

        switch (playerStatusInfo.getEventType()) {
            case PlaybackEvent.Status_Ended:
                mProgressSeekBar.setProgress(mProgressSeekBar.getMax());
                break;
            case PlaybackEvent.Status_Stopped:
            case PlaybackEvent.Status_NothingIdle:
                finish();
                break;
            case PlaybackEvent.Status_Playing:
                mImageViewPlayPause.setImageResource(R.drawable.selector_stop);
                break;
            case PlaybackEvent.Status_Paused:
                mImageViewPlayPause.setImageResource(R.drawable.selector_play);
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        ///findViewById(R.id.ivList).setOnClickListener(this);
        ///findViewById(R.id.ivPrev).setOnClickListener(this);
        ///findViewById(R.id.ivNext).setOnClickListener(this);
        ///findViewById(R.id.ivPlay).setOnClickListener(this);
        ///findViewById(R.id.ivEq).setOnClickListener(this);
        boolean isPlaying = Cabinet.getPlayManager().isPlaying();
        if (v.getId() == R.id.ivList) {
            Cabinet.getPlayManager().stopPlay();
            openLocalActivity(MainVideoActivity.class);
        } else if (v.getId() == R.id.ivPrev) {
            Cabinet.getPlayManager().playPre();
        } else if (v.getId() == R.id.ivNext) {
            Cabinet.getPlayManager().playNext();
        } else if (v.getId() == R.id.ivPlay) {
            Cabinet.getPlayManager().playPause();
            if (isPlaying) mImageViewPlayPause.setImageResource(R.drawable.selector_play);
            else mImageViewPlayPause.setImageResource(R.drawable.selector_stop);
        } else if (v.getId() == R.id.ivEq) {
            Cabinet.getPlayManager().stopPlay();
        }
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

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            Objects.requireNonNull(mContentView.getWindowInsetsController()).show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}