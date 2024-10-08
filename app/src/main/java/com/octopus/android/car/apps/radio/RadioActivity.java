package com.octopus.android.car.apps.radio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.car.api.ApiKit;
import com.car.api.ApiMain;
import com.car.api.ApiRadio;
import com.car.api.CarService;
import com.car.ipc.Connection;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.databinding.ActivityRadioBinding;
import com.octopus.android.car.apps.equalizer.EqualizerHomeActivity;
import com.zhuchao.android.session.MApplication;
import com.zhuchao.android.session.base.BaseActivity;

import java.text.DecimalFormat;
import java.util.Objects;

public class RadioActivity extends BaseActivity implements View.OnClickListener, Connection.OnCallbackListener {
    private static final String TAG = "RadioActivity";
    private final String RADIO_PACKAGE_NAME = "com.car.radio";
    private final String[] recommendChannelFM = new String[]{"89.80", "94.20", "91.20", "95.80", "99.10", "101.20"};
    private final String[] recommendChannelAM = new String[]{"522", "612", "635", "755", "845", "956"};
    protected Connection connection = new Connection(this);
    private ActivityRadioBinding binding;
    private int sBand, sFreq;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        // 初始化绑定类实例
        binding = ActivityRadioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.viewFmOrAm.setOnClickListener(this);
        binding.viewRight.setOnClickListener(this);
        binding.viewLeft.setOnClickListener(this);
        binding.viewSearch.setOnClickListener(this);
        binding.viewNextChannel.setOnClickListener(this);
        binding.viewPrevChannel.setOnClickListener(this);
        binding.viewAF.setOnClickListener(this);
        binding.imageAfBg.setOnClickListener(this);
        binding.viewTA.setOnClickListener(this);
        binding.imageTa.setOnClickListener(this);
        binding.viewLeftChannel1.setOnClickListener(this);
        binding.viewLeftChannel2.setOnClickListener(this);
        binding.viewLeftChannel3.setOnClickListener(this);
        binding.viewRightChannel1.setOnClickListener(this);
        binding.viewRightChannel2.setOnClickListener(this);
        binding.viewRightChannel3.setOnClickListener(this);
        binding.viewRDS.setOnClickListener(this);
        binding.viewRDSVi.setOnClickListener(this);
        binding.viewDoubleCircle.setOnClickListener(this);
        binding.viewPTY.setOnClickListener(this);
        binding.viewSignal.setOnClickListener(this);
        binding.viewEq.setOnClickListener(this);

