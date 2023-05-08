package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ZAP> mZAPList;

    public MyAdapter(List<ZAP> ZAPList) {
        mZAPList = ZAPList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ZAPView = inflater.inflate(R.layout.zap, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ZAPView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        ZAP zap = mZAPList.get(position);

        // Set item views based on your views and data model
        TextView usernameTextView = holder.usernameTextView;
        usernameTextView.setText(zap.getUsuario());

        TextView contentTextView = holder.contentTextView;
        contentTextView.setText(zap.getContenido());

        TextView datetimeTextView = holder.datetimeTextView;
        datetimeTextView.setText(zap.getFecha().toString());

//        TextView likesTextView = holder.likesTextView;
//        likesTextView.setText("Likes: " + zap.getLikes());
//
//        TextView dislikesTextView = holder.dislikesTextView;
//        dislikesTextView.setText("Dislikes: " + zap.getDislikes());
    }

    @Override
    public int getItemCount() {
        return mZAPList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView contentTextView;
        public TextView datetimeTextView;
        public TextView likesTextView;
        public TextView dislikesTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            usernameTextView = (TextView) itemView.findViewById(R.id.username_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
            datetimeTextView = (TextView) itemView.findViewById(R.id.datetime_text);
           // likesTextView = (TextView) itemView.findViewById(R.id.likes_text);
           // dislikesTextView = (TextView) itemView.findViewById(R.id.dislikes_text);
        }
    }
}
