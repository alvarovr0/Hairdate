package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentKt;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menu_Peluquero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_Peluquero extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView usuario;
    private Button btn_controlStock;
    private Button btn_cerrarSesion;

    ImageView profileImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    public menu_Peluquero() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu_Peluquero.
     */
    // TODO: Rename and change types and number of parameters
    public static menu_Peluquero newInstance(String param1, String param2) {
        menu_Peluquero fragment = new menu_Peluquero();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_peluquero, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        profileImage = view.findViewById(R.id.img_imagenPerfil);


        usuario = view.findViewById(R.id.txt_nombrePeluquero);
        btn_controlStock = view.findViewById(R.id.btn_comprobarStock);
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                String result = bundle.getString("bundleKey");
                Query query = db.collection("Peluquero").whereEqualTo("UID", result);
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                // Iterar a través de los documentos
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    // Obtener el valor en concreto que necesitas
                                    String nombreUsuario = document.getString("nombre");
                                    usuario.setText(nombreUsuario);
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
        usuario.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_activity_profile);
            }
        }));

        // Cuando se pulsa el botón "Comprobar Stock" se cambia al fragment donde se puede comprobar el stock
        btn_controlStock.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_fragment_ver_objeto);
            }
        }));

        btn_cerrarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_inicioSesion_Peluquero);
            }
        }));

        return view;
    }

}