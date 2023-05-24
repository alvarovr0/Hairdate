package com.example.hairdate;

public class Peluquero {
    private String cif;
    private String id;
    private String direccion;
    private String email;
    private String nombre;
    private String usuario;

    public Peluquero() {
        // Constructor vacío requerido por Firebase Firestore
    }

    public Peluquero(String cif, String id, String direccion, String email, String nombre, String usuario) {
        this.cif = cif;
        this.id = id;
        this.direccion = direccion;
        this.email = email;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
