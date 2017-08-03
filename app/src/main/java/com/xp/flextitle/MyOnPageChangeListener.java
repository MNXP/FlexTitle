package com.xp.flextitle;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static com.xp.flextitle.Tool.getScreenWidth;
import static com.xp.flextitle.Tool.getTextViewLength;

/**
 * Created by xiangpan on 2017/8/1.
 */

public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private static final String TAG = "test_tag";
    private final Context context;
    private int fixLeftDis;
    private ArrayList<TextView> textViews;
    private ViewPagerTitle viewPagerTitle;
    private DynamicLine dynamicLine;
    private float defaultTextSize;//未选择文字长度
    private float selectTextSize;//选择文字长度
    private ViewPager pager;
    private int pagerCount;
    private int screenWidth;
    private int lineWidth;
    private int lastPosition;
    private int dis;
    private int[] location = new int[2];
    private float left;
    private float right;
    private boolean titleCenter;
    private boolean hasSeting;

    public MyOnPageChangeListener(Context context, ViewPager viewPager, DynamicLine dynamicLine, ViewPagerTitle viewPagerTitle, int allLength, int margin, int fixLeftDis, float defaultTextSize, float selectTextSize, boolean titleCenter) {
        this.viewPagerTitle = viewPagerTitle;
        this.pager = viewPager;
        this.dynamicLine = dynamicLine;
        this.context = context;
        this.defaultTextSize = defaultTextSize;
        this.selectTextSize = selectTextSize;
        this.titleCenter = titleCenter;
        textViews = viewPagerTitle.getTextView();
        pagerCount = textViews.size();
        screenWidth = getScreenWidth(context);

        lineWidth = (int) getTextViewLength(textViews.get(0));
        dis = margin;
        this.fixLeftDis = fixLeftDis;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (titleCenter) {
            if (lastPosition > position) {
                left = (position + positionOffset) * (defaultTextSize + 2 * dis) + dis + fixLeftDis;
                right = (lastPosition + 1) * defaultTextSize + (lastPosition * 2 + 1) * dis + fixLeftDis;
                dynamicLine.updateView(left, right);
            } else {
                if (positionOffset > 0.5f) {
                    positionOffset = 0.5f;
                }
                left = lastPosition * defaultTextSize + (lastPosition * 2 + 1) * dis + fixLeftDis;
                right = (position + positionOffset * 2) * (defaultTextSize + 2 * dis) + dis + fixLeftDis + lineWidth;
                dynamicLine.updateView(left, right);
            }
        } else {
            if (lastPosition > position) {
                left = (position + positionOffset) * (defaultTextSize + dis) + dis + fixLeftDis;
                right = (lastPosition + 1) * defaultTextSize + (lastPosition + 1) * dis + fixLeftDis;
                dynamicLine.updateView(left, right);
            } else {
                if (positionOffset > 0.5f) {
                    positionOffset = 0.5f;
                }
                left = lastPosition * defaultTextSize + (lastPosition + 1) * dis + fixLeftDis;
                right = (position + positionOffset * 2) * (defaultTextSize + dis) + dis + fixLeftDis + lineWidth;
                dynamicLine.updateView(left, right);
            }
        }


    }


    @Override
    public void onPageSelected(int position) {
        viewPagerTitle.setCurrentItem(position);

    }


    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == SCROLL_STATE_SETTLING) {
            hasSeting = true;
            int position = pager.getCurrentItem();
            if (position + 1 < textViews.size() && position - 1 >= 0) {
                textViews.get(position).getLocationOnScreen(location);
                int x ;
                if (position > lastPosition) {
                    x = location[0] - screenWidth / 2 - fixLeftDis + lineWidth / 2;
                } else {
                    x = location[0] - screenWidth / 2 + fixLeftDis + lineWidth / 2;
                }

                viewPagerTitle.smoothScrollBy(x, 0);

            } else if (position + 1 == textViews.size()) {
                viewPagerTitle.smoothScrollBy(lineWidth, 0);

            }
            lastPosition = pager.getCurrentItem();

        } else if (state == SCROLL_STATE_IDLE) {
            if (!hasSeting){
                int position = pager.getCurrentItem();
                if (position + 1 < textViews.size() && position - 1 >= 0) {
                    textViews.get(position).getLocationOnScreen(location);
                    int x ;
                    if (position > lastPosition) {
                        x = location[0] - screenWidth / 2 - fixLeftDis + lineWidth / 2;
                    } else {
                        x = location[0] - screenWidth / 2 + fixLeftDis + lineWidth / 2;
                    }

                    viewPagerTitle.smoothScrollBy(x, 0);

                } else if (position + 1 == textViews.size()) {
                    viewPagerTitle.smoothScrollBy(lineWidth, 0);

                }
            }
            hasSeting = false;
            lastPosition = pager.getCurrentItem();
            if (titleCenter) {
                float leftS = lastPosition * defaultTextSize + (lastPosition * 2 + 1) * dis + fixLeftDis;
                float rightS = leftS + lineWidth;
                if (left != leftS || right != rightS) {
                    dynamicLine.updateView(leftS, rightS);
                }
            } else {
                float leftS = lastPosition * defaultTextSize + (lastPosition + 1) * dis + fixLeftDis;
                float rightS = leftS + lineWidth;
                if (left != leftS || right != rightS) {
                    dynamicLine.updateView(leftS, rightS);
                }
            }

        }

    }

}

