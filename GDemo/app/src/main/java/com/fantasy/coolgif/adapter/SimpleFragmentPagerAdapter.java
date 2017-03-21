package com.fantasy.coolgif.adapter;

import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fantasy.coolgif.fragment.MovieGifFragment;
import com.fantasy.coolgif.main.MainGifFragment;

/**
 * Created by fanlitao on 17/3/21.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


    /**
     * Created by Administrator on 2015/7/30.
     */

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"搞笑", "电影", "tab3"};
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  MainGifFragment.newInstance(position);
            case 1:
                return MovieGifFragment.newInstance(position);
        }

        return MainGifFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
