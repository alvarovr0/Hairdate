package com.example.hairdate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PeluqueriaAdapter /*extends FirestoreRecyclerAdapter<Peluqueria, PeluqueriaAdapter.ViewHolder>*/ {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link //FirestoreRecyclerOptions} for configuration options.
     *
     * @param //options
     */
    public PeluqueriaAdapter(/*@NonNull FirestoreRecyclerOptions<Peluqueria> options*/) {
        //super(options);

    }

    /*@Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Peluqueria model) {
        holder.horario.setText(model.getHorario());
        holder.direccion.setText(model.getDireccion());
        holder.numeroTelefono.setText(model.getNumeroTelefono());
        holder.nombre.setText(model.getNombre());
    }*/

    /*@NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peluqueria, parent, false);
        return new ViewHolder(v);
    }^*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView horario;
        TextView direccion;
        TextView numeroTelefono;
        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            horario = itemView.findViewById(R.id.txtHorario);
            direccion = itemView.findViewById(R.id.txtDireccion);
            numeroTelefono = itemView.findViewById(R.id.txtNumTlf);
            nombre = itemView.findViewById(R.id.txtNombre);

        }
    }
}