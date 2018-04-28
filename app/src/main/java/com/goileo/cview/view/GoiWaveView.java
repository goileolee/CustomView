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
    private Paint circlePaint;
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
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLUE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        circleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) /2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for(int i = 0; i < mTotalHeight; i ++){
            float positionY = (float) (numberA * Math.sin(FACTOR_W *i) + OFFSET_Y);
            canvas.drawLine(i, mTotalHeight - positionY - 100, i, mTotalHeight, circlePaint);
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        FACTOR_W = (float) (2 * Math.PI / mTotalWidth);
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
