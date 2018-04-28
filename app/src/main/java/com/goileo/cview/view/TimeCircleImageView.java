package com.goileo.cview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class TimeCircleImageView extends ImageView {

	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	/** 边框的默认宽度 */
	private static final int DEFAULT_BORDER_WIDTH = 0;

	/** 图片的矩形对象 */
	private final RectF mDrawableRect = new RectF();
	/** 边框的矩形对象 */
	private final RectF mTimeBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mTimeBorderPaint = new Paint();
	/** 进度条最大值 */
	private static final int PROGRESS_MAX = 100;
	/** 边框的宽度 */
	private int mBorderWidth = 10;
	private int mBorderProgress = DEFAULT_BORDER_WIDTH;

	/** 图片的Bitmap对象，可获取到图片信息，如长宽px值 */
	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	/** 图片的半径 */
	private float mDrawableRadius;

	private boolean mReady;
	private boolean mSetupPending;

	public TimeCircleImageView(Context context) {
		super(context);
	}

	public TimeCircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeCircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(SCALE_TYPE);
		initPaint();
		mReady = true;
		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}
	
	private void initPaint(){
		mTimeBorderPaint.setStyle(Paint.Style.STROKE);
		mTimeBorderPaint.setAntiAlias(true);
		mTimeBorderPaint.setColor(Color.BLACK);
//		mTimeBorderPaint.setColor(Color.rgb(mRed, mGreen, mBlue));
		mTimeBorderPaint.setStrokeWidth(mBorderWidth);
	}
	
	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException( 
					String.format("ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
//		mTimeBorderPaint.setColor(Color.rgb(mRed, mGreen, mBlue));
		mTimeBorderPaint.setColor(Color.YELLOW);
		//度数以平面X轴为起点，按顺时针计算。
		canvas.drawArc(mTimeBorderRect, 270, 360 * mBorderProgress / 100 - 360, 
				false, mTimeBorderPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	public int getProgress() {
		return mBorderProgress;
	}

	/**
	 * 设置进度条和颜色的渐变，这里实现由绿色到红色
	 * @param progress
	 * 当前进度
	 */
	public void setProgress(int progress) {
		if (progress < 0) {
			return;
		}
		if(progress > PROGRESS_MAX){
			progress = PROGRESS_MAX;
		}
		if(progress <= PROGRESS_MAX){
			if(progress == PROGRESS_MAX){
//				mRed = 0;
//				mGreen = 255;
			}
			mBorderProgress = progress;
//			if(mRed < COLOR_MAX - SPEED_COLOR_NUMBER){
//				mRed += SPEED_COLOR_NUMBER;
//			}else {
//				mRed = COLOR_MAX;
//				if(mGreen > SPEED_COLOR_NUMBER){
//					mGreen -= SPEED_COLOR_NUMBER;
//				}else {
//					mGreen = 0;
//				}
//			}
			
			postInvalidate();
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();

	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, 
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), 
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, 
				Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();
		
		mTimeBorderRect.set(mBorderWidth, mBorderWidth,
				getWidth() - mBorderWidth,
				getHeight() - mBorderWidth);
		
		mDrawableRect.set(mBorderWidth, mBorderWidth, 
				getWidth() - mBorderWidth, 
				getHeight() - mBorderWidth);
		mDrawableRadius = Math.min(mDrawableRect.height() / 2, 
				mDrawableRect.width() / 2);

		updateShaderMatrix();
		invalidate();
	}

	/**
	 * 适配图形与外圆框，让图形居中
	 */
	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;
		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}
		//缩放图片到外框大小
		mShaderMatrix.setScale(scale, scale);
		//移动图片到外框中间位置（细微调整，可无）
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, 
				(int) (dy + 0.5f) + mBorderWidth);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

}
