package com.example.hairdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PeluqueroAdapter extends FirestoreRecyclerAdapter<Peluquero, PeluqueroAdapter.ViewHolder> {

    private View.OnClickListener onClickListener;

    public PeluqueroAdapter(@NonNull FirestoreRecyclerOptions<Peluquero> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Peluquero model) {
        holder.nombre.setText(model.getNombre());
        holder.horario.setText(model.getHorario());
        holder.especialidad.setText(model.getEspecialidad());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Peluquero peluquero = getItem(position);
                    Bundle args = new Bundle();
                    args.putString("nombre", peluquero.getNombre());
                    args.putString("horario", peluquero.getHorario());
                    args.putString("especialidad", peluquero.getEspecialidad());
                    Navigation.findNavController(v).navigate(R.id.action_listaPeluqueros_to_peluquero_detalle, args);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peluquero, parent, false);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
