package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public void setDataList(List<ZAP> ZAPList) {
        mZAPList = ZAPList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el modelo de datos según la posición
        ZAP zap = mZAPList.get(position);

        // Establecer los valores de los elementos de la vista
        holder.usernameTextView.setText(zap.getUsuario() != null ? zap.getUsuario().toString() : "");
        holder.contentTextView.setText(zap.getContenido() != null ? zap.getContenido().toString() : "");
        holder.datetimeTextView.setText(zap.getFecha() != null ? zap.getFecha().toString() : "");

        holder.likeButton.setText("Likes: " + zap.getLikes());
        holder.dislikeButton.setText("Dislikes: " + zap.getDislikes());
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
        public Button likeButton;
        public Button dislikeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            usernameTextView = (TextView) itemView.findViewById(R.id.username_text);
            contentTextView = (TextView) itemView.findViewById(R.id.content_text);
            datetimeTextView = (TextView) itemView.findViewById(R.id.datetime_text);
            likeButton = (Button) itemView.findViewById(R.id.like_button);
            dislikeButton = (Button) itemView.findViewById(R.id.dislike_button);
            // likesTextView = (TextView) itemView.findViewById(R.id.likes_text);
            // dislikesTextView = (TextView) itemView.findViewById(R.id.dislikes_text);
        }
    }
}
