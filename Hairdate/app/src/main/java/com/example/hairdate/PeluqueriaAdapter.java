package com.example.hairdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;


public class PeluqueriaAdapter extends FirestoreRecyclerAdapter<Peluqueria, PeluqueriaAdapter.ViewHolder> implements Serializable {

    private View.OnClickListener onClickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public PeluqueriaAdapter(@NonNull FirestoreRecyclerOptions<Peluqueria> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Peluqueria model) {
        holder.horario.setText(model.getHorario());
        holder.direccion.setText(model.getDireccion());
        holder.numeroTelefono.setText(model.getNumeroTelefono());
        holder.nombre.setText(model.getNombre());
        // Cargar la imagen utilizando Picasso
        model.imagenPerfilPelu(holder.itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la posición del adaptador
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Obtener la peluquería en esa posición
                    Peluqueria peluqueria = getItem(position);

                    // Crear un Bundle para pasar los datos como argumentos
                    Bundle args = new Bundle();
                    args.putString("direccion", peluqueria.getDireccion());
                    args.putString("horario", peluqueria.getHorario());
                    args.putString("numeroTelefono", peluqueria.getNumeroTelefono());
                    args.putString("nombre", peluqueria.getNombre());

                    // Navegar al siguiente fragmento y pasar los argumentos
                    Navigation.findNavController(v).navigate(R.id.action_principal_cliente_to_perfil_peluqueria, args);
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peluqueria, parent, false);
        return new ViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView horario;
        TextView direccion;
        TextView numeroTelefono;
        TextView nombre;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            horario = itemView.findViewById(R.id.txtHorario);
            direccion = itemView.findViewById(R.id.txtDireccion);
            numeroTelefono = itemView.findViewById(R.id.txtNumTlf);
            nombre = itemView.findViewById(R.id.txtNombre);
            imageView = itemView.findViewById(R.id.imgFoto);

        }
    }
}