package com.fantasy.coolgif.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.fantasy.coolgif.adapter.MainGifRecyclerAdapter;
import com.fantasy.coolgif.adapter.SimpleFragmentPagerAdapter;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.utils.AnalyticsEvent;
import com.fantasy.coolgif.utils.LogUtil;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.utils.PreferenceUtil;
import com.fantasy.coolgif.widget.GIfSingleView;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class GifRecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {


    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
       // findViewById(R.id.setting).setOnClickListener(this);
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        int whiteColor = this.getResources().getColor(android.R.color.white);
        int normalColor = this.getResources().getColor(R.color.tab_text_normal_color);
        tabLayout.setTabTextColors(normalColor,whiteColor);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 清除内存缓存
     *
     * @param context
     */
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    protected void onDestroy() {

        AnalyticsEvent.onEvent("page_" + PreferenceUtil.getGifRequestPosition());
        clearMemoryCache(GifRecyclerViewActivity.this);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
