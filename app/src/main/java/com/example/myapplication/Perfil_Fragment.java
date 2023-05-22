package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Perfil_Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_, container, false);

        // Obtener los datos enviados al fragmento
        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            int photoResourceId = bundle.getInt("photo");

            // Acceder a los elementos de la interfaz de usuario en el fragmento y asignar los datos recibidos
            TextView usernameTextView = view.findViewById(R.id.username_text);
            ImageView profileImageView = view.findViewById(R.id.cover_image);

            usernameTextView.setText(username);
            profileImageView.setImageResource(photoResourceId);
        }

        return view;
    }


}