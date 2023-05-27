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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Context;
import android.content.DialogInterface;



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

    private FirebaseStorage firebaseStorage;

    private Peluqueria selectedPeluqueria; // Variable para almacenar la peluquería seleccionada

    FirebaseFirestore db;
    RecyclerView recyclerView;
    PeluqueriaAdapter adapter;
    //private ArrayList<Peluqueria> peluquerias;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal_cliente, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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


        /*adapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Te permite cancelar citas dando click en la basura
                String horarioCita = ((TextView) recyclerView.findViewHolderForAdapterPosition(recyclerView.getChildAdapterPosition(view)).itemView.findViewById(R.id.txt_horario)).getText().toString();

                recyclerView.findViewHolderForAdapterPosition(recyclerView.getChildAdapterPosition(view)).itemView.findViewById(R.id.imv_trashcan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) principal_cliente.this.requireActivity());
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
                    }
                });
            }
        });*/





        return view;
    }



    /*public void onItemClick(Peluqueria peluqueria) {
        // Aquí rediriges a otro fragmento o realizas la acción que desees
        Toast.makeText(getActivity(), "Se te va a redirigir a la peluería: " + peluqueria.getNombre(), Toast.LENGTH_SHORT).show();

        Query query = db.collection("Peluqueria").whereEqualTo("direccion", peluqueria.getDireccion());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        // Aquí tienes la ID del documento
                        Log.d("Firestore", "ID del documento: " + documentId);

                        Bundle result = new Bundle();
                        result.putString("bundleKey",documentId);

                        //Imprimir traza en el logCat
                        Log.d("UID", String.valueOf(result));
                        getParentFragmentManager().setFragmentResult("requestKey", result);

                        Navigation.findNavController(getView()).navigate(R.id.action_principal_cliente_to_perfil_peluqueria);
                    } else {
                        // No se encontraron resultados
                        Log.d("Firestore", "No se encontraron resultados para la consulta.");
                    }
                } else {
                    // Error al ejecutar la consulta
                    Log.e("Firestore", "Error al ejecutar la consulta: " + task.getException().getMessage());
                }
            }
        });

    }*/

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
