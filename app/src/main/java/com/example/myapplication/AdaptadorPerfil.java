package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdaptadorPerfil extends RecyclerView.Adapter<AdaptadorPerfil.ViewHolder> {
    private List<PerfilSearch> perfilSearchList;
    private OnItemClickListener clickListener;
    private FirebaseFirestore db;
    Context context;

    public AdaptadorPerfil(List<PerfilSearch> PerfilSearch) {
        perfilSearchList = PerfilSearch;
        db = FirebaseFirestore.getInstance();
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
         context = parent.getContext();
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
        Query query = db.collection("users").whereEqualTo("Username", perfilSearch.getUsername());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String photoUrl = documentSnapshot.getString("photo");
                    Log.d(TAG, "photoUrl: " + photoUrl);
                    if (photoUrl != null) {
                        Glide.with(context)
                                .load(photoUrl)
                                .into(holder.profileImageView);
                    } else {
                        Glide.with(holder.itemView.getContext())
                                .load(R.drawable.profile)
                                .into(holder.profileImageView);
                    }
                } else {
                    // El usuario no fue encontrado en la base de datos.
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting user document", e);
            }
        });
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
