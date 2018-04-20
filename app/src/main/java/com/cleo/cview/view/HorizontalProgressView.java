package com.cleo.cview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.cleo.cview.R;

/**
 * Created by LY on 2017/3/13.
 */

public class HorizontalProgressView extends ProgressBar {

    private Bitmap pgsbBitmap;
    private Bitmap pgsbBitmapBg;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public HorizontalProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        pgsbBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_home_prgb_p);
        obtainStyleAttributes(attrs);

    }

    /**
     * 获取属性值
     * @param attrs
     */
    private void obtainStyleAttributes(AttributeSet attrs){
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HorizontalProgressView);
        Drawable backgroundDrawable = a.getDrawable(R.styleable.HorizontalProgressView_progress_bg);
        if(backgroundDrawable != null){
            setProgressBackground(backgroundDrawable);
        }

    }

    public void setProgressBackground(Drawable drawable){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0xFFABCDEF);
        canvas.drawBitmap(pgsbBitmapBg, 0, 0, mPaint);
        // 图片的遮罩，目前发现只能从坐标0,0开始截取
        Bitmap mask = Bitmap.createBitmap(pgsbBitmap.getWidth(),
                getMeasuredHeight(), pgsbBitmap.getConfig());
        Canvas cc = new Canvas(mask);
        // 一个与进度相关的矩形，改变进度即改变这里矩形的宽度
        RectF mRectF = new RectF(0, 0, 200, mask.getHeight());
        mPaint.setColor(Color.BLUE);
        // 在遮罩上画一个矩形
        cc.drawRect(mRectF, mPaint);
        /*
        * 离屏缓存
        * Layer层的宽和高要设定好，不然会出现有些部位不再层里面，你的操作是不对这些部位起作用的
        */
        int sc = canvas.saveLayer(0, 0, pgsbBitmap.getWidth(),
                pgsbBitmap.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        // 先绘制dst目标图
        canvas.drawBitmap(pgsbBitmap, 0, 0, mPaint);
        // 设置混合模式 （只在源图像和目标图像相交的地方绘制目标图像）
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // 再绘制src源图
        canvas.drawBitmap(mask, 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        // 还原画布
        canvas.restoreToCount(sc);


//        // 画背景
//        canvas.drawBitmap(pgsbBitmapBg, 0, 0, mPaint);
//        // 复制一个bitmap，此处还可targetBitmap = pgsbBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap targetBitmap = Bitmap.createBitmap(pgsbBitmap.getWidth(),
//                pgsbBitmap.getHeight(), pgsbBitmap.getConfig());
//        // 用bitmap创建一个画布
//        Canvas mCanvas = new Canvas(targetBitmap);
//        // 在画布上画进度
//        mCanvas.drawBitmap(pgsbBitmap, 0, 0, mPaint);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        // 设置重合模式
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        paint.setColor(Color.TRANSPARENT);
//        //画裁剪区域
//        mCanvas.drawRect(new RectF(680, 0, targetBitmap.getWidth(), pgsbBitmap.getHeight()), paint);
//        //源区域，即mCanvas画布上的内容区域
//        Rect src = new Rect(0, 0, targetBitmap.getWidth(), targetBitmap.getHeight());
//        // 目标区域，即canvas画布上的展示区域
//        Rect dst = new Rect(0, 0, pgsbBitmapBg.getWidth(), pgsbBitmapBg.getHeight());
//        // 将源区域上的内容放到目标区域
//        canvas.drawBitmap(targetBitmap, src, dst, mPaint);

    }

}

