package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class LunarZapFeed extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunar_zap_feed);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(generateMyDataList());
        recyclerView.setAdapter(adapter);
    }

    private List<ZAP> generateMyDataList() {
        // Aquí generas una lista de datos que quieras mostrar en el RecyclerView
        List<ZAP> dataList = new ArrayList<>();
        dataList.add(new ZAP("Elemento 1"));
        dataList.add(new ZAP("Elemento 2"));
        dataList.add(new ZAP("Elemento 3"));
        return dataList;
    }
}