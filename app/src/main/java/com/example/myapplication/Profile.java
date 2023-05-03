package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;


public class Profile extends Fragment {
    FirebaseFirestore db;
    CollectionReference usersRef;
    String uid;
    DocumentReference userDoc;
    Button saveButton, editButton;
    TextView nameTextView, usernameTextView, emailTextView, dateTextView, biografiaTextView;
    EditText nameEditText, usernameEditText, emailEditText, dateEditText, biografiaEditText;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_perfil_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView profileImageView = view.findViewById(R.id.profile_image_view);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Glide.with(this /* contexto */)
                .load(currentUser.getPhotoUrl())
                .into(profileImageView);

        saveButton = view.findViewById(R.id.save_button);
        nameTextView = view.findViewById(R.id.name_text_view);
        usernameTextView = view.findViewById(R.id.username_text_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        dateTextView = view.findViewById(R.id.date_text_view);
        biografiaTextView = view.findViewById(R.id.biografia_text_view);
        nameEditText = view.findViewById(R.id.name_edit_text);
        usernameEditText = view.findViewById(R.id.username_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        dateEditText = view.findViewById(R.id.date_edit_text);
        biografiaEditText = view.findViewById(R.id.biografia_edit_text);
        editButton = view.findViewById(R.id.edit_profile_button);
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDoc = usersRef.document(uid);

        userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("Name");
                    String username = documentSnapshot.getString("Username");
                    String email = documentSnapshot.getString("email");
                    String photoUrl = documentSnapshot.getString("photo");
                    String date = documentSnapshot.getString("Fecha_nac");
                    String biografia = documentSnapshot.getString("biografia");

                    // Set the values to the UI components
                    nameTextView.setText(name);
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    dateTextView.setText(date);
                    biografiaTextView.setText(biografia);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTextView.setVisibility(View.GONE);
                nameEditText.setVisibility(View.VISIBLE);
                usernameTextView.setVisibility(View.GONE);
                usernameEditText.setVisibility(View.VISIBLE);
                emailTextView.setVisibility(View.GONE);
                emailEditText.setVisibility(View.VISIBLE);
                dateTextView.setVisibility(View.GONE);
                dateEditText.setVisibility(View.VISIBLE);
                biografiaTextView.setVisibility(View.GONE);
                biografiaEditText.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                nameEditText.setText(nameTextView.getText());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
                nameTextView.setVisibility(View.VISIBLE);
                nameEditText.setVisibility(View.GONE);
                usernameTextView.setVisibility(View.VISIBLE);
                usernameEditText.setVisibility(View.GONE);
                emailTextView.setVisibility(View.VISIBLE);
                emailEditText.setVisibility(View.GONE);
                dateTextView.setVisibility(View.VISIBLE);
                dateEditText.setVisibility(View.GONE);
                biografiaTextView.setVisibility(View.VISIBLE);
                biografiaEditText.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                nameEditText.setText(nameTextView.getText());
            }
        });

    }

    public void updateInfo() {
        Map<String, Object> updates = new HashMap<>();
        //updates
        updates.put("Name", nameEditText.getText().toString());
        updates.put("Username", usernameEditText.getText().toString());
        updates.put("Fecha_nac", dateEditText.getText().toString());
        updates.put("biografia", biografiaEditText.getText().toString());

        userDoc.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveButton.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating profile", e);
                //Toast.makeText(PerfilPersonal.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

    }


}