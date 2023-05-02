package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class PerfilPersonal extends  FragmentActivity {
    FirebaseFirestore db;
    CollectionReference usersRef;
    String uid;
    DocumentReference userDoc;
    Button saveButton, editButton;
    TextView nameTextView, usernameTextView, emailTextView, dateTextView, biografiaTextView;
    EditText nameEditText, usernameEditText, emailEditText, dateEditText, biografiaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);
        saveButton = findViewById(R.id.save_button);
        nameTextView = findViewById(R.id.name_text_view);
        usernameTextView = findViewById(R.id.username_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        biografiaTextView = findViewById(R.id.biografia_text_view);
        nameEditText = findViewById(R.id.name_edit_text);
        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        biografiaEditText = findViewById(R.id.biografia_edit_text);
        editButton = findViewById(R.id.edit_profile_button);
        FirebaseApp.initializeApp(this);
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
                    String name = documentSnapshot.getString("Nombre");
                    String username = documentSnapshot.getString("Username");
                    String email = documentSnapshot.getString("email");
                    String photoUrl = documentSnapshot.getString("photo");
                    String date = documentSnapshot.getString("Fecha_nac");
                    String biografia = documentSnapshot.getString("biografia");

                    // Set the values to the UI components

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

        userDoc.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveButton.setVisibility(View.GONE);
                Toast.makeText(PerfilPersonal.this, "Profile updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating profile", e);
                Toast.makeText(PerfilPersonal.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
