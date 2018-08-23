package com.example.chayent.sampleloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Student> studentList;

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<>();
        handler = new Handler();

        loadData();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DataAdapter(studentList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        setLoadListener();
    }

    private void loadData() {
        for (int i = 1; i <= 10; i++) {
            studentList.add(new Student("Student " + i, "student" + i + "@gmail.com"));
        }
    }

    private void setLoadListener(){
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());

                        int start = studentList.size();
                        int end = start + 10;
                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new Student("Student " + i, "student" + i + "@gmail.com"));
                        }
                        mAdapter.notifyItemInserted(studentList.size());
                        mAdapter.setLoaded();
                    }
                }, 3000);
            }
        });
    }
}
