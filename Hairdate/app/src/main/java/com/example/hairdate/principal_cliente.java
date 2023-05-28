package com.example.hairdate;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Context;
import android.content.DialogInterface;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link principal_cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class principal_cliente extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;
    RecyclerView recyclerView;
    RecyclerView recyclerViewFav;
    PeluqueriaAdapter adapter;
    PeluqueriaAdapter adapterFav;

    public principal_cliente() {
        // Required empty public constructor
    }

    @NonNull
    public static principal_cliente newInstance(String param1, String param2) {
        principal_cliente fragment = new principal_cliente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal_cliente, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewFav = view.findViewById(R.id.recyclerViewFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Consultar todas las peluquerías
        Query query = db.collection("Peluqueria");

        FirestoreRecyclerOptions<Peluqueria> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Peluqueria>().setQuery(query, Peluqueria.class).build();

        adapter = new PeluqueriaAdapter(firestoreRecyclerOptions);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                Peluqueria peluqueria = adapter.getItem(position);

                // Navegar al siguiente fragmento y pasar los datos como argumentos
                Bundle args = new Bundle();
                args.putString("direccion", peluqueria.getDireccion());
                args.putString("horario", peluqueria.getHorario());
                args.putString("numeroTelefono", peluqueria.getNumeroTelefono());
                args.putString("nombre", peluqueria.getNombre());

                Navigation.findNavController(view).navigate(R.id.action_principal_cliente_to_perfil_peluqueria, args);
            }
        });

        // Obtener el UID del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Consultar las peluquerías favoritas del usuario
            Query queryFav = db.collection("Favoritos").document(uid).collection("Peluquerias");
            FirestoreRecyclerOptions<Peluqueria> firestoreRecyclerOptionsFav = new FirestoreRecyclerOptions.Builder<Peluqueria>().setQuery(queryFav, Peluqueria.class).build();

            adapterFav = new PeluqueriaAdapter(firestoreRecyclerOptionsFav);
            adapterFav.notifyDataSetChanged();
            recyclerViewFav.setAdapter(adapterFav);

            adapterFav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = recyclerViewFav.getChildAdapterPosition(view);
                    Peluqueria peluqueria = adapterFav.getItem(position);

                    // Navegar al siguiente fragmento y pasar los datos como argumentos
                    Bundle args = new Bundle();
                    args.putString("direccion", peluqueria.getDireccion());
                    args.putString("horario", peluqueria.getHorario());
                    args.putString("numeroTelefono", peluqueria.getNumeroTelefono());
                    args.putString("nombre", peluqueria.getNombre());

                    Navigation.findNavController(view).navigate(R.id.action_principal_cliente_to_perfil_peluqueria, args);
                }
            });

            // Verificar si hay peluquerías favoritas
            queryFav.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No hay peluquerías favoritas, mostrar el TextView correspondiente
                            mostrarTextoNoFavoritas(true);
                        } else {
                            // Hay peluquerías favoritas, ocultar el TextView correspondiente
                            mostrarTextoNoFavoritas(false);
                        }
                    } else {
                        // Error al obtener las peluquerías favoritas, mostrar un mensaje de error o tomar medidas adecuadas
                        Toast.makeText(getActivity(), "Error al obtener las peluquerías favoritas", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapterFav.startListening();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapterFav.stopListening();
        adapter.stopListening();
    }

    // Método para mostrar el Texto cuando no hay peluquerías favoritas
    private void mostrarTextoNoFavoritas(boolean mostrar) {
        TextView textNoFav = getView().findViewById(R.id.textNoFav);
        if (mostrar) {
            textNoFav.setVisibility(View.VISIBLE);
        } else {
            textNoFav.setVisibility(View.GONE);
        }
    }

}
