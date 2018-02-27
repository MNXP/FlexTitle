package com.xp.different;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import static com.xp.different.Tool.getScreenWidth;

/**
 *
 * Created by xiangpan on 2017/8/1.
 */

public class DynamicLine extends View {
    private int lineHeight;
    private int lineBottomMargins;
    private int shaderColorEnd;
    private int shaderColorStart;
    private float startX, stopX;
    private Paint paint;
    private RectF rectF = new RectF(startX, 0, stopX, 0);


    public DynamicLine(Context context, int shaderColorStart, int shaderColorEnd, int lineHeight,int lineBottomMargins) {
        this(context, null);
        this.shaderColorStart = shaderColorStart;
        this.shaderColorEnd = shaderColorEnd;
        this.lineHeight = lineHeight;
        this.lineBottomMargins = lineBottomMargins;
        init();
    }

    public DynamicLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setShaderColorEnd(int shaderColorEnd) {
        this.shaderColorEnd = shaderColorEnd;
    }

    public void setShaderColorStart(int shaderColorStart) {
        this.shaderColorStart = shaderColorStart;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setShader(new LinearGradient(0, 100, getScreenWidth(getContext()), 100, shaderColorStart, shaderColorEnd, Shader.TileMode.MIRROR));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(lineHeight+lineBottomMargins, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(startX, 0, stopX, lineHeight);
        canvas.drawRoundRect(rectF, lineHeight/2, lineHeight/2, paint);
    }


    public void updateView(float startX, float stopX) {
        this.startX = startX;
        this.stopX = stopX;
        invalidate();
    }

}
