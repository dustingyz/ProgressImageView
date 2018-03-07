package me.limbre.glide4progressimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import me.limbre.glide4progressimageview.R;

/**
 * Created by Limbre on 2018/3/5.
 */

public class ProgressImageView extends android.support.v7.widget.AppCompatImageView {

    private boolean mShowProgress = false;
    private int mProgress;
    private int mLoadingColor = -1;
    private int mBackDrawable = -1;
    private RectF mRectF;
    private Paint mPaint;
    private Bitmap mBitmap;
    private float mCircleSize = 47;
    private float mCircleWidth = 6;

    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressImageView);
        mCircleSize = mTypedArray.getDimension(R.styleable.ProgressImageView_circle_size, 47f);
        mCircleWidth = mTypedArray.getDimension(R.styleable.ProgressImageView_circle_width, 6f);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mShowProgress) {
            if (mLoadingColor != -1) {
                mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
                mPaint.setColor(mLoadingColor);
                canvas.drawRect(mRectF, mPaint);
            } else if (mBackDrawable != -1){
                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();

                float rOut = getMeasuredWidth() * 1f / getMeasuredHeight();
                float rInner = width * 1f / height;

                if (rOut > rInner) {
                    float ratio = getMeasuredHeight() * 1f / height;
                    width = (int) (width * ratio);
                    int left = (getMeasuredWidth() - width) / 2;
                    int right = (getMeasuredWidth() - width) / 2 + width;
                    mRectF.set(left, 0, right, getMeasuredHeight());
                } else {
                    float ratio = getMeasuredWidth() * 1f / width;
                    height = (int) (height * ratio);
                    int top = (getMeasuredHeight() - height) / 2;
                    int bottom = (getMeasuredHeight() - height) / 2 + height;
                    mRectF.set(0, top, getMeasuredWidth(), bottom);
                }

                canvas.drawBitmap(mBitmap, null,mRectF, mPaint);
            }
            mPaint.setColor(0x22888888);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mCircleSize, mPaint);
            mPaint.setColor(0xFFFFFFFF);
            mRectF.set(getMeasuredWidth() / 2 - mCircleSize,
                    getMeasuredHeight() / 2 - mCircleSize,
                    getMeasuredWidth() / 2 + mCircleSize,
                    getMeasuredHeight() / 2 + mCircleSize);

            float sweepAngle = 360f * mProgress / 100;
            canvas.drawArc(mRectF,-90, sweepAngle, false, mPaint);
            mRectF.set(0,0,0,0);
        }
    }

    public void showProgress(int progress) {
        if (!isProgressShowing()) {
            mShowProgress = true;
        }
        mProgress = progress;
        postInvalidate();
    }

    public void hideProgress() {
        if (!isProgressShowing()) {
            return;
        }
        mShowProgress = false;
        postInvalidate();
    }

    public boolean isProgressShowing() {
        return mShowProgress;
    }

    public void setLoadingBackgroundColor(int color) {
        mLoadingColor = color;
    }

    public void setLoadingDrawable(int drawable) {
        mBackDrawable = drawable;
        if (drawable == -1) {
            mBitmap = null;
            return;
        }
        mBitmap = BitmapFactory.decodeResource(getResources(), mBackDrawable);
    }

    public void setCircleSize(float size) {
        mCircleSize = size;
    }

    public void setCircleWidth(float width) {
        mCircleWidth = width;
    }

}
