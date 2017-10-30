package com.delizhi.cardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private int mTextViewHeight;
    private RecyclerViewHeader mRecyclerViewHeader;
    //头部图片(轮播图的高度)
    private int mRecyclerHeaderBannerHeight;
    //头部的高度
    private int mRecyclerHeaderHeight;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.home);
        mRecyclerView = findViewById(R.id.recyclerview);
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            contents.add("这是CardView-------"+i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CardViewAdapter(this,contents));
        //获取到文本的高度
        ViewTreeObserver vto = mTextView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mTextViewHeight = mTextView.getMeasuredHeight();
            }
        });

        //轮播图片的高度--和xml图片的高度是一样的
        mRecyclerHeaderBannerHeight = (int) getResources().getDimension(R.dimen.home_page_banner_height);
        //RecyclerView每个Item之间的距离,和Adapter中设置的距离一样
        final int recyclerItemHeight = (int) getResources().getDimension(R.dimen.home_page_list_item_margin_top);

        //添加头部视图,其布局文件就忽略
        mRecyclerViewHeader = RecyclerViewHeader.fromXml(this, R.layout.list_item_prime_product_header);
        //设置其滑动事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //设置其透明度
                float alpha = 0;
                int scollYHeight = getScollYHeight(true, mRecyclerHeaderHeight);
                //起始截止变化高度,如可以变化高度为mRecyclerHeaderHeight
                int baseHeight = mRecyclerHeaderBannerHeight - recyclerItemHeight - mTextViewHeight;
                if(scollYHeight >= baseHeight) {
                    //完全不透明
                    alpha = 1;
                }else {
                    //产生渐变效果
                    alpha = scollYHeight / (baseHeight*1.0f);
                }
                mTextView.setAlpha(alpha);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //将头部视图添加到RecyclerView中
        mRecyclerViewHeader.attachTo(mRecyclerView);
        //第一次进来其状态显示
        mRecyclerViewHeader.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerHeaderHeight =  mRecyclerViewHeader.getHeight();
                mTextViewHeight = mTextView.getHeight();
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setAlpha(0);
            }
        });

        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(4000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(4000);
            }
        });
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    /**
     * 计算RecyclerView滑动的距离
     * @param hasHead 是否有头部
     * @param headerHeight RecyclerView的头部高度
     * @return 滑动的距离
     */
    private int getScollYHeight(boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = firstVisiableChildView.getHeight();
        //有头部
        if(hasHead) {
            return headerHeight + itemHeight*position - firstVisiableChildView.getTop();
        }else {
            return itemHeight*position - firstVisiableChildView.getTop();
        }
    }
}
