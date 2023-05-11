package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    FloatingActionButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_lunar_zap_feed, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(generateMyDataList());
        recyclerView.setAdapter(adapter);
        button = rootView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir el fragment de crear zap
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CrearZap()).commit();


            }
        });
        return rootView;
    }

    private List<ZAP> generateMyDataList() {
        // Aquí generas una lista de datos que quieras mostrar en el RecyclerView
        List<String> List = new ArrayList<>();
        List<ZAP> dataList = new ArrayList<>();
        for(int i = 0; i < 14; i++) {
            dataList.add(new ZAP("Hola", new Date(), "Usuario 1", 0, 0, List));
        }
         return dataList;
    }
}