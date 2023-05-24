package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


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
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private PeluqueriaAdapter adapter;
    private ArrayList<Peluqueria> peluquerias;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_principal_cliente, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        Query peluqueriasQuery = db.collection("Peluqueria");

        peluqueriasQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                peluquerias = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    Peluqueria peluqueria = documentSnapshot.toObject(Peluqueria.class);
                    peluquerias.add(peluqueria);
                }

                adapter = new PeluqueriaAdapter(peluquerias);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("MyApp", "Error al obtener los documentos: ", e);
            }
        });

        return rootView;
    }
}
