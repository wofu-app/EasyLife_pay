package com.landicorp.android.wofupay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.DefaultThreadPoll;

/**
 * Created by Administrator on 2017/3/28.
 */

public class WaveBgProgress extends View {

    private Paint bitmapPaint;// 绘制图像的画笔
    private Paint textPaint;// 绘制进度的画笔
    private Path mPath;
    private Bitmap bgBitmap, frontBitmap; //

    private int textClocr;// 进度文字颜色
    private int waveColor;// 水波纹颜色
    private float ratio;
    private int max, progress;

    private float mWidth, mHeight;// 控件宽高

    private float cellHeight, cellWidth, startX;
    private int cellHeightCount = 20;
    private int cellWidthCount = 4;
    private int bgBitmapID;// 背景图片id
    private int frontBitmapID;// 前方图片id
    private int textSize;
    private boolean isLoop, ismove;

    public WaveBgProgress(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public WaveBgProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public WaveBgProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.customProgress);
        waveColor = typedArray.getColor(
                R.styleable.customProgress_custom_color, Color.alpha(0xabcdef));
        textClocr = typedArray.getColor(
                R.styleable.customProgress_custom_textcolor, Color.RED);
        bgBitmapID = typedArray.getResourceId(
                R.styleable.customProgress_custom_bg, R.mipmap.logowf);
        frontBitmapID = typedArray.getResourceId(
                R.styleable.customProgress_custom_front, -1);
        textSize = (int) typedArray.getDimension(
                R.styleable.customProgress_custom_textsize,
                AppUtils.dip2px(context, 15));
        max = typedArray.getInt(R.styleable.customProgress_custom_max, 100);
        progress = typedArray.getInt(R.styleable.customProgress_custom_max, 0);
        isLoop = typedArray.getBoolean(R.styleable.customProgress_custom_loop,
                false);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setTextSize(textSize);
        bitmapPaint.setStrokeWidth(5);
        bitmapPaint.setColor(textClocr);

        textPaint = new Paint();
        textPaint.setStrokeWidth(5);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textClocr);

        mPath = new Path();
        textPaint.setAntiAlias(true);
        bitmapPaint.setColor(waveColor);

        BitmapFactory.Options op = new BitmapFactory.Options();

        op.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), bgBitmapID, op);
        mWidth = op.outWidth;
        mHeight = op.outHeight;
        ismove = isLoop = true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            int exceptHeight = (int) ((getPaddingBottom() + getPaddingTop()) + mHeight);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight,
                    MeasureSpec.EXACTLY);

        }

        if (widthMode != MeasureSpec.EXACTLY) {
            int exceptWidth = (int) ((getPaddingLeft() + getPaddingRight()) + mWidth);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth,
                    MeasureSpec.EXACTLY);

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        cellHeight = mWidth / cellHeightCount;
        cellWidth = mWidth / cellWidthCount;

        bgBitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                bgBitmapID, (int) mWidth, (int) mHeight);
        frontBitmap = frontBitmapID > 0 ? BitmapUtils
                .decodeSampledBitmapFromResource(getResources(), frontBitmapID,
                        (int) mWidth, (int) mHeight) : null;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制最底层的背景
        canvas.drawBitmap(bgBitmap, 0, 0, bitmapPaint);
        show = AppUtils.get2Double(progress * 100.0 / max) + "%";
        // bitmapPaint.setColor(Color.RED);
        // canvas.drawText(show, mWidth / 2, mHeight / 2, bitmapPaint);

        // 限定进度绘制区域
        mPath.reset();
        ratio = (float) (progress * 1.0 / max);
        float currentX = startX + mWidth;
        float currentY = mHeight * (1 - ratio) - cellHeight;
        mPath.moveTo(currentX, mHeight);
        mPath.lineTo(currentX, currentY);
        int count = 0;
        for (int i = 0; i < cellWidthCount; i++) {
            count = 4 * i;
            mPath.quadTo(currentX - (1 * cellWidth) - count * cellWidth,
                    currentY - cellHeight, currentX - (2 * cellWidth) - count
                            * cellWidth, currentY);
            mPath.quadTo(currentX - (3 * cellWidth) - count * cellWidth,
                    currentY + cellHeight, currentX - (4 * cellWidth) - count
                            * cellWidth, currentY);
        }
        mPath.lineTo(-currentX, mHeight);
        mPath.lineTo(currentX, mHeight);
        canvas.clipPath(mPath);

        // 如果没有设置前面的图片,就直接使用颜色填充
        if (frontBitmap == null) {
            bitmapPaint.setColor(waveColor);
            canvas.drawBitmap(creatBitmap(waveColor), 0, 0, bitmapPaint);

            // 绘制前面的图片
        } else {
            canvas.drawBitmap(frontBitmap, 0, 0, bitmapPaint);
        }

    }

    String show = "";
    private Thread thread;

    private Bitmap creatBitmap(int color) {

        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bgBitmap, 0, 0, bitmapPaint);

        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        // canvas.drawText(Utils.get2Double(ratio*100)+"%",);
        canvas.setBitmap(null);
        return newBitmap;

    }

    private void move() {
        DefaultThreadPoll.getThreadPool().addTask(new Runnable() {

            @Override
            public void run() {
                int count = 0;
                while (ismove) {
                    SystemClock.sleep(30);
                    startX += AppUtils.dp2px(2, getContext());
                    if (startX > mWidth) {
                        startX = 0;
                    }

                    if (isLoop) {
                        count++;
                        if (count > 2) {
                            count = 0;
                            progress++;
                            if (progress >= max) {
                                progress = 0;
                            }


                        }
                    }
                    postInvalidate();
                }
            }
        });


    }

    /**
     * 设置最大进度
     *
     * @param max 最大进度
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 设置当前进度
     *
     * @param progress 当前进度
     */
    public void setProgress(int progress) {
        if (isLoop) {
            progress = this.progress;
        }
        this.progress = progress;
        postInvalidate();
    }

    /**
     * 获取最大进度
     *
     * @return 最大进度
     */
    public int getMax() {
        return max;
    }

    /**
     * 获取当前进度
     *
     * @return 当前进度
     */
    public int getProgress() {
        return progress;
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }

    @Override
    protected void onAttachedToWindow() {
        ismove = true;
        move();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        ismove = false;
        super.onDetachedFromWindow();
    }

}