package com.octopus.android.car.apps.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.car.ipc.Connection;
import com.zhuchao.android.session.MApplication;
import com.zhuchao.android.session.base.BaseFragment;

public abstract class BaseViewBindingFragment<T extends ViewBinding> extends BaseFragment implements Connection.OnCallbackListener {
    protected T binding;
    protected Connection connection = new Connection(this);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 调用onCreateViewBinding方法获取binding
        binding = onCreateViewBinding();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //创建远程IPC连接
        connection.connect(MApplication.getAppContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        //断开IPC连接
        connection.disconnect(MApplication.getAppContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 引用置空处理
        binding = null;
    }

    protected abstract T onCreateViewBinding();

    /**
     * 设置事件监听
     */
    protected abstract void setListener();
}
