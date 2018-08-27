package com.example.chayent.sampleloadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.List;

/**
 * DataAdapter.java
 * SampleLoadMore
 * Created by Chayen Tansritrang on 8/23/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class DataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private final int VIEW_REFRESH = -1;

    private List<Student> studentList;

    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private boolean isRefresh = false;
    private OnLoadListener onLoadListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            viewHolder = new CardViewHolder(v);
        } else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
//            viewHolder = new ProgressViewHolder(v);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_anim, parent, false);
            viewHolder = new AnimationViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder) {
            Student singleStudent = studentList.get(position);
            ((CardViewHolder) holder).tvName.setText(singleStudent.getName());
            ((CardViewHolder) holder).tvEmailId.setText(singleStudent.getEmailId());
            ((CardViewHolder) holder).student = singleStudent;
        } else {
            ((AnimationViewHolder) holder).animationView.setRepeatCount(LottieDrawable.INFINITE);
            ((AnimationViewHolder) holder).animationView.playAnimation();
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        int viewType;
//        if (studentList.get(position) != null) {
//            switch (position) {
//                case 0:
//                    isRefresh = true;
//                    viewType = VIEW_REFRESH;
//                    break;
//                case 1:
//                    isRefresh = false;
//                    viewType = VIEW_ITEM;
//                    break;
//                default:
//                    isRefresh = false;
//                    viewType = VIEW_ITEM;
//                    break;
//            }
//        } else {
//            isRefresh = false;
//            viewType = VIEW_PROGRESS;
//        }
//        return viewType;
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    DataAdapter(List<Student> students, RecyclerView recyclerView) {
        studentList = students;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadListener != null) {
                            onLoadListener.onLoadMore();
                        }
                        loading = true;
                    }
                    if(isRefresh){
                        if(onLoadListener != null){
                            onLoadListener.onRefresh();
                        }
                    }
                }
            });
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvEmailId;
        Student student;

        CardViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmailId = itemView.findViewById(R.id.tvEmailId);

            itemView.setOnClickListener(v -> Toast.makeText(v.getContext(),
                    "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                    Toast.LENGTH_SHORT).show());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public static class AnimationViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView animationView;

        AnimationViewHolder(View itemView) {
            super(itemView);
            animationView = itemView.findViewById(R.id.custom_progress_bar_animation_view);
            animationView.setAnimation(R.raw.recycler_loading);
        }
    }
}
