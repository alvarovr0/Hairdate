package com.example.hairdate;

public class peluqueroStock {
    private String nombreProducto;
    private double precio;
    private int cantidad;

    public peluqueroStock(String name, double price, int quantity){
        this.nombreProducto = name;
        this.precio = price;
        this.cantidad = quantity;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
