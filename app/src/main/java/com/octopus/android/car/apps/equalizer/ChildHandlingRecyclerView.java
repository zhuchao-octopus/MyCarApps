package com.octopus.android.car.apps.equalizer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ChildHandlingRecyclerView extends RecyclerView {

    private float mInitialTouchX, mInitialTouchY;
    private boolean mChildHandlesTouch = false;

    public ChildHandlingRecyclerView(Context context) {
        super(context);
    }

    public ChildHandlingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouchX = e.getX();
                mInitialTouchY = e.getY();
                mChildHandlesTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mChildHandlesTouch) {
                    return false;
                }

                float x = e.getX();
                float y = e.getY();
                View child = findChildViewUnder(x, y);
                if (child != null && shouldCaptureEvent(child, x, y)) {
                    mChildHandlesTouch = true;
                    return false;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        if (mChildHandlesTouch) {
//            return false;
//        }
        return false;
    }

    private boolean shouldCaptureEvent(View child, float x, float y) {
        x -= mInitialTouchX;
        y -= mInitialTouchY;
        return x >= -child.getScrollX() && x < child.getWidth() + child.getScrollX()
                && y >= -child.getScrollY() && y < child.getHeight() + child.getScrollY();
    }
}