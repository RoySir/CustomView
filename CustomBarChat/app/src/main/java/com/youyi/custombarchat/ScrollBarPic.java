package com.youyi.custombarchat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


/**
 * 柱形图控件
 * Created by Roy on 16/10/14.
 */
public class ScrollBarPic extends View {

    private int width;// 控件的宽度
    private int height;// 控件的高度
    private int leftPadding;// 柱子的左右边距
    private int barPadding;// 柱子之间的间距

    /**
     * 柱子底部距离控件底部高度
     */
    private int barbottom;
    /**
     * 柱子顶部距离控件底部高度
     */
    private int bartop;
    /**
     * 每个柱子的宽度
     */
    private int perBarWidth;
    /**
     * 柱状图画笔
     */
    private Paint barPaint;
    /**
     * 基线
     */
    private Paint linePaint;
    /**
     * 虚基线
     */
    private Paint dashLinePaint;

    /**
     * x轴坐标画笔
     */
    private TextPaint textPaint;
    /**
     * X轴刻度
     */
    private Paint lineUnitPaint;


    private Paint.FontMetrics metrics1;
    private int baseline1;

    /**
     * 是否有选中的柱子
     */
    private boolean flag;
    private OnClickListener listener;
    private ScrollBarBean scrollBarBean;
    /**
     * 在ScrollBarPic中坐标轴（x, y）
     */
    float horilineX, horiLineY;

    /**
     * 刻度下面的游标
     */
    private Drawable mCursorDrawable;

    public ScrollBarPic(Context context) {
        this(context, null);
    }

    public ScrollBarPic(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBarPic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollBarPic, defStyle, 0);
        mCursorDrawable = ta.getDrawable(R.styleable.ScrollBarPic_scroll_bar_cursor);
        ta.recycle();
        init();
    }

    private void init() {

        //?
        bartop = (int) getResources().getDimension(R.dimen.px132);

        //柱子的左右边距 50-20 = 30
        leftPadding = (int) getResources().getDimension(R.dimen.px30);
        //柱子之间间距
//        barPadding = getResources().getDimensionPixelOffset(R.dimen.px60);
        perBarWidth = getResources().getDimensionPixelOffset(R.dimen.px16);
        barbottom = (int) getResources().getDimension(R.dimen.px64);
        height = (int) getResources().getDimension(R.dimen.px264);
        // 先赋值horiline (0, height - barbottom) 坐标
        horilineX = 0 + leftPadding / 2;
        horiLineY = height - barbottom;

        //基线
        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.white_tran16_bg));
        linePaint.setStrokeWidth(1);


        dashLinePaint = new Paint();
        dashLinePaint.setStyle(Paint.Style.STROKE);
        dashLinePaint.setStrokeWidth(2);
        dashLinePaint.setColor(getResources().getColor(R.color.white_tran16_bg));
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashLinePaint.setPathEffect(effects);


        //文字下标
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setTextSize(getResources().getDimension(R.dimen.px20));
        textPaint.setColor(getResources().getColor(R.color.white_tran40_text));
        textPaint.setTextAlign(Paint.Align.CENTER);
        metrics1 = textPaint.getFontMetrics();
        baseline1 = (int) ((-metrics1.ascent - metrics1.descent) / 2);

        //刻度
        lineUnitPaint = new Paint();
        lineUnitPaint.setColor(getResources().getColor(R.color.white));
        lineUnitPaint.setStrokeWidth(1);


        //柱状图画笔
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        barPaint.setColor(getResources().getColor(R.color.white_tran30_bg));
        barPaint.setStrokeWidth(perBarWidth);

        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;

        //计算柱子的宽度
//        perBarWidth = (w_screen - 2 * leftPadding - 7 * barPadding) / 7;
//        int width = (perBarWidth + barPadding) * 7;// this的宽度
        barPadding = (w_screen - 2 * leftPadding - 7 * perBarWidth) / 7;
        int width = (perBarWidth + barPadding) * 7;// this的宽度

