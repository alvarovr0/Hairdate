package com.example.hairdate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PeluqueriaAdapter extends RecyclerView.Adapter<PeluqueriaAdapter.PeluqueriaViewHolder> {

    private List<Peluqueria> peluquerias;

    public PeluqueriaAdapter() {
        this.peluquerias = new ArrayList<>();
    }

    public void setPeluquerias(List<Peluqueria> peluquerias) {
        this.peluquerias = peluquerias;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeluqueriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_perfil_peluqueria, parent, false);
        return new PeluqueriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeluqueriaViewHolder holder, int position) {
        Peluqueria peluqueria = peluquerias.get(position);

        holder.txtNombre.setText(peluqueria.getNombre());
        holder.txtDireccion.setText(peluqueria.getDireccion());
        holder.txtHorario.setText(peluqueria.getHorario());
        // ... mostrar otros atributos de la peluquería en los TextView correspondientes
    }

    @Override
    public int getItemCount() {
        return peluquerias.size();
    }

    public void addPeluqueria(Peluqueria peluqueria) {
        peluquerias.add(peluqueria);
        notifyItemInserted(peluquerias.size() - 1);
    }


    static class PeluqueriaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtDireccion;
        TextView txtHorario;

        public PeluqueriaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
            txtHorario = itemView.findViewById(R.id.txtHorario);
            // ... obtener referencias a los otros TextView del layout del item
        }
    }

}
