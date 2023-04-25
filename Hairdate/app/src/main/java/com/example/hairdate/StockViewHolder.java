package com.example.hairdate;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder {
    private EditText nombreEditText;
    private EditText precioEditText;
    private EditText cantidadEditText;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreEditText = itemView.findViewById(R.id.nombreEditText);
        precioEditText = itemView.findViewById(R.id.precioEditText);
        cantidadEditText = itemView.findViewById(R.id.cantidadEditText);
    }

    public void bind(peluqueroStock stock) {
        nombreEditText.setText(stock.getNombreProducto());
        precioEditText.setText(String.valueOf(stock.getPrecio()));
        cantidadEditText.setText(String.valueOf(stock.getCantidad()));
    }
}
