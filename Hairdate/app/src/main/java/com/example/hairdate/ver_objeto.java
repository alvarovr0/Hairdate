package com.example.hairdate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ver_objeto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ver_objeto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView productoImage;
    int numProducto;
    Uri imageUri;
    String emailActual;
    private EditText tituloProducto;
    private EditText cantidadProducto;
    private Button botonIzquierda;
    private Button botonDerecha;
    private Button botonGuardar;
    private Button botonCancelar;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    public ver_objeto() {
        // Required empty public constructor
    }

    public static ver_objeto newInstance(String param1, String param2) {
        ver_objeto fragment = new ver_objeto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numProducto = 1;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_objeto, container, false);
        productoImage = view.findViewById(R.id.imagenProducto_activityProfile);
        tituloProducto = view.findViewById(R.id.edTxt_tituloProducto);
        cantidadProducto = view.findViewById(R.id.edTxt_cantidadProducto);
        botonGuardar = view.findViewById(R.id.btn_guardarCambios);
        botonCancelar = view.findViewById(R.id.btn_cancelarCambios);
        botonIzquierda = view.findViewById(R.id.btn_productoIzquierda);
        botonDerecha = view.findViewById(R.id.btn_productoDerecha);

        storageReference = FirebaseStorage.getInstance().getReference();

        getParentFragmentManager().setFragmentResultListener("fragmentVerObjeto", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                emailActual = result.getString("email");
                //Toast.makeText(view.getContext(), emailActual, Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("email", emailActual);
                getParentFragmentManager().setFragmentResult("menuPeluquero", bundle);

                // Carga la imagen del primer producto si ya existe
                StorageReference profileRef = storageReference.child(emailActual + "_producto" + numProducto + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUri = uri;
                        Picasso.get().load(uri).into(productoImage);
                    }
                });

                // Carga el nombre y la cantidad si esta en la base de datos
                documentReference = firestore.collection("productos").document(emailActual + "_producto" + numProducto);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tituloProducto.setText(documentSnapshot.getString("titulo"));
                        cantidadProducto.setText(documentSnapshot.getString("cantidad"));
                    }
                });
            }
        });


        firestore = FirebaseFirestore.getInstance();



        productoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes abrir la galería o la cámara para que el usuario pueda seleccionar una nueva imagen de perfil
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        botonIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pasa al producto anterior y se vuelven a cargar los datos
                numProducto--;
                StorageReference profileRef = storageReference.child(emailActual + "_producto" + numProducto + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUri = uri;
                        Picasso.get().load(uri).into(productoImage);
                    }
                });
                // Carga el nombre y la cantidad si esta en la base de datos
                documentReference = firestore.collection("productos").document(emailActual + "_producto" + numProducto);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tituloProducto.setText(documentSnapshot.getString("titulo"));
                        cantidadProducto.setText(documentSnapshot.getString("cantidad"));
                    }
                });
            }
        });
        botonDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pasa al siguiente producto y se vuelven a cargar los datos
                numProducto++;
                StorageReference profileRef = storageReference.child(emailActual + "_producto" + numProducto + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUri = uri;
                        Picasso.get().load(uri).into(productoImage);
                    }
                });
                // Carga el nombre y la cantidad si esta en la base de datos
                documentReference = firestore.collection("productos").document(emailActual + "_producto" + numProducto);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tituloProducto.setText(documentSnapshot.getString("titulo"));
                        cantidadProducto.setText(documentSnapshot.getString("cantidad"));
                    }
                });
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirImagenAFirebase(imageUri);
                subirTextoAFirebase();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_ver_objeto_to_menu_Peluquero);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(productoImage);
            }
        }
    }

    private void subirImagenAFirebase(Uri imageUri) {
        // Sube la imagen al almacenamiento de Firebase
        StorageReference fileRef = storageReference.child(emailActual + "_producto" + numProducto + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(productoImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.print("Error");
            }
        });
    }

    private void subirTextoAFirebase() {
        firestore = FirebaseFirestore.getInstance();
        Map<String, String> items = new HashMap<>();
        items.put("titulo", tituloProducto.getText().toString().trim());
        items.put("cantidad", cantidadProducto.getText().toString().trim());
        firestore.collection("productos").document(emailActual + "_producto" + numProducto).set(items);
    }
}