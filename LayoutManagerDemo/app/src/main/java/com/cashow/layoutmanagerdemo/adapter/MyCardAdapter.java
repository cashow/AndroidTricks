package com.cashow.layoutmanagerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashow.layoutmanagerdemo.MLog;
import com.cashow.layoutmanagerdemo.R;

import butterknife.ButterKnife;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyViewHolder> {
    private int count = 20;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MLog.d();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_image, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MLog.d("onBindViewHolder " + position);
    }

    public void removeTop() {
        count -= 1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return count;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
