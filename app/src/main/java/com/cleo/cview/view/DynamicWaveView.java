package com.cleo.cview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;


public class DynamicWaveView extends View {

	/** 波纹颜色 */
	private static final int WAVE_PAINT_COLOR = Color.argb(255, 52, 156, 255);
	private static final int WAVE_COLOR = Color.argb(255, 42, 129, 209);
	/** y = Asin(wx+b)+h */
	private static final float STRETCH_FACTOR_A = 4;
	private static final int OFFSET_Y = 0;
	/** 第一条水波移动速度 */
	private static final int TRANSLATE_X_SPEED_ONE = 2;
	/** 第二条水波移动速度 */
	private static final int TRANSLATE_X_SPEED_TWO = 2;
	private float mCycleFactorW;
	/** 水平线 */
	private int levelNumber = 0;

	private int mTotalWidth, mTotalHeight;
	private float[] mYOnePositions;
	private float[] mYTwoPositions;
	private float[] mResetOneYPositions;
	private float[] mResetTwoYPositions;
	private int mXOffsetSpeedOne;
	private int mXOffsetSpeedTwo;
	private int stretchFactorA;
	// 当前移动到的点 */
	private int mXOneOffset;
	private int mXTwoOffset;

	private Paint mWavePaint;
	private Paint mWavePaintN;
	private PaintFlagsDrawFilter mDrawFilter;

	public DynamicWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 将dp转化为px，用于控制不同分辨率上移动速度基本一致
		mXOffsetSpeedOne = dip2px(context, TRANSLATE_X_SPEED_ONE);
		mXOffsetSpeedTwo = dip2px(context, TRANSLATE_X_SPEED_TWO);
		stretchFactorA = dip2px(context, (int)STRETCH_FACTOR_A);
//		Paint.ANTI_ALIAS_FLAG, Paint.DITHER_FLAG
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		// 初始绘制波纹的画笔
		mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWavePaintN = new Paint(Paint.ANTI_ALIAS_FLAG);
		// 去除画笔锯齿
		mWavePaint.setAntiAlias(true);
		mWavePaintN.setAntiAlias(true);
		// 设置风格为实线
		mWavePaint.setStyle(Style.FILL);
		mWavePaintN.setStyle(Style.FILL);
		// 设置画笔颜色
		mWavePaint.setColor(WAVE_COLOR);
		mWavePaintN.setColor(WAVE_PAINT_COLOR);

//        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        // 防抖动
//        mBitmapPaint.setDither(true);
//        mBitmapPaint.setAntiAlias(true);
//        // 开启图像过滤
//        mBitmapPaint.setFilterBitmap(true);
        
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        // 从canvas层面去除锯齿
        canvas.setDrawFilter(mDrawFilter);
		resetPositonY();
		for (int i = 0; i < mTotalWidth; i++) {
			// 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
			// 绘制第一条水波纹
			canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] 
					- getLevelNumber(), i, mTotalHeight, mWavePaint);

			// 绘制第二条水波纹
			canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] 
					- getLevelNumber(), i, mTotalHeight, mWavePaintN);
		}

		// 改变两条波纹的移动点
		mXOneOffset += mXOffsetSpeedOne;
		mXTwoOffset += mXOffsetSpeedTwo;

		// 如果已经移动到结尾处，则重头记录
		if (mXOneOffset >= mTotalWidth) {
			mXOneOffset = 0;
		}
		if (mXTwoOffset >= mTotalWidth) {
			mXTwoOffset = 0;
		}
		postInvalidate();
	}

	public float getLevelNumber() {
		return levelNumber;
	}

	/**
	 * @param levelNumbers
	 * 水平线
	 */
	public void setLevelNumber(double levelNumbers) {
		levelNumber = (int)((double)(levelNumbers * (double)mTotalHeight / 1000));
//		Log.i("TAG", "levelNumber-->"+levelNumber);
//		levelNumber = (int)((double)(levelNumbers * (double)mTotalHeight / 100));
		this.invalidate();
	}

	private void resetPositonY() {
		// mXOneOffset代表当前第一条水波纹要移动的距离
		int yOneInterval = mYOnePositions.length - mXOneOffset;
		// 使用System.arraycopy方式重新填充第一条波纹的数据 1.原数组，2.起始位置，3.目标数组，4.目标位置，5.复制的长度。
		System.arraycopy(mYOnePositions, mXOneOffset, mResetOneYPositions, 0,
				yOneInterval);
		System.arraycopy(mYOnePositions, 0, mResetOneYPositions, yOneInterval,
				mXOneOffset);

		int yTwoInterval = mYTwoPositions.length - mXTwoOffset;
		System.arraycopy(mYTwoPositions, mXTwoOffset, mResetTwoYPositions, 0,
				yTwoInterval);
		System.arraycopy(mYTwoPositions, 0, mResetTwoYPositions, yTwoInterval,
				mXTwoOffset);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 记录下view的宽高
		mTotalWidth = w;
		mTotalHeight = h;
		// 用于保存原始波纹的y值
		mYOnePositions = new float[mTotalWidth];
		mYTwoPositions = new float[mTotalWidth];
		// 用于保存波纹一的y值
		mResetOneYPositions = new float[mTotalWidth];
		// 用于保存波纹二的y值
		mResetTwoYPositions = new float[mTotalWidth];

		// 将周期定为view总宽度
		mCycleFactorW = (float) (2 * Math.PI / mTotalWidth );

		// 根据view总宽度得出所有对应的y值
		for (int i = 0; i < mTotalWidth; i++) {
			mYOnePositions[i] = (float) (stretchFactorA
					* Math.sin(mCycleFactorW * i) + OFFSET_Y);
			mYTwoPositions[i] = (float) (stretchFactorA
					* Math.cos(mCycleFactorW * i) + OFFSET_Y);
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