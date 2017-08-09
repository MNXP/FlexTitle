package com.xp.shadowtitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xp.differentflextitle.DynamicLine;
import com.xp.differentflextitle.Tool;
import com.xp.flextitle.R;

/**
 * Created by XMuser on 2017-06-06.
 */

public class ShadowTab extends HorizontalScrollView {

    private static int DEFAULT_COLOR = 0xff000000;
    private static int CHANGED_COLOR = 0xffff0000;
    private DynamicLine dynamicLine;
    private Context context;
    private LinearLayout baseLinearLayout;
    private float textSize;
    private int defaultColor; //默认颜色
    private int changeColor; //渐变颜色
    private int padding;
    private int paddingL;
    private int paddingR;
    private int paddingT;
    private int paddingB;
    private ViewPager vp; //关联的viewpager
    private int currentPos;
    private int shaderColorEnd;
    private int shaderColorStart;
    private float lineHeight;
    private float lineBottomMargins;


    public ShadowTab(Context context) {
        this(context, null);
    }

    public ShadowTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ShadowTab);
        textSize = Tool.px2sp(context, t.getDimension(R.styleable.ShadowTab_text_size, 14));
        defaultColor = t.getColor(R.styleable.ShadowTab_default_color, DEFAULT_COLOR);
        changeColor = t.getColor(R.styleable.ShadowTab_changed_color, CHANGED_COLOR);
        padding = (int) Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_item_padding, 0));
        paddingL = (int) Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_item_padding_l, Tool.dip2px(context, padding)));
        paddingR = (int) Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_item_padding_r, Tool.dip2px(context, padding)));
        paddingT = (int) Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_item_padding_t, Tool.dip2px(context, padding)));
        paddingB = (int) Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_item_padding_b, Tool.dip2px(context, padding)));
        shaderColorStart = t.getColor(R.styleable.ShadowTab_line_start_colors, Color.GREEN);
        shaderColorEnd = t.getColor(R.styleable.ShadowTab_line_end_colors, Color.BLUE);
        lineHeight = Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_line_heights, 10));
        lineBottomMargins = Tool.px2dip(context, t.getDimension(R.styleable.ShadowTab_line_bottom_margin, 10));
        t.recycle();
        createDynamicLine();
        addBaseView(context);

    }

    private void addBaseView(Context context) {
        LinearLayout contentLl = new LinearLayout(getContext());
        contentLl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentLl.setOrientation(LinearLayout.VERTICAL);
        addView(contentLl);

        baseLinearLayout = new LinearLayout(context);
        baseLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        baseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        baseLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

        contentLl.addView(baseLinearLayout);
        contentLl.addView(dynamicLine);
    }


    private void createDynamicLine() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dynamicLine = new DynamicLine(getContext(), shaderColorStart, shaderColorEnd, (int) lineHeight, (int) lineBottomMargins);
        dynamicLine.setLayoutParams(params);
    }

    /**
     * 关联viewpager，
     *
     * @param vp
     */
    public void setupWithViewPager(final ViewPager vp) {
        this.vp = vp;
        if (vp == null || vp.getAdapter() == null) {
            return;
        }
        addLyricTextViews();
        addClickEvent();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                itemScroll(position, positionOffset);

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ccy", "onPageSelected" + position);
                resetAllItem();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {  //解决残影，不够完美，被这个问题烦死了！！！！
                    resetAllItem();
                }
            }
        });

    }

    private void resetAllItem() {
        for (int i = 0; i < baseLinearLayout.getChildCount(); i++) {
            ShadowTextView ltv = (ShadowTextView) baseLinearLayout.getChildAt(i);
            if (i == vp.getCurrentItem()) {
                ltv.setProgress(1f);
            } else {
                ltv.setProgress(0f);
            }
        }
        invalidate();
    }

    /**
     * 添加所有item
     */
    private void addLyricTextViews() {
        currentPos = vp.getCurrentItem();
        for (int i = 0; i < vp.getAdapter().getCount(); i++) {
            ShadowTextView ltv = new ShadowTextView(context);
            String str = vp.getAdapter().getPageTitle(i) + "";
            ltv.setAll(0f, str, textSize, defaultColor, changeColor, ShadowTextView.LEFT);
            ltv.setPadding(paddingL, paddingT, paddingR, paddingB);
            ltv.setTag(i);
            baseLinearLayout.addView(ltv);
            if (i == currentPos) {
                ltv.setProgress(1);
            }
        }
        String str = vp.getAdapter().getPageTitle(0) + "";
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextSize(textSize);

        int startX = paddingL;
        int endX = (int)Tool.getTextViewLength(textView)+paddingL;
        dynamicLine.updateView(startX,endX);


    }

    private void addClickEvent() {
        for (int i = 0; i < baseLinearLayout.getChildCount(); i++) {
            ShadowTextView ltv = (ShadowTextView) baseLinearLayout.getChildAt(i);
            ltv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    vp.setCurrentItem(pos);
                }
            });
        }
    }

    private void itemScroll(int position, float positionOffset) {
        if (positionOffset > 0 && position + 1 <= vp.getAdapter().getCount()) {
            ShadowTextView left = (ShadowTextView) baseLinearLayout.getChildAt(position);
            ShadowTextView right = (ShadowTextView) baseLinearLayout.getChildAt(position + 1);
            left.setDirection(ShadowTextView.RIGHT);
            left.setProgress(1 - positionOffset);
            right.setDirection(ShadowTextView.LEFT);
            right.setProgress(positionOffset);
            int leftWidth = 0;
            for (int i=0 ;i<position;i++){
                leftWidth = leftWidth+baseLinearLayout.getChildAt(i).getMeasuredWidth();
            }
            int startX = leftWidth+left.getPaddingLeft()+(int)(left.getMeasuredWidth()*positionOffset);
            int endX = leftWidth+left.getMeasuredWidth()-left.getPaddingRight()+(int)(right.getMeasuredWidth()*positionOffset);
            dynamicLine.updateView(startX,endX);

            invalidate();
            layoutScroll(position, positionOffset);
        }
    }


    private void layoutScroll(int pos, float positionOffset) {
        scrollTo(calculateScrollXForTab(pos, positionOffset), 0);
    }

    private int calculateScrollXForTab(int pos, float positionOffset) {
        ShadowTextView selectedChild = (ShadowTextView) baseLinearLayout.getChildAt(pos);
        ShadowTextView nextChild = (ShadowTextView) baseLinearLayout.getChildAt(pos + 1);
        final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
        final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
        int scrollBase = selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2);
        int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);
        return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                ? scrollBase + scrollOffset
                : scrollBase - scrollOffset;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


}
