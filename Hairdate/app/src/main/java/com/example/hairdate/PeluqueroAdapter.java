package com.example.hairdate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PeluqueroAdapter extends FirestoreRecyclerAdapter<Peluquero, PeluqueroAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;

    public PeluqueroAdapter(@NonNull FirestoreRecyclerOptions<Peluquero> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Peluquero model) {
        holder.nombre.setText(model.getNombre());
        holder.horario.setText(model.getHorario());
        holder.especialidad.setText(model.getEspecialidad());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peluquero, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView horario;
        TextView especialidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombrePeluquero);
            horario = itemView.findViewById(R.id.txtHorarioPeluquero);
            especialidad = itemView.findViewById(R.id.txtEspecialidad);
        }
    }


}