        binding.seekBarFm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int index, boolean fromUser) {
                progress = index;
                //                ApiRadio.freq(ApiRadio.FREQ_DIRECT, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onProgressChanged: stop" + progress);
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, progress);
            }
        });
        binding.seekBarAm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int index, boolean fromUser) {
                progress = index;
                //                ApiRadio.freq(ApiRadio.FREQ_DIRECT, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onProgressChanged: stop" + progress);
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, progress);
            }
        });
    }

    /**
     * @param list 频道内容
     * @param fmAm 0代表 fm  1代表 AM
     */
    @SuppressLint("SetTextI18n")
    private void setChanelDate(String[] list, int fmAm) {
        //赋值到推荐频道
        if (fmAm == 0) {
            binding.tvSearchUnit1.setText("MHz");
            binding.tvSearchUnit2.setText("MHz");
            binding.tvSearchUnit3.setText("MHz");
            binding.tvSearchUnit4.setText("MHz");
            binding.tvSearchUnit5.setText("MHz");
            binding.tvSearchUnit6.setText("MHz");
            binding.tvMaxChannel.setText("108.00");
            binding.tvMinChannel.setText("87.50");
        } else {
            binding.tvSearchUnit1.setText("KHz");
            binding.tvSearchUnit2.setText("KHz");
            binding.tvSearchUnit3.setText("KHz");
            binding.tvSearchUnit4.setText("KHz");
            binding.tvSearchUnit5.setText("KHz");
            binding.tvSearchUnit6.setText("KHz");
            binding.tvMaxChannel.setText("1620");
            binding.tvMinChannel.setText("522");
        }
        binding.tvSearchChannel1.setText(list[0]);
        binding.tvSearchChannel2.setText(list[1]);
        binding.tvSearchChannel3.setText(list[2]);
        binding.tvSearchChannel4.setText(list[3]);
        binding.tvSearchChannel5.setText(list[4]);
        binding.tvSearchChannel6.setText(list[5]);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viewFmOrAm) {
            if (sBand >= ApiRadio.BAND_AM_INDEX_BEGIN && sBand < ApiRadio.BAND_AM_INDEX_END) {
                //AM
                ApiRadio.band(ApiRadio.BAND_FM_INDEX_BEGIN);
            } else if (sBand >= ApiRadio.BAND_FM_INDEX_BEGIN && sBand < ApiRadio.BAND_FM_INDEX_END) {
                //FM
                ApiRadio.band(ApiRadio.BAND_AM_INDEX_BEGIN);
            }
        } else if (v.getId() == R.id.viewSearch) {
            ApiRadio.seekUp();
        } else if (v.getId() == R.id.viewRight) {
            ApiRadio.nextChannel();
        } else if (v.getId() == R.id.viewLeft) {
            ApiRadio.prevChannel();
        } else if (v.getId() == R.id.viewNextChannel) {
            ApiRadio.freqUp();
        } else if (v.getId() == R.id.viewPrevChannel) {
            ApiRadio.freqDown();
        } else if (v.getId() == R.id.viewAF || v.getId() == R.id.image_af_bg) {
            if (binding.imageAfOff.getVisibility() == View.VISIBLE) {
                ApiRadio.rdsAfEnable(0);
            } else {
                ApiRadio.rdsAfEnable(1);
            }
        } else if (v.getId() == R.id.viewTA || v.getId() == R.id.image_ta) {
            if (binding.imageTaOff.getVisibility() == View.VISIBLE) {
                ApiRadio.rdsTaEnable(0);
            } else {
                ApiRadio.rdsTaEnable(1);
            }
        } else if (v.getId() == R.id.viewLeftChannel1) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[0].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[0]));
            }

        } else if (v.getId() == R.id.viewLeftChannel2) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[1].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[1]));
            }

        } else if (v.getId() == R.id.viewLeftChannel3) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[2].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[2]));
            }
        } else if (v.getId() == R.id.viewRightChannel1) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[3].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[3]));
            }
        } else if (v.getId() == R.id.viewRightChannel2) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[4].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[4]));
            }
        } else if (v.getId() == R.id.viewRightChannel3) {
            if (binding.tvChannelFM.getVisibility() == View.VISIBLE) {
                String end = recommendChannelFM[5].replace(".", "");
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(end));
            } else {
                ApiRadio.freq(ApiRadio.FREQ_DIRECT, Integer.parseInt(recommendChannelAM[5]));
            }
        } else if (v.getId() == R.id.viewRDS) {
            ApiRadio.rdsEnable(binding.viewRDS.isSelected() ? 0 : 1);
        } else if (v.getId() == R.id.viewDoubleCircle) {
            ApiRadio.stero(binding.viewDoubleCircle.isSelected() ? 0 : 1);
        } else if (v.getId() == R.id.viewPTY) {
            showAlertDialogPTY(ApiRadio.PTY_DISPLAY);
        } else if (v.getId() == R.id.viewSignal) {
            ApiRadio.loc(binding.viewSignal.isSelected() ? 0 : 1);
        } else if (v.getId() == R.id.viewEq) {
            openLocalActivity(EqualizerHomeActivity.class);
        }

    }

    /**
     * 弹框选项
     *
     * @param items pty类型
     */
    private void showAlertDialogPTY(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PTY");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 这里处理选项点击事件
                Toast.makeText(getApplicationContext(), "选择了: " + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 关闭弹框
                ApiRadio.rdsPtySeek(which);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //创建远程IPC连接
        connection.connect(MApplication.getAppContext());

        //打开收音机
        ApiMain.appId(ApiMain.APP_ID_RADIO, ApiMain.APP_ID_RADIO);
        CarService.me().cmd(ApiRadio.CMD_POWER, ApiKit.ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        //断开IPC连接
        connection.disconnect(MApplication.getAppContext());
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiRadio.UPDATE_BAND, ApiRadio.UPDATE_FREQ, ApiRadio.UPDATE_RDS_AF_ENABLE, ApiRadio.UPDATE_RDS_TA_ENABLE, ApiRadio.UPDATE_RDS_ENABLE, ApiRadio.UPDATE_STEREO, ApiRadio.UPDATE_LOC}, iCallback, true);
    }

    @Override
    public void onUpdate(Bundle params) {
        if (params == null) return;
        String id = params.getString("id");
        int value = params.getInt("value");
        Log.d(TAG, "onUpdate: " + id + " value " + value);
        switch (Objects.requireNonNull(id)) {
            case ApiRadio.UPDATE_BAND:
                sBand = value;
                updateView();
                break;
            case ApiRadio.UPDATE_FREQ:
                sFreq = value;
                updateView();
                break;
            case ApiRadio.UPDATE_RDS_AF_ENABLE:
                if (value == 0) {
                    binding.imageAfOff.setVisibility(View.GONE);
                } else if (value == 1) {
                    binding.imageAfOff.setVisibility(View.VISIBLE);
                }
                break;
            case ApiRadio.UPDATE_RDS_TA_ENABLE:
                if (value == 0) {
                    binding.imageTaOff.setVisibility(View.GONE);
                } else if (value == 1) {
                    binding.imageTaOff.setVisibility(View.VISIBLE);
                }
                break;
            case ApiRadio.UPDATE_RDS_ENABLE:
                binding.viewRDS.setSelected(value == 1);
                break;
            case ApiRadio.UPDATE_STEREO:
                if (value == 0) {
                    binding.viewDoubleCircle.setSelected(false);
                } else if (value == 1) {
                    binding.viewDoubleCircle.setSelected(true);
                }
                break;
            case ApiRadio.UPDATE_LOC:
                if (value == 0) {
                    binding.viewSignal.setSelected(false);
                } else if (value == 1) {
                    binding.viewSignal.setSelected(true);
                }
                break;
        }

    }

    /**
     * 更新FM/AM状态
     */
    @SuppressLint("SetTextI18n")
    private void updateView() {
        if (sBand >= ApiRadio.BAND_AM_INDEX_BEGIN && sBand < ApiRadio.BAND_AM_INDEX_END) {
            //AM
            String freqText = String.valueOf(sFreq);
            binding.tvChannel.setText(freqText);
            binding.tvUnit.setText("KHz");
            binding.tvChannelFM.setVisibility(View.INVISIBLE);
            binding.tvChannelAM.setVisibility(View.VISIBLE);
            setChanelDate(recommendChannelAM, 1);
            binding.seekBarFm.setVisibility(View.GONE);
            //更新频道滑动条
            binding.seekBarAm.setProgress(sFreq);
            binding.seekBarAm.setVisibility(View.VISIBLE);
        } else if (sBand >= ApiRadio.BAND_FM_INDEX_BEGIN && sBand < ApiRadio.BAND_FM_INDEX_END) {
            //FM
            double d = sFreq * 1.0 / 100;
            if (d < 17) {
                return;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            String formattedNumber = decimalFormat.format(d);
            String freqText = formattedNumber + "0";
            binding.tvChannel.setText(freqText);
            binding.tvUnit.setText("MHz");
            binding.tvChannelFM.setVisibility(View.VISIBLE);
            binding.tvChannelAM.setVisibility(View.INVISIBLE);
            setChanelDate(recommendChannelFM, 0);
            String end = freqText.replace(".", "");
            //更新频道滑动条
            binding.seekBarAm.setVisibility(View.GONE);
            binding.seekBarFm.setProgress(Integer.parseInt(end));
            binding.seekBarFm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭收音机
        //        CarService.me().cmd(ApiMain.CMD_KILL_APP, RADIO_PACKAGE_NAME);
    }
}