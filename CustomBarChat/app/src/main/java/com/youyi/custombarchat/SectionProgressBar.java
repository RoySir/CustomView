/*
 * Copyright (C) 2016 solartisan/imilk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youyi.custombarchat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 花样可做分段进度条
 * Created by Roy on 16/10/14.
 */
public class SectionProgressBar extends View {
    //#ececec
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#6aa8f7");
    //#b93a2c
    private static final int DEFAULT_FOREGROUND_COLOR = Color.parseColor("#5af0df");

    private static final int DEFAULT_SPACE_COLOR = Color.parseColor("#0076d6");
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffffff");
    private static final int DEFAULT_LIGHT_TEXT_COLOR = Color.parseColor("#89000000");
    //    private static final int DEFAULT_TEXT_BG_COLOR = Color.parseColor("#60a4f7");
    private static final int DEFAULT_TEXT_BG_COLOR = Color.parseColor("#0076d6");
    private static final float DEFAULT_INITIAL_RATIO = 0.00f;//just pinned zero position
    private static final long DEFAULT_TRANSITION_TIME = 1000;
    private int mBackgroundColor;
    private int mForegroundColor;
    private int mSpaceColor;
    private int mTextColor;
    private int mLightTextColor;
    private int mTextBgColor;
    private float mTextSize;
    private float mLightTextSize;
    private float mBarHeight;
    private Drawable mCursorDrawable;
    private Drawable mTextBgCursorDrawable;
    private Paint mBackgroundPaint;
    private Paint mForegroundPaint;
    private Paint mTextPaintBgPaint;
    private Paint mTextPaint;
    private Paint mLightTextPaint;
    private Paint mSectionPaint;
    private int[] mLevelValues;
    private ValueAnimator mRatioAnimator;
    private long mTransitionDuration = DEFAULT_TRANSITION_TIME;
    private float mRatio = DEFAULT_INITIAL_RATIO;
    private RatioPolicy mPolicy = new RatioPolicy() {
        @Override
        public float computeProgressRatio(int current) {
            float ratio = 0.0f;

            float span = 1.0f / (mLevelValues.length - 1);
            int index = 0;
            int sum = 0;


            for (int i = 1; i <= mLevelValues.length - 1; i++) {
                if (current >= mLevelValues[i]) {
                    ratio += span;
                    index = i;
                    sum = mLevelValues[i];
                }
            }

            if (index < (mLevelValues.length - 1)) {
                int value_span = mLevelValues[index + 1] - mLevelValues[index];
                float diff = current - sum;
                ratio = ratio + (diff / value_span) * span;
            } else {
                ratio = 1.0f;
            }


            return ratio;
        }
    };

    public SectionProgressBar(Context context) {
        this(context, null);
    }

