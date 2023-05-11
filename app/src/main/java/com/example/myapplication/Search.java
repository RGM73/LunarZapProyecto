package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private EditText searchEditText;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString().trim();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                List<DocumentSnapshot> results = new ArrayList<>();

                for (int i = 1; i <= searchTerm.length(); i++) {
                    String subTerm = searchTerm.substring(0, i);
                    Query query = db.collection("usuarios")
                            .whereGreaterThanOrEqualTo("Username", subTerm)
                            .whereLessThanOrEqualTo("Username", subTerm + "\uf8ff");

                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    // Se encontró el usuario que contiene la subcadena buscada
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    results.add(document);
                                }
                            } else {
                                Log.d(TAG, "Error al buscar usuario: ", task.getException());
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}
