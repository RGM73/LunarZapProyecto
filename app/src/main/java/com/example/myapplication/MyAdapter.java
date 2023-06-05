package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ZAP> mZAPList;
    private FirebaseFirestore db;
    Context context;

    public MyAdapter(List<ZAP> ZAPList) {
        mZAPList = ZAPList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View ZAPView = inflater.inflate(R.layout.zap, parent, false);
        return new ViewHolder(ZAPView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ZAP zap = mZAPList.get(position);

        holder.usernameTextView.setText(zap.getUsuario() != null ? zap.getUsuario().toString() : "");
        holder.contentTextView.setText(zap.getContenido() != null ? zap.getContenido().toString() : "");
        holder.datetimeTextView.setText(zap.getFecha() != null ? zap.getFecha().toString() : "");

        Query query = db.collection("users").whereEqualTo("Username", zap.getUsuario());
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
                                .into(holder.imageView);
                    } else {
                        Glide.with(holder.itemView.getContext())
                                .load(R.drawable.profile)
                                .into(holder.imageView);
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

        holder.likeButton.setText("Likes: " + zap.getLikes());
        holder.dislikeButton.setText("Dislikes: " + zap.getDislikes());
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.collection("zaps").whereEqualTo("usuario", zap.getUsuario());
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            document.getReference().update("likes", zap.getLikes() + 1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }
                    }
                });
            }
        });

        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.collection("zaps").whereEqualTo("usuario", zap.getUsuario());
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            document.getReference().update("dislikes", zap.getDislikes() + 1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mZAPList.size();
    }
    public void setDataList(List<ZAP> ZAPList) {
        mZAPList = ZAPList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView usernameTextView;
        public TextView contentTextView;
        public TextView datetimeTextView;
        public Button likeButton;
        public Button dislikeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.avatar_image);
            usernameTextView = itemView.findViewById(R.id.username_text);
            contentTextView = itemView.findViewById(R.id.content_text);
            datetimeTextView = itemView.findViewById(R.id.datetime_text);
            likeButton = itemView.findViewById(R.id.like_button);
            dislikeButton = itemView.findViewById(R.id.dislike_button);
        }
    }
}

