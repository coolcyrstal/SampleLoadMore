package com.example.chayent.sampleloadmore;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
            PtrClassicFrameLayout swipeRefreshLayout;
    @BindView(R.id.empty_view_animation)
    LottieAnimationView emptyAnimationView;

    private DataAdapter mAdapter;
    private List<Student> mStudentList = new ArrayList<>();
    protected Handler handler = new Handler();
    private int refreshDelay = 2500;
    private int loadMoreDelay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadData();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.scrollToPosition(1);

        mAdapter = new DataAdapter(mStudentList, recyclerView);
        recyclerView.setAdapter(mAdapter);

        if (mStudentList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
            emptyAnimationView.setVisibility(View.VISIBLE);
            emptyAnimationView.setRepeatCount(LottieDrawable.INFINITE);
            emptyAnimationView.playAnimation();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
            emptyAnimationView.setVisibility(View.GONE);
        }

        setLoadListener();
        setSwipeRefreshListener();
    }

    private void loadData() {
        for (int i = 1; i <= 10; i++) {
            mStudentList.add(new Student("Test" + i, "test id" + i));
        }
    }

    private void setLoadListener() {
        mAdapter.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoadMore() {
                mStudentList.add(null);
                mAdapter.notifyItemInserted(mStudentList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStudentList.remove(mStudentList.size() - 1);
                        mAdapter.notifyItemRemoved(mStudentList.size());

                        int start = mStudentList.size();
                        int end = start + 10;
                        for (int i = start + 1; i <= end; i++) {
                            mStudentList.add(new Student("Test" + i, "test id" + i));
                        }
                        mAdapter.notifyItemInserted(mStudentList.size());
                        mAdapter.setLoaded();
                    }
                }, loadMoreDelay);
            }

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, refreshDelay);
            }
        });
    }

    private void setSwipeRefreshListener() {
//        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.canChildScrollUp();
//                refreshItems();
//            }
//        });
        swipeRefreshLayout.setResistance(1.7f);
        swipeRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        swipeRefreshLayout.setDurationToClose(200);
        swipeRefreshLayout.setDurationToCloseHeader(1000);
        swipeRefreshLayout.setPullToRefresh(false);
        swipeRefreshLayout.setKeepHeaderWhenRefresh(true);
        refreshItems();
    }

    private void refreshItems() {
        LinearLayout refreshLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.progressbar_anim, swipeRefreshLayout, false);
        LottieAnimationView refreshAnimationView = refreshLayout.findViewById(R.id.custom_progress_bar_animation_view);
        refreshAnimationView.setAnimation(R.raw.recycler_loading);
        refreshAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        refreshAnimationView.playAnimation();
        swipeRefreshLayout.setHeaderView(refreshLayout);
        swipeRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onItemsLoadComplete();
                        swipeRefreshLayout.refreshComplete();
                    }
                }, refreshDelay);
            }
        });
//        swipeRefreshLayout.setRefreshing(true);
//        if(swipeRefreshLayout.isRefreshing()){
//            swipeRefreshLayout.setRefreshing(false);
//            refreshAnimationView.setVisibility(View.VISIBLE);
//            refreshAnimationView.setRepeatCount(LottieDrawable.INFINITE);
//            refreshAnimationView.playAnimation();
//        }
//        //Load item
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refreshAnimationView.setVisibility(View.GONE);
//                onItemsLoadComplete();
////                recyclerView.getLayoutManager().scrollToPosition(1);
//            }
//        }, refreshDelay);
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(mAdapter);
        Toast.makeText(this, "Refresh complete", Toast.LENGTH_SHORT).show();
//        swipeRefreshLayout.setRefreshing(false);
    }
}
