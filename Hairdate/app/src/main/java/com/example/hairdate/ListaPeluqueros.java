package com.example.hairdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaPeluqueros#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPeluqueros extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;
    RecyclerView recyclerViewPeluqueros;
    PeluqueroAdapter adapter;
    StorageReference storageReference;
    RoundedImageView profileImage;
    String numeroTelefono;
    TextView usuario;

    private Button btn_addPeluquero;

    public ListaPeluqueros() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaPeluqueros.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaPeluqueros newInstance(String param1, String param2) {
        ListaPeluqueros fragment = new ListaPeluqueros();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_peluqueros, container, false);

        db = FirebaseFirestore.getInstance();
        usuario = view.findViewById(R.id.txtNombrePeluquero);
        profileImage = view.findViewById(R.id.img_imagenPerfilCliente);
        if (profileImage == null) {
            profileImage.setImageResource(R.drawable.user_profile);
        }
        recyclerViewPeluqueros = view.findViewById(R.id.recyclerViewPeluqueros);
        recyclerViewPeluqueros.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Consulta los peluqueros que tienen una referencia a la peluquería especificada
            Query query = db.collection("Peluquero").whereEqualTo("peluqueria", uid);
            FirestoreRecyclerOptions<Peluquero> firestoreRecyclerOptionsFav = new FirestoreRecyclerOptions.Builder<Peluquero>().setQuery(query, Peluquero.class).build();
            adapter = new PeluqueroAdapter(firestoreRecyclerOptionsFav);
            adapter.notifyDataSetChanged();
            recyclerViewPeluqueros.setAdapter(adapter);

            query.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                adapter.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        int position = recyclerViewPeluqueros.getChildAdapterPosition(view);
                                        Peluquero peluquero = adapter.getItem(position);

                                        // Navegar al siguiente fragmento y pasar los datos como argumentos
                                        Bundle args = new Bundle();
                                        args.putString("especialidad", peluquero.getEspecialidad());
                                        args.putString("horario", peluquero.getHorario());
                                        args.putString("nombre", peluquero.getNombre());
                                        Navigation.findNavController(view).navigate(R.id.action_listaPeluqueros_to_peluquero_detalle, args);
                                    }
                                });

                            }
                        }
                    });
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes abrir la galería o la cámara para que el usuario pueda seleccionar una nueva imagen de perfil
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                subirImagenAFirebase(imageUri);
            }
        }
    }

    private void subirImagenAFirebase(Uri imageUri) {
        storageReference = FirebaseStorage.getInstance().getReference();
        // Recoge los datos que han sido traidos desde menuPeluquero/menuCliente
        getParentFragmentManager().setFragmentResultListener("menuPeluquero", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                numeroTelefono = result.getString("numeroTelefono");

                // Muestra imagen si ya había alguna anteriormente
                StorageReference profileRef = storageReference.child(numeroTelefono + "_profile.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        });

        // Sube la imagen al almacenamiento de Firebase
        profileImage = getView().findViewById(R.id.imagePeluqueria);
        storageReference = FirebaseStorage.getInstance().getReference();
        // Muestra una imagen
        StorageReference profileRef = storageReference.child(numeroTelefono + "_pelu.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }

    public void addPeluquero(View view){
        btn_addPeluquero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AlertDialog dialog = builder.create();
                dialog.show();
                Navigation.findNavController(view).navigate(R.id.action_listaPeluqueros_to_peluqueroAgregar);
            }
        });
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