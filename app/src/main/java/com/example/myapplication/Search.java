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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private RecyclerView recyclerView;
    private AdaptadorPerfil adapter;
    private EditText searchEditText;
    private Button searchButton;
    private List<PerfilSearch> results = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.searchRecyclerView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString().trim();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                results.clear(); // Limpiar los resultados anteriores

                for (int i = 1; i <= searchTerm.length(); i++) {
                    String subTerm = searchTerm.substring(0, i);
                    Query query = db.collection("usuarios")
                            .whereGreaterThanOrEqualTo("Username", subTerm)
                            .whereLessThanOrEqualTo("Username", subTerm + "\uf8ff");

                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                results.clear(); // Clear previous results
                                for (DocumentSnapshot document : task.getResult()) {
                                    // Get the necessary data from the DocumentSnapshot
                                    String username = document.getString("Username");
                                    int photo = R.drawable.profile; // Change this to the correct resource for the profile image

                                    // Create a PerfilSearch object
                                    PerfilSearch perfilSearch = new PerfilSearch(username, photo);

                                    // Add the PerfilSearch object to the results list
                                    results.add(perfilSearch);
                                }

                                // Update the adapter with the new results
                                if (adapter == null) {
                                    adapter = new AdaptadorPerfil(results);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                } else {
                                    adapter.setDataList(results);
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

