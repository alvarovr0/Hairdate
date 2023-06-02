package com.example.hairdate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairdate.R;
import com.example.hairdate.model.ChatModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatModel model) {
        holder.mensaje.setText(model.getMensaje());
        holder.envioEmail.setText(model.getEnvioEmail());
        //holder.reciboEmail.setText(model.getReciboEmail());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mensaje_single, parent, false);

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
        TextView mensaje;
        TextView envioEmail;
        //TextView reciboEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.txt_mensaje);
            envioEmail = itemView.findViewById(R.id.txt_envio);
            //reciboEmail = itemView.findViewById(R.id.txt_recibo);
        }
    }
}