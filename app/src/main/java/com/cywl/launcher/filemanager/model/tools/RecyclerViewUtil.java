package com.cywl.launcher.filemanager.model.tools;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cywl.launcher.filemanager.application.MyApplication;
import com.cywl.launcher.filemanager.model.listener.OnRecyclerViewItemListener;

/**
 * @author Altair
 * @date :2019.11.15 上午 10:12
 * @description: recyclerView add itemClickListener
 */
public class RecyclerViewUtil {
    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private OnRecyclerViewItemListener mItemListener;

    public RecyclerViewUtil(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;

        mGestureDetector = new GestureDetector(MyApplication.getInstance(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {// 长按事件
                        super.onLongPress(e);
                        if (mItemListener != null) {
                            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null) {
                                int position = mRecyclerView.getChildLayoutPosition(childView);
                                mItemListener.onItemLongClickListener(childView, position);
                            }
                        }
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {// 单击事件
                        if (mItemListener != null) {
                            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null) {
                                int position = mRecyclerView.getChildLayoutPosition(childView);
                                mItemListener.onItemClickListener(childView, position);
                                return true;
                            }
                        }
                        return super.onSingleTapUp(e);
                    }
                });

        RecyclerView.SimpleOnItemTouchListener mSimpleOnItemTouchListener =
                new RecyclerView.SimpleOnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return mGestureDetector.onTouchEvent(e);
                    }
                };
        mRecyclerView.addOnItemTouchListener(mSimpleOnItemTouchListener);
    }

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        this.mItemListener = listener;
    }
}