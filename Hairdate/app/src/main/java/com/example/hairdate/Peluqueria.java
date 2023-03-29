package com.example.hairdate;

public class Peluqueria {

    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;

    public Peluqueria(){
        //Construcctor por defecto
    }
    public Peluqueria(String nombre, String direccion, double latitud, double longitud){
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud_nueva) {
        this.longitud = longitud_nueva;
    }

    public void setNombre(String nombre_nuevo) {
        this.nombre = nombre_nuevo;
    }
}