    public SectionProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLevelValues = context.getResources().getIntArray(R.array.SectionLevelValues);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SectionProgressBar, defStyleAttr, 0);


        mBackgroundColor = ta.getColor(R.styleable.SectionProgressBar_section_background, DEFAULT_BACKGROUND_COLOR);
        mForegroundColor = ta.getColor(R.styleable.SectionProgressBar_section_foreground, DEFAULT_FOREGROUND_COLOR);
        mSpaceColor = ta.getColor(R.styleable.SectionProgressBar_section_space_color, DEFAULT_SPACE_COLOR);
        mTextColor = ta.getColor(R.styleable.SectionProgressBar_section_text_color, DEFAULT_TEXT_COLOR);
        mLightTextColor = ta.getColor(R.styleable.SectionProgressBar_section_light_text_color, DEFAULT_LIGHT_TEXT_COLOR);
        mTextSize = ta.getDimension(R.styleable.SectionProgressBar_section_text_size, sp2px(12));
        mLightTextSize = ta.getDimension(R.styleable.SectionProgressBar_section_text_size, sp2px(12));

        mTextBgColor = ta.getColor(R.styleable.SectionProgressBar_section_text_bg_color, DEFAULT_TEXT_BG_COLOR);

        mBarHeight = ta.getDimension(R.styleable.SectionProgressBar_section_bar_height, dp2px(5));
        mCursorDrawable = ta.getDrawable(R.styleable.SectionProgressBar_section_bar_cursor);
        mTextBgCursorDrawable = ta.getDrawable(R.styleable.SectionProgressBar_section_text_bg_cursor);
        ta.recycle();

        initializePaints();
    }

    private void initializePaints() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setColor(mBackgroundColor);

        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setStyle(Paint.Style.FILL);
        mForegroundPaint.setColor(mForegroundColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        mLightTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLightTextPaint.setColor(mLightTextColor);
        mLightTextPaint.setTextAlign(Paint.Align.CENTER);
        mLightTextPaint.setTextSize(mLightTextSize);

        mTextPaintBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaintBgPaint.setColor(mTextBgColor);

        mSectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSectionPaint.setColor(mSpaceColor);
    }

    private float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawForeground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = getPaddingLeft() + dp2px(10); // 两边短2dp
        rectF.top = getHeight() / 2.f - mBarHeight / 2.f;
        rectF.right = getWidth() - getPaddingRight() - dp2px(10);
        rectF.bottom = getHeight() / 2.f + mBarHeight / 2.f;
        canvas.drawRoundRect(rectF, mBarHeight / 2.f, mBarHeight / 2.f, mBackgroundPaint);
    }


    private void drawForeground(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = getPaddingLeft() + dp2px(10);
        rectF.top = getHeight() / 2.f - mBarHeight / 2.f;
        rectF.right = getWidth() * mRatio - getPaddingRight() - dp2px(10); //the dynamic position
        rectF.bottom = getHeight() / 2.f + mBarHeight / 2.f;
        //先转换色值
        canvas.drawRoundRect(rectF, mBarHeight / 2.f, mBarHeight / 2.f, mForegroundPaint);

        //draw arrow cursor
        Rect cursorRect = new Rect();
        if (mRatio >= 0.05f) {
            cursorRect.left = (int) (rectF.right - mCursorDrawable.getIntrinsicWidth() / 2);
            cursorRect.right = cursorRect.left + mCursorDrawable.getIntrinsicWidth();
        } else {
            cursorRect.left = (int) (getPaddingLeft() + dp2px(10) - mCursorDrawable.getIntrinsicWidth() / 2);
            cursorRect.right = cursorRect.left + mCursorDrawable.getIntrinsicWidth();
        }


//        if (mRatio >= 0.02f && mRatio < 0.98f) {
//            cursorRect.left = (int) (rectF.right - mCursorDrawable.getIntrinsicWidth() / 2);
//            cursorRect.right = (int) (rectF.right + mCursorDrawable.getIntrinsicWidth() / 2);
//        } else if (mRatio > 0.0f && mRatio < 0.02f) {
//            cursorRect.left = (int) (rectF.right - mCursorDrawable.getIntrinsicWidth() / 2 + dp2px(2)); //2dp偏移量
//            cursorRect.right = cursorRect.left + mCursorDrawable.getIntrinsicWidth();
//        } else { // > 0.95f
//            cursorRect.left = (int) (rectF.right - mCursorDrawable.getIntrinsicWidth() / 2);
//            cursorRect.right = (int) (rectF.right + mCursorDrawable.getIntrinsicWidth() / 2);
//        }


        cursorRect.bottom = (int) (rectF.top - dp2px(5));
        cursorRect.top = cursorRect.bottom - mCursorDrawable.getIntrinsicHeight();
        mCursorDrawable.setBounds(cursorRect);
        mCursorDrawable.draw(canvas);


        //draw text cursor background
//        RectF boundsRectF = new RectF();
//        boundsRectF.left = rectF.right - dp2px(40);
//        boundsRectF.right = rectF.right + dp2px(40);
//        boundsRectF.top = rectF.right - dp2px(16);
////        boundsRectF.bottom = mLevelY;
//        boundsRectF.bottom = rectF.top - dp2px(5) - mCursorDrawable.getIntrinsicHeight() - dp2px(14);
////        canvas.drawRect(boundsRectF, mBackgroundPaint);
//
//        float rx = dp2px(10);
//        float ry = dp2px(10);
//        canvas.drawRoundRect(boundsRectF, rx, ry, mTextPaintBgPaint);


        Rect cursorTextRect = new Rect();
        float mLevelX = 0.0f;
        float mLevelY = 0.0f;
        if (mRatio > 0.11f && mRatio < 0.88f) {  //保持 > mRatio 时UI效果
            mLevelX = rectF.right;

            cursorTextRect.left = (int) (rectF.right - dp2px(50)); //50表示背景框1/2宽度
            cursorTextRect.right = (int) (cursorTextRect.left + dp2px(100));

        } else if (mRatio >= 0 && mRatio <= 0.11f) { //直接写死

            mLevelX = getPaddingLeft() + dp2px(50);
            cursorTextRect.left = getPaddingLeft();
            cursorTextRect.right = (int) (cursorTextRect.left + dp2px(100));
        } else { // >= 0.88f

            mLevelX = getWidth() - getPaddingRight() - dp2px(50);
            cursorTextRect.left = (int) (getWidth() - getPaddingRight() - dp2px(100));
            cursorTextRect.right = (int) (cursorTextRect.left + dp2px(100));
        }


        mLevelY = rectF.top - dp2px(5) - mCursorDrawable.getIntrinsicHeight() - dp2px(12);
        cursorTextRect.bottom = (int) (mLevelY + dp2px(12)); // 15表示背景框1/2高度
        cursorTextRect.top = (int) (cursorTextRect.bottom - dp2px(30)); // 30表示背景框高度

        //draw text cursor background
        mTextBgCursorDrawable.setBounds(cursorTextRect);
        mTextBgCursorDrawable.draw(canvas);

        //draw cursor text
        canvas.drawText("击败了 " + (int) (mRatio * 100) + "%" + " 销售", mLevelX, mLevelY, mTextPaint);


    }


    /**
     * 设多大比值
     *
     * @param current
     */
    public void setCurrent(int current) {
        if (current < 0) {
            throw new IllegalArgumentException("current value not allowed for negative numbers");
        }
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
            mRatioAnimator.setDuration(mTransitionDuration);
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(DEFAULT_INITIAL_RATIO, mPolicy.computeProgressRatio(current));
        mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRatio = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mRatioAnimator.start();
    }

    public void setTransitionDuration(long duration) {
        mTransitionDuration = duration;
    }

    public void setRatioPolicy(RatioPolicy policy) {
        if (policy == null) throw new IllegalArgumentException("The policy must be not null!");
        mPolicy = policy;
    }

    public void setSectionBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public void setSectionForegroundColor(int mForegroundColor) {
        this.mForegroundColor = mForegroundColor;
    }

    public void setSectionSpaceColor(int mSectionColor) {
        this.mSpaceColor = mSectionColor;
    }

    public void setLevelTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setLightTextColor(int lightTextColor) {
        this.mLightTextColor = lightTextColor;
    }

    public void setLevelTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public void setLightTextSize(float lightTextSize) {
        this.mLightTextSize = lightTextSize;
    }

    public void setBarHeight(float barHeight) {
        this.mBarHeight = barHeight;
    }

    public void setCursorDrawable(Drawable cursorDrawable) {
        this.mCursorDrawable = cursorDrawable;
    }


    public interface RatioPolicy {
        /**
         * Calculated ratio based on current value
         *
         * @param current The value of current state
         * @return
         */
        float computeProgressRatio(int current);
    }
}
