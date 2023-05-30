package com.example.hairdate;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


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

    String nombreUsuario;
    String emailActual;

    FirebaseFirestore db;
    //String emailActual;
    TextView usuario;
    RecyclerView recyclerView;
    RecyclerView recyclerViewFav;
    PeluqueriaAdapter adapter;
    PeluqueriaAdapter adapterFav;
    ImageView profileImage;
    View view;
    private StorageReference storageReference;
    private Button btn_cerrarSesion;
    //private TextView usuario;

    public principal_cliente() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_principal_cliente, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();

        profileImage = view.findViewById(R.id.img_imagenPerfilCliente);
        usuario = view.findViewById(R.id.txt_nombreCliente);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuario = view.findViewById(R.id.txt_nombreCliente);
        profileImage = view.findViewById(R.id.img_imagenPerfilCliente);
        if(profileImage == null){
            profileImage.setImageResource(R.drawable.user_profile);
        }
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

        // Al pulsar el nombre, se te dirige a cambiarte la imagen de perfil
        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moverAActivityProfile();
            }
        });
        // Al pulsar la imagen, se te dirige a cambiarte la imagen de perfil
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moverAActivityProfile();
            }
        });

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

            // Obtiene el email actual
            Query queryEmail = db.collection("Cliente").whereEqualTo("UID", uid);
            queryEmail.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                emailActual = document.getString("email");
                                Toast.makeText(getActivity(), "emailActual es " + emailActual, Toast.LENGTH_SHORT).show();

                                // Coloca el email como nombre de usuario
                                usuario.setText(document.getString("usuario"));
                                // Si existe una imagen, la coloca como imagen de perfil
                                StorageReference profileRef = storageReference.child(emailActual + "_profile.jpg");
                                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).into(profileImage);
                                    }
                                });
                            }

                        }
                    });

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
                    getParentFragmentManager().setFragmentResult("resquestKey", args);
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

        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        btn_cerrarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_principal_cliente_to_inicioSesion);
            }
        }));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapterFav !=null){
            adapterFav.startListening();
        }
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
    if(adapterFav !=null){
        adapterFav.stopListening();
    }
        adapter.stopListening();
    }

    // Método para mostrar el Texto cuando no hay peluquerías favoritas
    private void mostrarTextoNoFavoritas(boolean mostrar) {
        TextView textNoFav = getView().findViewById(R.id.textNoFav);
        if (mostrar) {
            textNoFav.setVisibility(View.VISIBLE);
            recyclerViewFav.setVisibility(View.GONE);
        } else {
            textNoFav.setVisibility(View.GONE);
            recyclerViewFav.setVisibility(View.VISIBLE);
        }
    }

    public void cerrarSesion(View view){
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        btn_cerrarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_principal_cliente_to_inicioSesion);
            }
        }));
    }

    public void moverAActivityProfile() {
        Bundle bundle = new Bundle();
        bundle.putString("email", emailActual);
        getParentFragmentManager().setFragmentResult("menuPeluquero_to_activityProfile", bundle);
        Navigation.findNavController(view).navigate(R.id.action_menu_cliente_to_activity_profile);
    }
}
