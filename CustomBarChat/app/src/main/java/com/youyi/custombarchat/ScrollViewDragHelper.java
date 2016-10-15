package com.youyi.custombarchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Roy on 16/10/14.
 */
public class ScrollViewDragHelper extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private ScrollBarPic scrollBarPic;
    private int width;// 子孩子的宽度
    private int height;
    private int perBarWidth;// 每个柱子的宽度
    private int padding;// 柱子之间的间隔
    private int initialposition;// 初始位置
    private int leftpadding;// 父容器的padding值（左右padding值应该相等）


    private boolean flag = false;
    private int index;// 记录回退位置

    @SuppressLint("NewApi")
    public ScrollViewDragHelper(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScrollViewDragHelper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewDragHelper(Context context) {
        this(context, null);
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f,
                new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        return true;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child,
                                                           int left, int dx) {
                        if (left > leftpadding) {
                            left = leftpadding;
                        } else if (left < -initialposition + leftpadding) {
                            left = -initialposition + leftpadding;
                        }
                        if (Math.abs(dx) > 2) {// 记录是否应该回调柱子的点击事件
                            flag = true;
                        }
                        return left;
                    }

                    @Override
                    public void onViewPositionChanged(View changedView,
                                                      int left, int top, int dx, int dy) {
                        super.onViewPositionChanged(changedView, left, top, dx,
                                dy);
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel,
                                               float yvel) {
                        super.onViewReleased(releasedChild, xvel, yvel);
                        if (Math.abs(scrollBarPic.getLeft()) < (perBarWidth + padding) / 2) {
                            // 0号
                            index = 0;
                        } else {
                            // 其余的
                            index = Math.abs((scrollBarPic.getLeft() - (perBarWidth + padding) / 2)
                                    / (perBarWidth + padding));
                        }
                        if (viewDragHelper.smoothSlideViewTo(scrollBarPic,
                                -index * (perBarWidth + padding) + leftpadding,
                                0)) {
                            // 返回true， 说明还没有移动到指定位置。需要重绘界面
                            ViewCompat
                                    .postInvalidateOnAnimation(ScrollViewDragHelper.this);
                        }
                    }

                });

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:  //点击
                scrollBarPic.clickcallback(flag);
                break;

            default:
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        scrollBarPic = (ScrollBarPic) getChildAt(0);
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = scrollBarPic.getMeasuredWidth();
        height = scrollBarPic.getMeasuredHeight();
        padding = scrollBarPic.getPadding();
        perBarWidth = scrollBarPic.getPerbarWidth();
        initialposition = width - (padding * 7 + perBarWidth * 7);
        leftpadding = getPaddingLeft();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollBarPic.layout(-initialposition + leftpadding, 0, width
                - initialposition + leftpadding, height);
    }

}
