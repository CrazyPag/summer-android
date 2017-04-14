package cn.cerc.summer.android.Activity;

import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.huagu.ehealth.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2017/4/14.
 */
/**
 * 图表Activity
 * 两个自定义View类
 * LineChartView折线图类
 * HistogramView柱状图类
 * 一个Activity中嵌套了两个fragment(v4)，是用viewpager对页面进行滑动
 *
 */


public class ChartActivity extends FragmentActivity {

    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter adapter;
    // 设置是否显示动画，为了防止在创建时就开启动画，用以下两个参数做了判断，只有当看到视图后才会显示动画
    public static int flag1 = 2;
    public static int flag2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
      //  viewPager = (ViewPager) findViewById(R.id.record_viewpager);
        fragments = new ArrayList<Fragment>();
     //   RecordPager1 recordPager1 = new RecordPager1();
      //  RecordPager2 recordPager2 = new RecordPager2();
      //  fragments.add(recordPager1);
       // fragments.add(recordPager2);

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && flag1 == 1) {
                    flag1 = 2;
                    fragments.get(0).onResume();
                    flag1 = 3;
                }
                if (position == 1 && flag2 == 1) {
                    flag2 = 2;
                    fragments.get(1).onResume();
                    flag2 = 3;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
