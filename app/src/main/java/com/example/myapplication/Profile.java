package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Profile extends Fragment {
    FirebaseFirestore db;
    CollectionReference usersRef;
    String uid;
    DocumentReference userDoc;
    Button saveButton, editButton;
    TextView nameTextView, usernameTextView, emailTextView, dateTextView, biografiaTextView;
    EditText nameEditText, usernameEditText, emailEditText, dateEditText, biografiaEditText;
    ImageView profileImageView;
    StorageReference storageRef;
    FirebaseUser currentUser;
    private static final int RESULTADO_SELECCIONAR_IMAGEN = 1;

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
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        profileImageView = view.findViewById(R.id.profile_image_view);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userDoc = db.collection("users").document(currentUser.getUid());
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
        profileImageView = view.findViewById(R.id.profile_image_view);
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        usersRef = db.collection("users");
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        profileImageView = view.findViewById(R.id.profile_image_view);
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String photoUrl = documentSnapshot.getString("photo");
                if (photoUrl != null) {
                    Glide.with(requireContext())
                            .load(photoUrl)
                            .into(profileImageView);
                } else {
                    // Si no hay una URL de foto en la base de datos, puedes establecer una imagen de placeholder predeterminada o dejarla vacía.
                    // Por ejemplo:
                    // profileImageView.setImageResource(R.drawable.placeholder_image);
                    // O simplemente:
                    // profileImageView.setImageDrawable(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting user document", e);
            }
        });
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
                dateTextView.setVisibility(View.GONE);
                dateEditText.setVisibility(View.VISIBLE);
                biografiaTextView.setVisibility(View.GONE);
                biografiaEditText.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                nameEditText.setText(nameTextView.getText());
                dateEditText.setText(dateTextView.getText());
                biografiaEditText.setText(biografiaTextView.getText());
                usernameEditText.setText(usernameTextView.getText());

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
                dateTextView.setVisibility(View.VISIBLE);
                dateEditText.setVisibility(View.GONE);
                biografiaTextView.setVisibility(View.VISIBLE);
                biografiaEditText.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenClickeada(getView());
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

    public void imagenClickeada(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULTADO_SELECCIONAR_IMAGEN);

        Toast.makeText(getActivity().getApplicationContext(), "Profile picture", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULTADO_SELECCIONAR_IMAGEN && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                String fileName = UUID.randomUUID().toString();
                StorageReference fileRef = storageRef.child("users/" + currentUser.getUid() + "/" + fileName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();

                UploadTask uploadTask = fileRef.putBytes(datas);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // La imagen se subió correctamente
                            // Obtener la URL de descarga de la imagen
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    // Guardar la URL de descarga de la imagen en la base de datos de Firestore
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DocumentReference userDoc = db.collection("users").document(currentUser.getUid());
                                    userDoc.update("photo", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Actualización exitosa
                                                    Glide.with(requireContext())
                                                            .load(imageUrl)
                                                            .into(profileImageView);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Error al actualizar
                                                    Log.e(TAG, "Error updating photo URL", e);
                                                }
                                            });
                                }
                            });
                        } else {
                            // Error al subir la imagen
                            // Manejar el error
                        }
                    }
                });
                Toast.makeText(getActivity().getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

