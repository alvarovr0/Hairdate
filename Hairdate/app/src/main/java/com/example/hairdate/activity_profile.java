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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link activity_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class activity_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView profileImage;
    String emailActual;
    String ADondeVolver;
    private Button changeImageButton;
    private Button volverAtras;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public activity_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment activity_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static activity_profile newInstance(String param1, String param2) {
        activity_profile fragment = new activity_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_profile, container, false);
        profileImage = view.findViewById(R.id.profile_image);
        changeImageButton = view.findViewById(R.id.change_image_button);
        volverAtras = view.findViewById(R.id.btn_volver_activityProfile);

        getParentFragmentManager().setFragmentResultListener("fragmentVerObjeto", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                emailActual = result.getString("email");



            }
        });



        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email", emailActual);
                bundle.putString("ADondeVolver", ADondeVolver);
                getParentFragmentManager().setFragmentResult("menuPeluquero_to_activityProfile", bundle);
                // Aquí puedes abrir la galería o la cámara para que el usuario pueda seleccionar una nueva imagen de perfil
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Envía la información a menuPeluquero y vuelve a ese fragment
                Bundle bundle = new Bundle();
                bundle.putString("email", emailActual);
                getParentFragmentManager().setFragmentResult("menuPeluquero", bundle);
                Navigation.findNavController(view).navigate(R.id.action_activity_profile_to_menu_Peluquero);
            }
        });

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //firebaseStorage = FirebaseStorage.getInstance();
        //storageReference = firebaseStorage.getReference().child("profile_images");


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                subirImagenAFirebase(imageUri);
            }
        }
    }

    private void subirImagenAFirebase(Uri imageUri) {
        // Sube la imagen al almacenamiento de Firebase
        StorageReference fileRef = storageReference.child(emailActual + "_profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
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

    @Override
    public void onStart() {
        super.onStart();

        ADondeVolver = "Cliente";
        storageReference = FirebaseStorage.getInstance().getReference();
        // Recoge los datos que han sido traidos desde menuPeluquero/menuCliente
        getParentFragmentManager().setFragmentResultListener("menuPeluquero_to_activityProfile", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                emailActual = result.getString("email");
                ADondeVolver = result.getString("ADondeVolver");

                //Toast.makeText(getContext(), ADondeVolver, Toast.LENGTH_SHORT).show();

                // Muestra imagen si ya había alguna anteriormente
                StorageReference profileRef = storageReference.child(emailActual + "_profile.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        });
    }
}