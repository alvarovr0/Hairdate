package com.example.hairdate;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class detalles_citas extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button btnVolver;
    private Button btnBorrarCita;
    private String emailActual, horarioActual;
    private TextView musicaJazz, musicaPop, musicaRap, musicaReggae, musicaReggaeton, musicaOtros;
    private TextView temasMascotas, temasSeries, temasCine, temasFutbol, temasPoliticas, temasOtros;
    private TextView horario, tipoCita, cliente;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    public detalles_citas() {

    }

    public static detalles_citas newInstance(String param1, String param2) {
        detalles_citas fragment = new detalles_citas();
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
        View view = inflater.inflate(R.layout.fragment_detalles_citas, container, false);
        musicaJazz = view.findViewById(R.id.txt_jazz_detallesCitas);
        musicaPop = view.findViewById(R.id.txt_pop_detallesCitas);
        musicaRap = view.findViewById(R.id.txt_rap_detallesCitas);
        musicaReggae = view.findViewById(R.id.txt_reggae_detallesCitas);
        musicaReggaeton = view.findViewById(R.id.txt_reggaeton_detallesCitas);
        musicaOtros = view.findViewById(R.id.txt_musicaOtros_detallesCitas);
        temasMascotas = view.findViewById(R.id.txt_mascotas_detallesCitas);
        temasCine = view.findViewById(R.id.txt_cine_detallesCitas);
        temasFutbol = view.findViewById(R.id.txt_futbol_detallesCitas);
        temasSeries = view.findViewById(R.id.txt_series_detallesCitas);
        temasOtros = view.findViewById(R.id.txt_temasOtros_detallesCitas);
        temasPoliticas = view.findViewById(R.id.txt_politica_detallesCitas);
        horario = view.findViewById(R.id.txt_horario_detallesCitas);
        tipoCita = view.findViewById(R.id.txt_tipoCita_detallesCitas);
        cliente = view.findViewById(R.id.txt_cliente_detallesCitas);

        btnVolver = view.findViewById(R.id.btn_volver_detallesCitas);
        btnBorrarCita = view.findViewById(R.id.btn_borrarCita_detallesCitas);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Recoge los datos que se han mandado desde menu_peluquero, los cuales son necesarios para conectarse a la base de datos correcta
        getParentFragmentManager().setFragmentResultListener("menuPeluquero_to_detallesCitas", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                emailActual = result.getString("email");
                horarioActual = result.getString("horario");

                // Se conecta a la base de datos y obtiene los valores correspondientes a dicha cita
                Query query = db.collection("Citas").whereEqualTo("Fecha_Hora", horarioActual);
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                // Iterar a través de los documentos
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    // Obtener el valor en concreto que necesitas
                                    musicaJazz.setText(document.getString("Musica_Jazz"));
                                    musicaPop.setText(document.getString("Musica_Pop"));
                                    musicaRap.setText(document.getString("Musica_Rap"));
                                    musicaReggae.setText(document.getString("Musica_Reggae"));
                                    musicaReggaeton.setText(document.getString("Musica_Reggaeton"));
                                    musicaOtros.setText(document.getString("Musica_Otro"));
                                    temasCine.setText(document.getString("Tema_cine"));
                                    temasMascotas.setText(document.getString("Tema_Mascotas"));
                                    temasSeries.setText(document.getString("Tema_Series"));
                                    temasPoliticas.setText(document.getString("Tema_politica"));
                                    temasFutbol.setText(document.getString("Tema_futbol"));
                                    horario.setText(document.getString("Fecha_Hora"));
                                    tipoCita.setText(document.getString("Servicio"));

                                    // Busca el nombre y del usuario que ha sacado cita y lo muestra
                                    String UID_necesario = document.getString("UID");
                                    Query query2 = db.collection("Cliente").whereEqualTo("UID", UID_necesario);
                                    query2.get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (DocumentSnapshot document2 : queryDocumentSnapshots.getDocuments()) {
                                                                cliente.setText(document2.getString("nombre") + " - " + document2.getString("email"));
                                                            }
                                                        }
                                                    });

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("MyApp", "Error al obtener los documentos: ", e);
                            }
                        });
            }
        });

        // Al pulsar el boton "Volver" se vuelve al menu_peluquero
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverAtras(view);
            }
        });

        // Al pulsar el boton "Borrar Cita" se borra la cita y se vuelve al menu_peluquero
        btnBorrarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) detalles_citas.this.requireActivity());
                constructorDialogo.setMessage((CharSequence) "Horario: " + horarioActual).setCancelable(false).setPositiveButton((CharSequence) "Sí", (DialogInterface.OnClickListener) (new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {

                        // Borra la cita de la base de datos
                        db.collection("Citas")
                                .whereEqualTo("Fecha_Hora", horarioActual)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Citas")
                                                    .document(documentID)
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // Si se ha podido borrar muestra el siguiente mensaje
                                                            Toast.makeText(view.getContext(), "Cancelada cita: " + horarioActual, Toast.LENGTH_LONG).show();

                                                            volverAtras(view);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Si no se ha podido borrar muestra el siguiente mensaje
                                                            Toast.makeText(view.getContext(), "Lo sentimos, no se pudo cancelar cita", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                })).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) null);
                android.app.AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence) "¿Quieres cancelar esta cita?");
                alerta.show();
            }
        });

        return view;
    }

    public void volverAtras(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("email", emailActual);
        getParentFragmentManager().setFragmentResult("menuPeluquero", bundle);
        Navigation.findNavController(view).navigate(R.id.action_detalles_citas_to_menu_Peluquero);
    }
}