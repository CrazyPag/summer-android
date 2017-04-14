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
 * ͼ��Activity
 * �����Զ���View��
 * LineChartView����ͼ��
 * HistogramView��״ͼ��
 * һ��Activity��Ƕ��������fragment(v4)������viewpager��ҳ����л���
 *
 */


public class ChartActivity extends FragmentActivity {

    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter adapter;
    // �����Ƿ���ʾ������Ϊ�˷�ֹ�ڴ���ʱ�Ϳ����������������������������жϣ�ֻ�е�������ͼ��Ż���ʾ����
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
