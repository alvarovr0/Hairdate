package com.example.hairdate;

//Importamos la lib serializable para que sea más facil pasar los datos de una clase a otra
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class Peluqueria implements Serializable {
    private String direccion;
    private String horario;
    private String numeroTelefono;
    private String nombre;
    private ImageView profileImagePelu;


    public Peluqueria() {
        // Constructor vacío requerido por Firebase Firestore
    }

    public Peluqueria(String direccion, String horario, String numeroTelefono, String nombre) {
        this.direccion = direccion;
        this.horario = horario;
        this.numeroTelefono = numeroTelefono;
        this.nombre = nombre;
    }

    public void imagenPerfilPelu(View view){
        profileImagePelu = view.findViewById(R.id.imgFoto);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        // Muestra una imagen
        StorageReference profileRef = storageReference.child(numeroTelefono + "_pelu.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImagePelu);
            }
        });
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}