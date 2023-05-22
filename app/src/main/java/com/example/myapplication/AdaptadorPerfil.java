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

public class AdaptadorPerfil extends RecyclerView.Adapter<AdaptadorPerfil.ViewHolder> {
    private List<PerfilSearch> perfilSearchList;
    private OnItemClickListener clickListener;

    public AdaptadorPerfil(List<PerfilSearch> PerfilSearch) {
        perfilSearchList = PerfilSearch;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View SearchView = inflater.inflate(R.layout.search, parent, false);

        ViewHolder viewHolder = new ViewHolder(SearchView);
        return viewHolder;
    }

    public void setDataList(List<PerfilSearch> list) {
        perfilSearchList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int itemPosition = position;
        PerfilSearch perfilSearch = perfilSearchList.get(position);
        holder.usernameTextView.setText(perfilSearch.getUsername() != null ? perfilSearch.getUsername().toString() : "");
       //holder.profileImageView.setImageResource(perfilSearch.getPhoto() != null ? perfilSearch.getPhoto().toString() : "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(itemPosition);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return perfilSearchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public ImageView profileImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.username_text);
            profileImageView = (ImageView) itemView.findViewById(R.id.avatar_image);
        }
    }
}
