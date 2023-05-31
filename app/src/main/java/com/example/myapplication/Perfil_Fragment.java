package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class Perfil_Fragment extends Fragment {
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_, container, false);
        context = container.getContext();
        TextView usernameTextView = view.findViewById(R.id.username_text);
        ImageView profileImageView = view.findViewById(R.id.cover_image);
        TextView nameTextView = view.findViewById(R.id.nombre_text);
        TextView edadTextView = view.findViewById(R.id.edad_text);
        // Obtener los datos enviados al fragmento
        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("users").whereEqualTo("Username", username);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String photoUrl = documentSnapshot.getString("photo");
                        usernameTextView.setText(username);
                        nameTextView.setText(documentSnapshot.getString("Name"));
                        String fecha = documentSnapshot.getString("Fecha_nac");
                        Date date=new Date(fecha);
                        int edad = (int) ((new Date().getTime() - date.getTime()) / 86400000 / 365);
                        edadTextView.setText(String.valueOf(edad));
                        if (photoUrl != null) {
                            Glide.with(context)
                                    .load(photoUrl)
                                    .into(profileImageView);
                        } else {
                            Glide.with(context)
                                    .load(R.drawable.profile)
                                    .into(profileImageView);
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
        }

        return view;
    }


}