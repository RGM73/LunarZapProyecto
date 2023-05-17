package com.example.myapplication;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Feed extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    FloatingActionButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_lunar_zap_feed, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        button = rootView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CrearZap()).commit();
            }
        });

        // Mover la llamada a generateMyDataList() después de configurar el adaptador
        generateMyDataList();

        return rootView;
    }

    private void generateMyDataList() {
        final List<ZAP> dataList = new ArrayList<>();

        // Acceder a la instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference zapCollectionRef = db.collection("zaps");

        // Obtener los documentos de la colección "zaps"
        zapCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y crear un objeto ZAP
                        String username = document.getString("usuario");
                        Date date = document.getDate("fecha");
                        String content = document.getString("contenido");
                        int likes = document.getLong("likes").intValue();
                        int dislikes = document.getLong("dislikes").intValue();

                        ZAP zap = new ZAP(content, date, username, likes, dislikes, new ArrayList<>());
                        dataList.add(zap);
                    }
                    Collections.sort(dataList, Collections.reverseOrder());

                    // Actualizar la lista de datos del adaptador
                    adapter.setDataList(dataList);
                    Log.d(TAG, "Data list updated. Size: " + dataList.size());
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}

