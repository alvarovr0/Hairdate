package com.example.hairdate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder {
    private TextView nombreTextView;
    private TextView precioTextView;
    private TextView cantidadTextView;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        nombreTextView = itemView.findViewById(R.id.nombreTextView);
        precioTextView = itemView.findViewById(R.id.precioTextView);
        cantidadTextView = itemView.findViewById(R.id.cantidadTextView);
    }

    public void bind(peluqueroStock stock) {
        nombreTextView.setText(stock.getNombreProducto());
        precioTextView.setText(String.valueOf(stock.getPrecio()));
        cantidadTextView.setText(String.valueOf(stock.getCantidad()));
    }
}
