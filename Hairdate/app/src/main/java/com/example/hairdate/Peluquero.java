package com.example.hairdate;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class Peluquero {
    private String nif;
    private String nombre;
    private String usuario;
    private String horario;
    private String especialidad;

    public Peluquero(){
        //Contrucctor por defecto
    }

    public Peluquero(String nif, String nombre, String usuario, String horario, String especialidad) {
        this.nif = nif;
        this.nombre = nombre;
        this.usuario = usuario;
        this.horario = horario;
        this.especialidad = especialidad;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}