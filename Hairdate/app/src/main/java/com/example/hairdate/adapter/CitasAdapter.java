package com.example.hairdate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairdate.R;
import com.example.hairdate.model.Citas;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CitasAdapter extends FirestoreRecyclerAdapter<Citas, CitasAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    public CitasAdapter(@NonNull FirestoreRecyclerOptions<Citas> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Citas model) {
        holder.horario.setText(model.getFecha_Hora());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_peluquerocitas_single, parent, false);

        v.setOnClickListener(this);

        return new ViewHolder(v);
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

    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView horario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            horario = itemView.findViewById(R.id.txt_horario);
        }
    }
}
