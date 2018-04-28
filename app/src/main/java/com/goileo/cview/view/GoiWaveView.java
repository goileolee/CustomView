package com.goileo.cview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LY on 2018/4/26.
 */

public class GoiWaveView extends View {

    private Context context;
    private float circleRadius;
    private Paint sinPaint;
    private Paint cosPaint;
    private int mTotalHeight, mTotalWidth;

    private int numberA = 10; // 峰值
    private float FACTOR_W; // 周期
    private int OFFSET_Y = 0; // Y轴偏移量

    public GoiWaveView(Context context) {
        super(context);
    }

    public GoiWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoiWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init(){
        sinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sinPaint.setAntiAlias(true);
        sinPaint.setStyle(Paint.Style.FILL);
        sinPaint.setColor(Color.BLUE);
        cosPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cosPaint.setAntiAlias(true);
        cosPaint.setStyle(Paint.Style.FILL);
        cosPaint.setColor(Color.RED);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        circleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) /2;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w; // 控件的宽度
        mTotalHeight = h; // 控件的高度
        FACTOR_W = (float) (2 * Math.PI / mTotalWidth); // 定义波形的周期为控件的宽度，即 ω：周期
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < mTotalHeight; i ++){
            // 计算出横坐标 i 对应的 纵坐标值 positionY
            float sinPositionY = (float) (numberA * Math.sin(FACTOR_W *i) + OFFSET_Y);
            float cosPositionY = (float) (numberA * Math.cos(FACTOR_W *i) + OFFSET_Y);
            // 画竖线，从起始位置(0, mTotalHeight - 0 - 100)画到终点坐标(0, mTotalHeight)
            canvas.drawLine(i, mTotalHeight - sinPositionY - 100, i, mTotalHeight, sinPaint);
            canvas.drawLine(i, mTotalHeight - cosPositionY - 100, i, mTotalHeight, cosPaint);
        }

    }

    /**
     * 描述：dip转换为px
     * @param context
     * @param dipValue
     * @return
     * @throws
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
