package com.example.hairdate;

import java.util.ArrayList;

public class Peluqueria {
    private String direccion;
    private String horario;
    private String numeroTelefono;
    private ArrayList<Peluquero> peluqueros;
    private String nombre;

    public Peluqueria() {
        // Constructor vacío requerido por Firebase Firestore
    }

    public Peluqueria(String direccion, String horario, String numeroTelefono, ArrayList<Peluquero> peluqueros, String nombre) {
        this.direccion = direccion;
        this.horario = horario;
        this.numeroTelefono = numeroTelefono;
        this.peluqueros = peluqueros;
        this.nombre = nombre;
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

    public ArrayList<Peluquero> getPeluqueros() {
        return peluqueros;
    }

    public void setPeluqueros(ArrayList<Peluquero> peluqueros) {
        this.peluqueros = peluqueros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}