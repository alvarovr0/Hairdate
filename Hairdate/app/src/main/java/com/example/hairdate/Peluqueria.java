package com.example.hairdate;

import java.util.List;

public class Peluqueria {
    private String direccion;
    private String horario;
    private int num_tlf;
    private List<Peluquero> Peluquero;
    private String nombre;

    public Peluqueria() {
        // Constructor vacío requerido por Firebase Firestore
    }

    public Peluqueria(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }
}

