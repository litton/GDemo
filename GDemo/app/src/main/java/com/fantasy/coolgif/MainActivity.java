package com.fantasy.coolgif;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fantasy.coolgif.adapter.MainPagerAdapter;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.utils.DimensUtil;
import com.fantasy.coolgif.widget.GIfSingleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements INetworkCallback {


    private ViewPager mGifPager;
    private MainPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        NetworkBus.getDefault().getTopGifList(this);


        mGifPager = (ViewPager) findViewById(R.id.list);
        mGifPager.setOffscreenPageLimit(1);
        mGifPager.setPageMargin(DimensUtil.dip2px(5));
        mGifPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GIfSingleView gIfSingleView = (GIfSingleView) mGifPager.findViewWithTag(position);
                gIfSingleView.startGifAnimation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onResponse(GifResponse response) {

        if (mAdapter == null) {
            GifItem item = response.data.get(8);
            List<GifItem> dataList = new ArrayList<GifItem>();
            dataList.add(item);
            mAdapter = new MainPagerAdapter(response.data);
            mGifPager.setAdapter(mAdapter);
        }

    }
}
