package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ZAP> mData;

    // Constructor que recibe los datos
    public MyAdapter(List<ZAP> data) {
        mData = data;
    }

    // Crea una nueva vista para cada elemento del RecyclerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Asigna los datos a cada vista del RecyclerView
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ZAP data = mData.get(position);
        holder.textView.setText(data.getText());
    }

    // Devuelve la cantidad de elementos en los datos
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Define la clase ViewHolder, que representa cada vista en el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
