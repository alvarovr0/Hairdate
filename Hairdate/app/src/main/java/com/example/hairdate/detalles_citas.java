package com.example.hairdate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class detalles_citas extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button btnVolver;
    private Button btnBorrarCita;
    private String musicaJazz, musicaPop, musicaRap, musicaReggae, musicaReggaeton, musicaOtros;
    private String temaMascotas, temasSeries, temasCine, temasFutbol, temasPeliculas, temasOtros;
    private String horario, tipoCita, cliente;
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
        btnVolver = view.findViewById(R.id.btn_volver_detallesCitas);
        btnBorrarCita = view.findViewById(R.id.btn_borrarCita_detallesCitas);


        // Al pulsar el boton "Volver" se vuelve al menu_peluquero
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_detalles_citas_to_menu_Peluquero);
            }
        });

        // Al pulsar el boton "Borrar Cita" se borra la cita y se vuelve al menu_peluquero
        /*android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) menu_Peluquero.this.requireActivity());
        constructorDialogo.setMessage((CharSequence) "Horario: " + horarioCita).setCancelable(false).setPositiveButton((CharSequence) "Sí", (android.content.DialogInterface.OnClickListener) (new android.content.DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogo, int id) {

                // Borra la cita de la base de datos
                db.collection("Citas")
                        .whereEqualTo("Fecha_Hora", horarioCita)
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
                                                    Toast.makeText(view.getContext(), "Cancelada cita: " + horarioCita, Toast.LENGTH_LONG).show();
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
        })).setNegativeButton((CharSequence) "No", (android.content.DialogInterface.OnClickListener) null);
        android.app.AlertDialog alerta = constructorDialogo.create();
        alerta.setTitle((CharSequence) "¿Quieres cancelar esta cita?");
        alerta.show();
    }*/

        return view;
    }
}