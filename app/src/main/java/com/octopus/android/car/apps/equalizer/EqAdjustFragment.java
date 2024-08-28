package com.octopus.android.car.apps.equalizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.car.api.ApiBt;
import com.car.ipc.ICallback;
import com.car.ipc.IRemote;
import com.octopus.android.car.apps.R;
import com.octopus.android.car.apps.bluetooth.bean.BTDevice;
import com.octopus.android.car.apps.common.BaseViewBindingFragment;
import com.octopus.android.car.apps.databinding.FragmentEqAjustBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * EqAdjustFragment
 */
public class EqAdjustFragment extends BaseViewBindingFragment<FragmentEqAjustBinding> implements View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private final String TAG = "EqAdjustFragment";
    private EqAdjustAdapter eqAdjustAdapter;
    private GestureDetector gestureDetector;
    private float lastX, lastY;

    public static EqAdjustFragment newInstance(int columnCount) {
        EqAdjustFragment fragment = new EqAdjustFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    protected FragmentEqAjustBinding onCreateViewBinding() {
        return FragmentEqAjustBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListener() {
        binding.eqFl.setOnClickListener(this);
        binding.zoneFl.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        List<BTDevice> list = new ArrayList<>();
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        list.add(new BTDevice());
        eqAdjustAdapter = new EqAdjustAdapter(list, new EqAdjustAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BTDevice folderBean) {

            }

            @Override
            public void onDeleteItem(BTDevice folderBean) {

            }
        });
        binding.recyclerView.setAdapter(eqAdjustAdapter);
        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                lastX = e.getRawX();
                lastY = e.getRawY();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float dx = e2.getRawX() - lastX;
                float dy = e2.getRawY() - lastY;
                int newLeft = (int) (binding.zoneRedCursorIv.getLeft() + dx);
                int newTop = (int) (binding.zoneRedCursorIv.getTop() + dy);
                int newRight = (int) (binding.zoneRedCursorIv.getRight() + dx);
                int newBottom = (int) (binding.zoneRedCursorIv.getBottom() + dy);
                // 限制移动范围在外部图片内
                if (newLeft >= 0 && newRight <= binding.zoneScopeLl.getWidth() && newTop >= 0 && newBottom <= binding.zoneScopeLl.getHeight()) {
                    binding.zoneRedCursorIv.layout(newLeft, newTop, newRight, newBottom);
                    Log.d(TAG, "onScroll:  " + newLeft + " newTop:  " + newTop);
                }

                lastX = e2.getRawX();
                lastY = e2.getRawY();
                return true;
            }
        });

        binding.zoneRedCursorIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFragmentVisible(boolean isVisible) {
        super.onFragmentVisible(isVisible);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.eq_fl) {
            binding.zoneLl.setVisibility(View.GONE);
            binding.zoneIv.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.eqIv.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.zone_fl) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.eqIv.setVisibility(View.GONE);
            binding.zoneLl.setVisibility(View.VISIBLE);
            binding.zoneIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnected(IRemote iRemote, ICallback iCallback) throws RemoteException {
        iRemote.register(new String[]{
                //注册想要监听是数据，true代表马上返回需要的值
                ApiBt.UPDATE_PHONE_STATE,//蓝牙状态
        }, iCallback, true);
    }


    @Override
    public void onUpdate(Bundle params) {
        if (params == null) return;
        String id = params.getString("id");
        if (TextUtils.isEmpty(id)) {
            return;
        }
        switch (id) {
            case ApiBt.UPDATE_PHONE_STATE:
                break;
        }
    }

    @Override
    public boolean isUpdateOnUIThread() {
        return true;
    }

}