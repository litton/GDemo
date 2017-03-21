package com.fantasy.coolgif.main;

import com.bumptech.glide.Glide;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.adapter.MainGifRecyclerAdapter;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.utils.PreferenceUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fanlitao on 17/3/21.
 */

public class MainGifFragment extends Fragment implements INetworkCallback {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private RecyclerView mGifPager;
    private MainGifRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mFirstVisiableItemPosition;
    private int mLastVisiableItemPosition;
    public static final int PRE_REQUERY_COUNT = 3;

    private boolean mWaitingResponse = false;

    private int mInitRequestPosition;
    private int mLastSavePosition;

    public static final boolean SUPPORT_PRELOAD_GIF = true;

    public static MainGifFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MainGifFragment pageFragment = new MainGifFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_gif_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }
    private void init() {
        mInitRequestPosition = PreferenceUtil.getGifRequestPosition();
        NetworkBus.getDefault().getTopGifList(mInitRequestPosition, this);
        mWaitingResponse = true;

        mGifPager = (RecyclerView) getActivity().findViewById(R.id.gif_list);
        mGifPager.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false);
        mGifPager.setLayoutManager(mLayoutManager);
        mGifPager.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mAdapter == null) {
                    return;
                }
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:

                        int newPos = mInitRequestPosition + mFirstVisiableItemPosition + 1;
                        if (newPos < mLastSavePosition) {
                            return;
                        }
                        PreferenceUtil.saveGifCurrentPos(mInitRequestPosition + mFirstVisiableItemPosition + 1);
                        mLastSavePosition = mInitRequestPosition + mFirstVisiableItemPosition + 1;

                        if (SUPPORT_PRELOAD_GIF) {
                            if (mAdapter != null) {
                                GifItem item = mAdapter.getItemByPosition(mLastVisiableItemPosition + 1);
                                if (item != null) {
                                    if (MainGifFragment.this.isAdded()) {
                                        Glide.with(MainGifFragment.this).load(item.gif_url).downloadOnly(500, 500);
                                    }

                                }

                                GifItem item2 = mAdapter.getItemByPosition(mLastVisiableItemPosition + 2);
                                if (item2 != null) {
                                    if (MainGifFragment.this.isAdded()) {
                                        Glide.with(MainGifFragment.this).load(item2.gif_url).downloadOnly(500, 500);
                                    }
                                }
                            }

                        }


                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        if (!mWaitingResponse && (visibleItemCount > 0 && (mLastVisiableItemPosition) >= totalItemCount - PRE_REQUERY_COUNT)) {
                            NetworkBus.getDefault().getTopGifList(totalItemCount + mInitRequestPosition, MainGifFragment.this);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        // Glide.with(getApplicationContext()).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //Glide.with(getApplicationContext()).pauseRequests();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager == null) {
                    return;
                }
                mFirstVisiableItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                mLastVisiableItemPosition = mLayoutManager.findLastVisibleItemPosition();

                if (mGifPager != null && mGifPager.getChildCount() > 0) {
                    if (mGifPager.getChildAt(0).getTop() < 0) {
                        mFirstVisiableItemPosition = mFirstVisiableItemPosition + 1;
                    }
                }

            }
        });
    }


    @Override
    public void onResponse(GifResponse response) {
        mWaitingResponse = false;
        if (mAdapter == null) {
            mAdapter = new MainGifRecyclerAdapter(mGifPager, response.data);
            mGifPager.setAdapter(mAdapter);
        } else {
            mAdapter.addNewDataList(response.data);
        }
    }

    @Override
    public void onDeleteCompeletd(int gifId) {

    }

    @Override
    public void onFailed() {
        mWaitingResponse = false;
    }
}
