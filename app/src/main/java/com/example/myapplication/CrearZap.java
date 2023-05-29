package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;


public class CrearZap extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_zap, container, false);
        Button crearZap = (Button) rootView.findViewById(R.id.button_create_zap);
        EditText content = (EditText) rootView.findViewById(R.id.edit_text_description);
        crearZap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener una referencia a la colección "zaps"
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference zapCollectionRef = db.collection("zaps");
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                CollectionReference userCollectionRef = db.collection("users");
                if (currentUser == null) {

                } else {
                    // Crear un nuevo documento con los datos del ZAP
                    ZAP zap = new ZAP(content.getText().toString(), new Date(), "", 0, 0, List.of());
                    // Obtener el nombre de usuario del usuario actual
                    String userId = currentUser.getUid();
                    userCollectionRef.document(userId).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String username = documentSnapshot.getString("Username");
                                        // Asignar el nombre de usuario al ZAP
                                        zap.setUsuario(username);

                                        // Agregar el ZAP a la colección "zaps"
                                        zapCollectionRef.add(zap)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "ZAP agregado con ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "Error al agregar el ZAP", e);
                                                    }
                                                });
                                    } else {
                                        Log.d(TAG, "El documento del usuario no existe");
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error al obtener el documento del usuario", e);
                                }
                            });

                }
                Feed fragmento = new Feed();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }


}