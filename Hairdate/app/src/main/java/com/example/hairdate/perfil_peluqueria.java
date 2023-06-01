package com.example.hairdate;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perfil_peluqueria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfil_peluqueria extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;

    private TextView direccionTextView;
    private TextView horarioTextView;
    private TextView numeroTelefonoTextView;
    private TextView nombreTextView;
    private TextView serviciosTextView;
    private TextView tipoTextView;
    private ImageButton btn_fav;
    private boolean fav_marc;
    private String peluqueriaId;
    private String userId;
    private Button btn_pedirCita;
    private Button btn_verProductos;
    String peluqueriaUid;

    RecyclerView recyclerView;
    PeluqueroAdapter adapter;
    private Button btn_volverAtras;

    private StorageReference storageReference;
    ImageView profileImage;

    public perfil_peluqueria() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfil_peluqueria.
     */
    // TODO: Rename and change types and number of parameters
    public static perfil_peluqueria newInstance(String param1, String param2) {
        perfil_peluqueria fragment = new perfil_peluqueria();
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

        View rootView = inflater.inflate(R.layout.fragment_perfil_peluqueria, container, false);

        db = FirebaseFirestore.getInstance();

        direccionTextView = rootView.findViewById(R.id.txtDireccion);
        horarioTextView = rootView.findViewById(R.id.txtHorario);
        numeroTelefonoTextView = rootView.findViewById(R.id.txtNumTlf);
        nombreTextView = rootView.findViewById(R.id.txtNombre);
        serviciosTextView = rootView.findViewById(R.id.txtServicios);
        tipoTextView = rootView.findViewById(R.id.txtTipo);
        btn_volverAtras = rootView.findViewById(R.id.btn_cancelar_perfilPeluqueria);
        btn_verProductos = rootView.findViewById(R.id.btn_verProductos);

        // Obtener los argumentos pasados desde el fragmento anterior
        if (getArguments() != null) {
            String direccion = getArguments().getString("direccion");
            String horario = getArguments().getString("horario");
            String numeroTelefono = getArguments().getString("numeroTelefono");
            String nombre = getArguments().getString("nombre");
            Query peluqueriaQuery = db.collection("Peluqueria").whereEqualTo("direccion", direccion);
            peluqueriaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    String servicios = "";
                    String tipo = "";
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String corte = document.getString("corte");
                                String corteTinte = document.getString("corte_tinte");
                                String tinte = document.getString("tinte");
                                String peinado = document.getString("peinado");
                                tipo = document.getString("tipo");
                                if(corte.equals("si")){
                                    servicios += "corte ";
                                }
                                if(corteTinte.equals("si")){
                                    servicios += "corte + tinte ";
                                }
                                if(tinte.equals("si")){
                                    servicios += "tinte ";
                                }
                                if(peinado.equals("si")){
                                    servicios += "peinado ";
                                }
                            }
                        }
                    }
                    serviciosTextView.setText(servicios);
                    tipoTextView.setText(tipo);
                }
            });

            // Mostrar los datos en los TextView correspondientes
            direccionTextView.setText(direccion);
            horarioTextView.setText(horario);
            numeroTelefonoTextView.setText(numeroTelefono);
            nombreTextView.setText(nombre);
            verificarFavorito();

        }
        //Aquí se añade la función del cambio de estado del botón de favoritos
        this.btn_fav = (ImageButton) rootView.findViewById(R.id.imagen_fav);
        this.btn_fav.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (fav_marc) {
                    btn_fav.setImageResource(R.drawable.favorito_no);
                    Toast.makeText(getActivity(), "Peluquería eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    eliminarDeFavoritos();
                    fav_marc = false;
                    // Si el corazón está sin marcar, lo cambia a rojo y se empieza añade a favoritos
                } else {
                    btn_fav.setImageResource(R.drawable.favorito_si);
                    favoritos();
                    Toast.makeText(getActivity(), "Peluquería añadida a favoritos", Toast.LENGTH_SHORT).show();
                    fav_marc = true;
                }
            }
        }));

        // Te devuelve al fragment de principalCliente
        btn_volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverAtras(rootView);
            }
        });
        btn_verProductos.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference peluqueriaRef = db.collection("Peluqueria");

                Query query = peluqueriaRef.whereEqualTo("direccion", direccionTextView.getText().toString());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String email = documentSnapshot.getString("email");
                                Bundle bundle = new Bundle();
                                bundle.putString("email", email);
                                getParentFragmentManager().setFragmentResult("correo", bundle);
                                Navigation.findNavController(getView()).navigate(R.id.action_perfil_peluqueria_to_fragment_verProductos_Cliente);
                            } else {
                                Toast.makeText(getContext(), "No se encontró el documento.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }));

        imagenPerfilPelu(rootView);
        pedirCita(rootView);
        listaPeluqueros(rootView);

        return rootView;
    }

    // Se ejecuta al pulsar el botón de "Volver". Te devuelve al fragment de principalCliente
    public void volverAtras(View rootView) {
        Navigation.findNavController(rootView).navigate(R.id.action_perfil_peluqueria_to_principal_cliente);
    }

    public void favoritos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String direccionPeluqueria = direccionTextView.getText().toString();
            String horarioPeluqueria = horarioTextView.getText().toString();
            String nombrePeluqueria = nombreTextView.getText().toString();
            String numeroTelefonoPeluqueria = numeroTelefonoTextView.getText().toString();

            // Realiza la consulta para obtener la peluquería con la dirección dada
            db.collection("Peluqueria")
                    .whereEqualTo("direccion", direccionPeluqueria)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Obtiene el primer documento de la consulta
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String peluqueriaId = documentSnapshot.getId();

                                // Crea un mapa con los datos de la peluquería favorita
                                Map<String, Object> favorito = new HashMap<>();
                                favorito.put("direccion", direccionPeluqueria);
                                favorito.put("horario", horarioPeluqueria);
                                favorito.put("nombre", nombrePeluqueria);
                                favorito.put("numeroTelefono", numeroTelefonoPeluqueria);
                                favorito.put("UID", userId);

                                // Guarda los datos en la colección "Favoritos" con el ID del usuario y el ID de la peluquería como nombre del documento
                                db.collection("Favoritos")
                                        .document(userId)
                                        .collection("Peluquerias")
                                        .document(peluqueriaId)
                                        .set(favorito)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // La peluquería se guardó como favorita exitosamente
                                                Toast.makeText(getActivity(), "Peluquería añadida a favoritos", Toast.LENGTH_SHORT).show();
                                                verificarFavorito();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Ocurrió un error al guardar la peluquería como favorita
                                                Toast.makeText(getActivity(), "Error al añadir la peluquería a favoritos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), "ERROR: No se encontró ninguna peluquería con esa dirección", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "ERROR: Ocurrió un error al obtener la ID de la peluquería", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void verificarFavorito() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String direccionPeluqueria = direccionTextView.getText().toString();

            db.collection("Favoritos")
                    .document(userId)
                    .collection("Peluquerias")
                    .whereEqualTo("direccion", direccionPeluqueria)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean esFavorito = !queryDocumentSnapshots.isEmpty();
                            actualizarEstadoFavorito(esFavorito);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "ERROR: Ocurrió un error al verificar el estado de favorito", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void actualizarEstadoFavorito(boolean esFavorito) {
        fav_marc = esFavorito;
        if (esFavorito) {
            btn_fav.setImageResource(R.drawable.favorito_si);
        } else {
            btn_fav.setImageResource(R.drawable.favorito_no);
        }
    }


    private void eliminarDeFavoritos() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String direccionPeluqueria = direccionTextView.getText().toString();

            db.collection("Favoritos")
                    .document(userId)
                    .collection("Peluquerias")
                    .whereEqualTo("direccion", direccionPeluqueria)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Obtiene el primer documento de la consulta
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String peluqueriaId = documentSnapshot.getId();

                                // Elimina el documento de la colección "Peluquerias"
                                db.collection("Favoritos")
                                        .document(userId)
                                        .collection("Peluquerias")
                                        .document(peluqueriaId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // La peluquería se eliminó de favoritos exitosamente
                                                Toast.makeText(getActivity(), "Peluquería eliminada de favoritos", Toast.LENGTH_SHORT).show();
                                                verificarFavorito();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "ERROR: Ocurrió un error al eliminar la peluquería de favoritos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), "ERROR: La peluquería no está en la lista de favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "ERROR: Ocurrió un error al obtener la información de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void imagenPerfilPelu(View rootView) {
        profileImage = rootView.findViewById(R.id.imagePeluqueria);
        storageReference = FirebaseStorage.getInstance().getReference();
        // Muestra una imagen
        StorageReference profileRef = storageReference.child(numeroTelefonoTextView.getText().toString() + "_pelu.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }

    public void pedirCita(View view) {
        btn_pedirCita = view.findViewById(R.id.btn_pedirCita);
        btn_pedirCita.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Query peluqueriaQuery = db.collection("Peluqueria").whereEqualTo("direccion", direccionTextView.getText().toString());
                peluqueriaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String UID = "";
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    UID = document.getString("UID");
                                }
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("bundlekey", UID);
                        getParentFragmentManager().setFragmentResult("bundlekey", bundle);
                    }
                });
                Navigation.findNavController(view).navigate(R.id.action_perfil_peluqueria_to_solicitar_cita);
            }
        }));
    }

    public void listaPeluqueros(View rootView) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        recyclerView = rootView.findViewById(R.id.recyclerViewPeluqueros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String direccionPeluqueria = direccionTextView.getText().toString();

            // Realiza la consulta para obtener la peluquería con la dirección dada
            db.collection("Peluqueria")
                    .whereEqualTo("direccion", direccionPeluqueria)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Obtiene el primer documento de la consulta
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String peluqueriaId = documentSnapshot.getId();

                                // Consultar los peluqueros de la peluquería actual desde la anterior UID
                                Query queryPeluqueros = db.collection("Peluquero")
                                        .whereEqualTo("peluqueria", "/Peluqueria/" + peluqueriaId); // Filtrar por el UID de la peluquería

                                FirestoreRecyclerOptions<Peluquero> firestoreRecyclerOptionsPeluqueros = new FirestoreRecyclerOptions.Builder<Peluquero>()
                                        .setQuery(queryPeluqueros, Peluquero.class)
                                        .build();

                                adapter = new PeluqueroAdapter(firestoreRecyclerOptionsPeluqueros);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(getActivity(), "ERROR: No se encontró ninguna peluquería con esa dirección", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "ERROR: Ocurrió un error al obtener la ID de la peluquería", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}