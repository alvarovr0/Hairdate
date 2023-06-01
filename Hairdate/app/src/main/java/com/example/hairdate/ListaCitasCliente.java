package com.example.hairdate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaCitasCliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaCitasCliente extends Fragment {

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

    public ListaCitasCliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaCitasCliente.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaCitasCliente newInstance(String param1, String param2) {
        ListaCitasCliente fragment = new ListaCitasCliente();
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
        View view = inflater.inflate(R.layout.fragment_lista_citas_cliente, container, false);

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
}