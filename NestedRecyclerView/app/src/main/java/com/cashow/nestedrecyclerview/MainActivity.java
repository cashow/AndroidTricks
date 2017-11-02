package com.cashow.nestedrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        recyclerView.setHasFixedSize(true);

        TextView text = (TextView) findViewById(R.id.text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(recyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
                return new MyHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.nested_recycler_view, parent, false);
                RecyclerView nestedRecyclerView = (RecyclerView) view;
                MyNestAdapter myNestAdapter = new MyNestAdapter();
                nestedRecyclerView.setHasFixedSize(true);
                nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                nestedRecyclerView.setAdapter(myNestAdapter);
                return new MyHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == 0) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(String.valueOf(position));
            }
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 10) {
                return 1;
            }
            return 0;
        }
    }

    private class MyNestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_nested_text, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
