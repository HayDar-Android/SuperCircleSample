package io.haydar.supercircle.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by gjy on 16/7/4.
 */

public class SuperCircleView extends View {

    private final String TAG = "SuperCircleView";

    private int mViewWidth; //view的宽
    private int mViewHeight;    //view的高
    private int mViewCenterX;   //view宽的中心点
    private int mViewCenterY;   //view高的中心点
    private int mMinRadio; //最里面白色圆的半径
    private float mRingWidth; //圆环的宽度
    private int mSelect;    //分成多少段
    private int mSelectAngle;   //每个圆环之间的间隔
    private int mMinCircleColor;    //最里面圆的颜色
    private int mMaxCircleColor;    //最外面圆的颜色
    private int mRingNormalColor;    //默认圆环的颜色
    private Paint mPaint;
    private int color[] = new int[3];   //渐变颜色

    private float mRingAngleWidth;  //每一段的角度

    private RectF mRectF; //圆环的矩形区域
    private int mSelectRing = 0;        //要显示几段彩色

    private boolean isShowSelect = false;   //是否显示断

    public SuperCircleView(Context context) {
        this(context, null);
    }

    public SuperCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperCircleView);
        mMinRadio = a.getInteger(R.styleable.SuperCircleView_min_circle_radio, 400);

        mRingWidth = a.getFloat(R.styleable.SuperCircleView_ring_width, 40);

        mSelect = a.getInteger(R.styleable.SuperCircleView_select, 7);
        mSelectAngle = a.getInteger(R.styleable.SuperCircleView_selec_angle, 3);

        mMinCircleColor = a.getColor(R.styleable.SuperCircleView_circle_color, context.getResources().getColor(R.color.white));
        mMaxCircleColor = a.getColor(R.styleable.SuperCircleView_max_circle_color, context.getResources().getColor(R.color.huise2));
        mRingNormalColor = a.getColor(R.styleable.SuperCircleView_ring_normal_color, context.getResources().getColor(R.color.huise));

        isShowSelect = a.getBoolean(R.styleable.SuperCircleView_is_show_select, false);
        mSelectRing = a.getInt(R.styleable.SuperCircleView_ring_color_select, 0);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
        color[0] = Color.parseColor("#8EE484");
        color[1] = Color.parseColor("#97C0EF");
        color[2] = Color.parseColor("#8EE484");
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;
        mRectF = new RectF(mViewCenterX - mMinRadio - mRingWidth / 2, mViewCenterY - mMinRadio - mRingWidth / 2, mViewCenterX + mMinRadio + mRingWidth / 2, mViewCenterY + mMinRadio + mRingWidth / 2);
        mRingAngleWidth = (360 - mSelect * mSelectAngle) / mSelect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 显示彩色断大于总共的段数是错误的
         */
        if (isShowSelect && mSelectRing > mSelect) {
            return;
        }
        mPaint.setColor(mMaxCircleColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, mMinRadio + mRingWidth + 20, mPaint);
        mPaint.setColor(mMinCircleColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, mMinRadio, mPaint);
        //画默认圆环
        drawNormalRing(canvas);
        //画彩色圆环
        drawColorRing(canvas);


    }

    /**
     * 画彩色圆环
     *
     * @param canvas
     */
    private void drawColorRing(Canvas canvas) {
        Paint ringColorPaint = new Paint(mPaint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeWidth(mRingWidth);
        ringColorPaint.setShader(new SweepGradient(mViewCenterX, mViewCenterX, color, null));

        if (!isShowSelect) {
            canvas.drawArc(mRectF, 270, mSelectRing, false, ringColorPaint);
            return;
        }

        if (mSelect == mSelectRing && mSelectRing != 0 && mSelect != 0) {
            canvas.drawArc(mRectF, 270, 360, false, ringColorPaint);
        } else {
            Log.d(TAG, (mRingAngleWidth * mSelectRing + mSelectAngle + mSelectRing) + "");
            canvas.drawArc(mRectF, 270, mRingAngleWidth * mSelectRing + mSelectAngle * mSelectRing, false, ringColorPaint);
        }

        ringColorPaint.setShader(null);
        ringColorPaint.setColor(mMaxCircleColor);
        for (int i = 0; i < mSelectRing; i++) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + (i) * mSelectAngle), mSelectAngle, false, ringColorPaint);
        }
    }

    /**
     * 画默认圆环
     *
     * @param canvas
     */
    private void drawNormalRing(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mRingWidth);
        ringNormalPaint.setColor(mRingNormalColor);
        canvas.drawArc(mRectF, 270, 360, false, ringNormalPaint);
        if (!isShowSelect) {
            return;
        }
        ringNormalPaint.setColor(mMaxCircleColor);
        for (int i = 0; i < mSelect; i++) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + (i) * mSelectAngle), mSelectAngle, false, ringNormalPaint);
        }
    }

    /**
     * 显示几段
     *
     * @param i
     */
    public void setSelect(int i) {
        this.mSelectRing = i;
        this.invalidate();
    }

    /**
     * 断的总数
     *
     * @param i
     */
    public void setSelectCount(int i) {
        this.mSelect = i;
    }


    /**
     * 是否显示断
     *
     * @param b
     */
    public void setShowSelect(boolean b) {
        this.isShowSelect = b;
    }


    public void setColor(int[] color) {
        this.color = color;
    }
}
