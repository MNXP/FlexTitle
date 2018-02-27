package com.xp.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xp.different.Tool;

/**
 *
 * Created by xiangpan on 2017/8/1.
 */

public class ShadowTextView extends View {
    private static int DEFAULT_COLOR = 0xff000000;
    private static int CHANGED_COLOR = 0xffff0000;
    public static int LEFT = 0;
    public static int RIGHT = 1;
    private int direction;//渐变方向
    private String text = "HELLO";
    private Context context;

    private float textSize;
    private int defaultColor; //默认颜色
    private int changeColor; //渐变颜色
    private float progress; //渐变比例

    private Paint paint;
    private int textWidth; //text的宽高
    private int textHeight;
    private int width; //控件宽高
    private int height;


    public ShadowTextView(Context context) {
        this(context, null);
    }

    public ShadowTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize); //必须在measureText前设置
    }

    /**
     * 测量内容文本的宽高，当改变textSize时应重新调用此方法
     */
    private void measureText() {
        Rect r = new Rect();
        paint.getTextBounds(text, 0, text.length(), r);//一个坑：rect.width会比实际字长度小一点点
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = (int) (-fontMetrics.ascent + fontMetrics.descent);

        textWidth = (int) paint.measureText(text, 0, text.length());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measure(widthMeasureSpec, true);
        height = measure(heightMeasureSpec, false);
        setMeasuredDimension(width, height);
    }

    private int measure(int measureSpec, boolean isWidth) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                Log.d("ccy", isWidth + "exactly");
                break;
            case MeasureSpec.AT_MOST:
                Log.d("ccy", isWidth + "at most");
            case MeasureSpec.UNSPECIFIED:
                Log.d("ccy", isWidth + "UNSPECIFIED");
                if (isWidth) {
                    size = textWidth;
                } else {
                    size = textHeight;
                }
                break;
        }
        return isWidth ? (size + getPaddingLeft() + getPaddingRight()) : (size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawText(canvas, direction, progress);
    }


    private void drawText(Canvas canvas, int direction, float progress) {

        int startX;
        int endX;
        int realWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        int realHeight = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
        int textLeft = getPaddingLeft() + realWidth / 2 - textWidth / 2;   //文本在控件中的起始x位置
        int textRight = getPaddingLeft() + realWidth / 2 + textWidth / 2;   // 文本在控件中的结束x位置
        int textBottom = getPaddingTop() + realHeight / 2 + textHeight / 2;  //文本在控件中的结束y位置
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1) {
            progress = 1;
        }
        int changedWidth = (int) (textWidth * progress);
        if (direction == LEFT) {
            startX = textLeft;
            endX = textLeft + changedWidth;
        } else {
            startX = textRight - changedWidth;
            endX = textRight;
        }

        //画正常的文字内容
        paint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        canvas.save();
        paint.setColor(defaultColor);
        canvas.drawText(text, textLeft, textBottom - fontMetrics.descent, paint);
        canvas.restore();
        //画渐变部分的文字
        canvas.save();
        paint.setColor(changeColor);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawText(text, textLeft, textBottom - fontMetrics.descent, paint);
        canvas.restore();

    }


    //以下settre getter供外部设置属性,别忘记invalidate();

    //ps:若要使用属性动画控制progress,前提得有progress的setter getter
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();

    }

    public float getProgress() {
        return progress;
    }

    public void setTextSize(float size) {
        textSize = size;
        initPaint();
        measureText();
        requestLayout();
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        requestLayout();  //wrap_content情况下文字长度改变需重新measure
        invalidate();
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
        invalidate();
    }

    public int getChangeColor() {
        return changeColor;
    }

    public void setChangeColor(int changeColor) {
        this.changeColor = changeColor;
        invalidate();
    }

    public void setAll(float progress, String text, float textSize, int defaultColor, int changeColor, int direction) {
        this.progress = progress;
        this.text = text;
        this.textSize = Tool.sp2px(context,textSize);
        this.defaultColor = defaultColor;
        this.changeColor = changeColor;
        this.direction = direction == LEFT ? LEFT : RIGHT;
        initPaint();
        measureText();
        requestLayout();
        invalidate();
    }

}