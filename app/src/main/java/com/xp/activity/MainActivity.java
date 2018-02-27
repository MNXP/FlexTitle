package com.xp.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xp.different.MyPagerAdapter;
import com.xp.different.ViewPagerTitle;
import com.xp.flextitle.R;
import com.xp.shadow.ShadowTab;

import java.util.ArrayList;

/**
 *
 * Created by xiangpan on 2017/8/1.
 *
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ArrayList<View> views;
    private View view1;
    private View view2;
    private View view3;
    private ViewPagerTitle viewPagerTitle;
    private View view4;
    private View view5;
    private View view6;
    private View view7;
    private View view8;
    private String[] str = new String[]{"首页","推荐内容","视频直播","图片","新段子","精华","热门","体育直播间"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFlexTitle();
        initShadowTitle();
    }

    private void initShadowTitle() {


        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2, null);
        view3 = inflater.inflate(R.layout.layout3, null);
        view4 = inflater.inflate(R.layout.layout4, null);
        view5 = inflater.inflate(R.layout.layout5, null);
        view6 = inflater.inflate(R.layout.layout6, null);
        view7 = inflater.inflate(R.layout.layout7, null);
        view8 = inflater.inflate(R.layout.layout8, null);

        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        views.add(view6);
        views.add(view7);
        views.add(view8);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ShadowTab shadowTab = (ShadowTab) findViewById(R.id.indicator);
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }


            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return str[position];
            }
        });

        shadowTab.setupWithViewPager(vp);

    }

    private void initFlexTitle() {


        views = new ArrayList<>();

        viewPagerTitle = (ViewPagerTitle)findViewById(R.id.pager_title);
        pager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerTitle.initData(new String[]{"关注", "推荐", "视频", "直播", "图片", "段子", "精华", "热门"}, pager, 0);


        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2, null);
        view3 = inflater.inflate(R.layout.layout3, null);
        view4 = inflater.inflate(R.layout.layout4, null);
        view5 = inflater.inflate(R.layout.layout5, null);
        view6 = inflater.inflate(R.layout.layout6, null);
        view7 = inflater.inflate(R.layout.layout7, null);
        view8 = inflater.inflate(R.layout.layout8, null);

        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        views.add(view6);
        views.add(view7);
        views.add(view8);

        adapter = new MyPagerAdapter(views);
        pager.setAdapter(adapter);
    }


}

