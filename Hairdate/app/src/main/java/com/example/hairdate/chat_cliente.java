package com.example.hairdate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairdate.adapter.ChatAdapter;
import com.example.hairdate.model.ChatModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class chat_cliente extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button btn_volver;
    Button btn_mandarMensaje;
    EditText edTxt_correo, edTxt_mensaje;
    private FirebaseFirestore db;

    RecyclerView recyclerView;
    ChatAdapter adapter;

    public chat_cliente() {

    }

    public static chat_cliente newInstance(String param1, String param2) {
        chat_cliente fragment = new chat_cliente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_cliente, container, false);

        db = FirebaseFirestore.getInstance();

        edTxt_correo = view.findViewById(R.id.edTxt_correo_chatCliente);
        edTxt_mensaje = view.findViewById(R.id.edTxt_mensaje_chatCliente);
        btn_volver = view.findViewById(R.id.btn_volver_chat_cliente);
        btn_mandarMensaje = view.findViewById(R.id.btn_enviarMensaje_cliente);

        recyclerView = view.findViewById(R.id.recyclerView_mensajes_cliente);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Consultar todos los mensajes que hayan sido enviados o recibidos por el correo asociado a la cuenta iniciada sesión
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();



        Query query = db.collection("Chats").whereEqualTo("reciboEmail", email);

        FirestoreRecyclerOptions<ChatModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query, ChatModel.class).build();

        adapter = new ChatAdapter(firestoreRecyclerOptions);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        // Al pulsar el boton Volver, se vuelve al fragment menuCliente
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chat_cliente_to_principal_cliente);
            }
        });

        // Al pulsar el boton Enviar mensaje, se envia el mensaje escrito al correo introducido
        btn_mandarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeEnviar = edTxt_mensaje.getText().toString();
                String correoEnviar = edTxt_correo.getText().toString();
                if(edTxt_correo.getText().toString().isEmpty() || edTxt_mensaje.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Hay un campo vacio")
                            .setMessage("Antes de poder enviar un mensaje tienes que rellenar todos los campos")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }else {
                    subirMensajeAFirebase(email, correoEnviar, mensajeEnviar);
                }
            }
        });


        return view;
    }

    private void subirMensajeAFirebase(String envioEmail, String reciboEmail, String mensaje) {
        db = FirebaseFirestore.getInstance();
        Map<String, String> items = new HashMap<>();
        items.put("envioEmail", envioEmail);
        items.put("reciboEmail", reciboEmail);
        items.put("mensaje", mensaje);
        db.collection("Chats").document().set(items);
        Toast.makeText(getActivity(), "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
        edTxt_mensaje.setText("");
        edTxt_correo.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}