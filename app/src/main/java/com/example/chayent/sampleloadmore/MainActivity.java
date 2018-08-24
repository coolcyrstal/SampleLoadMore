package com.example.chayent.sampleloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //    @BindView(R.id.empty_view)
//    TextView emptyView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view_animation)
    LottieAnimationView emptyAnimationView;

    private DataAdapter mAdapter;
    private List<Student> mStudentList = new ArrayList<>();
    protected Handler handler = new Handler();
    private int refreshDelay = 2000;
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
        mAdapter.setOnLoadMoreListener(() -> {
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
        });
    }

    private void setSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void refreshItems() {
        swipeRefreshLayout.setRefreshing(true);
        //Load item
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onItemsLoadComplete();
            }
        }, refreshDelay);
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(mAdapter);
        Toast.makeText(this, "Refresh complete", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }
}
