package com.example.chayent.sampleloadmore;

import android.os.Bundle;
import android.os.Handler;
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
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view_refresh_layout)
    PtrClassicFrameLayout animationRefreshLayout;
    //    SwipeRefreshLayout animationRefreshLayout;
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

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        loadData();
        mAdapter = new DataAdapter(mStudentList, recyclerView);
        recyclerView.setAdapter(mAdapter);

        checkEmptyData();
        setLoadListener();
        setSwipeRefreshListener();
    }

    private void checkEmptyData() {
        if (mStudentList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyAnimationView.setVisibility(View.VISIBLE);
            emptyAnimationView.setRepeatCount(LottieDrawable.INFINITE);
            emptyAnimationView.playAnimation();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyAnimationView.setVisibility(View.GONE);
        }
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
//        animationRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
//        animationRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                animationRefreshLayout.canChildScrollUp();
//                refreshItems();
//            }
//        });
        refreshItemsWithAnimation();
    }

    private void refreshItemsWithAnimation() {
        LinearLayout refreshLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.progressbar_anim, animationRefreshLayout, false);
        LottieAnimationView refreshAnimationView = refreshLayout.findViewById(R.id.custom_progress_bar_animation_view);
        refreshAnimationView.setAnimation(R.raw.recycler_loading3);
        animationRefreshLayout.setHeaderView(refreshLayout);
        animationRefreshLayout.setPinContent(true);
        animationRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (position != 0) {
                    return false;
                } else {
                    return true;
                }
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                refreshAnimationView.playAnimation();
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onItemsLoadComplete();
                        animationRefreshLayout.refreshComplete();
                        refreshAnimationView.cancelAnimation();
                    }
                }, refreshDelay);
            }
        });
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
//        recyclerView.setAdapter(mAdapter);
        Toast.makeText(this, "Refresh complete", Toast.LENGTH_SHORT).show();
//        animationRefreshLayout.setRefreshing(false);
    }

    private void refreshItems() {
//        animationRefreshLayout.setRefreshing(true);
//        if (animationRefreshLayout.isRefreshing()) {
//            animationRefreshLayout.setRefreshing(false);
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
}