//        perBarWidth = getResources().getDimensionPixelOffset(R.dimen.px16);
//        int width = (perBarWidth + barPadding) * 7 + 2 * leftPadding;

        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                heightMeasureSpec);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        barPaint.setStrokeWidth(perBarWidth);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (scrollBarBean == null)
            return;


        //画基线
        for (int i = 0; i < 3; i++) {

            Path path = new Path();
            path.moveTo(0 + leftPadding / 2, (0 + getResources().getDimension(R.dimen.px100) * i));
            path.lineTo(width, (0 + getResources().getDimension(R.dimen.px100) * i));
            canvas.drawPath(path, dashLinePaint);
//            canvas.drawLine(horilineX, horiLineY - getResources().getDimension(R.dimen.px100) * i, horilineX + width, horiLineY - getResources().getDimension(R.dimen.px100) * i, linePaint);
        }

        ScrollPerBarBean perBarBean = null;
        for (int i = 0; i < scrollBarBean.getLists().size(); i++) {
            perBarBean = scrollBarBean.getLists().get(i);

            if (perBarBean.isFlag())// 选中状态
            {
                barPaint.setColor(getResources().getColor(R.color.white_tran80_bg));
                textPaint.setColor(getResources().getColor(R.color.white_tran88_text));


                //draw cursor
                Rect cursorRect = new Rect();
                cursorRect.left = (int) (horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i - mCursorDrawable.getIntrinsicWidth() / 2);
                cursorRect.right = (int) (horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i + perBarWidth + mCursorDrawable.getIntrinsicWidth() / 2);
                cursorRect.bottom = height;
                cursorRect.top = cursorRect.bottom - mCursorDrawable.getIntrinsicHeight();
                mCursorDrawable.setBounds(cursorRect);
                mCursorDrawable.draw(canvas);


            } else {
                barPaint.setColor(getResources().getColor(R.color.white_tran30_bg));
                textPaint.setColor(getResources().getColor(R.color.white_tran40_text));
            }


            //绘制X轴坐标
            canvas.drawText(perBarBean.getxIndexStr(), horilineX + getResources().getDimension(R.dimen.px30) + perBarWidth / 2 + (barPadding + perBarWidth) * i, horiLineY + getResources().getDimension(R.dimen.px32) + baseline1, textPaint);

            //绘制X轴刻度
            canvas.drawLine(horilineX + getResources().getDimension(R.dimen.px30) + perBarWidth / 2 + (barPadding + perBarWidth) * i, horiLineY, horilineX + getResources().getDimension(R.dimen.px30) + perBarWidth / 2 + (barPadding + perBarWidth) * i, horiLineY + getResources().getDimension(R.dimen.px2), lineUnitPaint);

            // 绘制柱状图
            RectF rectF = new RectF();
            rectF.left = horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i;
            if (perBarBean.getActualHeight() >= horiLineY) {
                rectF.top = 0.0f;
            } else {
                rectF.top = horiLineY - perBarBean.getActualHeight();
            }

            rectF.right = horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i + perBarWidth;
            rectF.bottom = horiLineY;
            canvas.drawRoundRect(rectF, getResources().getDimension(R.dimen.px8), getResources().getDimension(R.dimen.px8), barPaint);


            // 设置左边界
            perBarBean.setLeftX((int) (horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i));
            // 设置右边界
            perBarBean.setRightX((int) (horilineX + getResources().getDimension(R.dimen.px30) + (barPadding + perBarWidth) * i + perBarWidth));
            // 设置上边界
            perBarBean.setTopY((int) (horiLineY - perBarBean.getActualHeight()));


        }
        super.onDraw(canvas);
    }


    /**
     * 得到柱子之间的宽度（父容器使用）
     *
     * @return
     */
    public int getPadding() {
        return barPadding;
    }

    /**
     * 得到每个柱子的宽度（父容器使用）
     *
     * @return
     */
    public int getPerbarWidth() {
        return perBarWidth;
    }

    /**
     * 填充数据
     *
     * @param scrollBarBean
     */
    public void setDatas(ScrollBarBean scrollBarBean) {
        this.scrollBarBean = scrollBarBean;
        ScrollPerBarBean perBarBean = null;
        for (int i = 0; i < scrollBarBean.getLists().size(); i++) {
            //转换成柱形图要画的高度
            perBarBean = scrollBarBean.getLists().get(i);
            int actualHeight = (int) ((perBarBean.getRatio() / 100) * (getResources().getDimensionPixelOffset(R.dimen.px200)));
            perBarBean.setActualHeight(actualHeight);

            if (perBarBean.isFlag()) {
                flag = true;
            }
        }


/*        if (!flag) {
            perBarBean.setFlag(true);
        }*/
        init();
    }


    int x = 0;
    int y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onclick(int i);
    }

    /**
     * 点击事件回调
     *
     * @param flag
     */
    public void clickcallback(boolean flag) {
        if (!flag) {
            ScrollPerBarBean perBarBean = null;
            for (int i = 0; i < scrollBarBean.getLists().size(); i++) {
                perBarBean = scrollBarBean.getLists().get(i);
                if (perBarBean.getLeftX() - barPadding/3 < x && x < perBarBean.getRightX() + barPadding/3
                        && y > perBarBean.getTopY() && y < height) {
                    //滑动下面的游标
                    //点击后改变状态并刷新UI
                    perBarBean.setFlag(true);

                    for (int j = 0; j < scrollBarBean.getLists().size(); j++) {
                        if (i != j) {
                            scrollBarBean.getLists().get(j).setFlag(false);
                        }
                    }
                    if (listener != null) {
                        listener.onclick(i);
                    }
                    invalidate();
                }
            }
        }
    }

}
