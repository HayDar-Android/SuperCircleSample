# SuperCircleSample
Android自定义view实现可配置圆环

##效果图
![](https://raw.githubusercontent.com/HayDar-Android/SuperCircleSample/master/a.png "SuperCircleSample")
![](https://raw.githubusercontent.com/HayDar-Android/SuperCircleSample/master/a.gif "SuperCircleSample")
##Getting Help

###实现思路步骤
1. 画一个实心的白色圆
2. 画一个默认的灰色的圆环
3. 画一个带颜色的白色圆环


这些是要用到的一些变量:

```	java
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
```

在onLayout中取得中心点，和圆环区域(RectF)

``` java
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
```
这里mRectF区域的left是中心点减去最里面实心圆的半价然后在减去圆环的宽度，right则相加;right和top一样的道理；每个端的角度就是（360°-断的数量*段与段之间的间隔）/端的数量
####现在开始画了
	画最里面的实心圆和最外面的灰色圆

```java
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
```


画默认的圆环
 首先设置画笔只填充边距，画笔的宽度和颜色
```	java
Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mRingWidth);
        ringNormalPaint.setColor(mRingNormalColor);
```
	
然后画一个360的圆环

```java
 canvas.drawArc(mRectF, 270, 360, false, ringNormalPaint);
```


最后判断是否要显示段，如果显示就在刚才圆环的基础上画7个小的间隔

``` java
 if (!isShowSelect) {
            return;
        }
        ringNormalPaint.setColor(mMaxCircleColor);
        for (int i = 0; i < mSelect; i++) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + (i) * mSelectAngle), mSelectAngle, false, ringNormalPaint);
        }
```
这边之所以是从270°开始是因为默认的0°在我们常见的90°的地方

画彩色的圆环和默认的圆环的原理是一样的




