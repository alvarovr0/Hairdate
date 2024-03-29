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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hairdate.adapter.CitasAdapter;
import com.example.hairdate.model.Citas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class menu_Peluquero extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String nombreUsuario;
    String emailActual;
    boolean IrADetallerCitas;
    private TextView usuario;
    private Button btn_controlStock;
    private Button btn_cerrarSesion;
    private Button btn_gestionPeluqueros;
    private Button btn_gestionCitas;
    private Button btn_chat;
    private View view;

    ImageView profileImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseFirestore mFirestore;


    public menu_Peluquero() {

    }

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_peluquero, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        IrADetallerCitas = true;

        profileImage = view.findViewById(R.id.img_imagenPerfil);
        usuario = view.findViewById(R.id.txt_nombrePeluquero);
        btn_chat = view.findViewById(R.id.btn_chat_menuPeluquero);
        btn_controlStock = view.findViewById(R.id.btn_comprobarStock);
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        btn_gestionPeluqueros = view.findViewById(R.id.btn_gestionPeluqueros);
        btn_gestionCitas = view.findViewById(R.id.btn_comprobarCitas);

        storageReference = FirebaseStorage.getInstance().getReference();


        // Obtener el UID del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            Query queryUsuario = db.collection("Peluqueria").whereEqualTo("UID", uid);
            queryUsuario.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                emailActual = document.getString("email");
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
            }

        // Al pulsar en el nombre de usuario se envía a cambiar sus datos
        usuario.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_peluqueria_mod);
            }
        }));
        // Al pulsar la imagen, se te dirige a cambiarte la imagen de perfil
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAActivityProfile();
            }
        });

        btn_gestionCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_citasLista);
            }
        });

        // Cuando se pulsa el botón "Comprobar Stock" se cambia al fragment donde se puede comprobar el stock
        btn_controlStock.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Bundle bundle = new Bundle();
                bundle.putString("email", emailActual);
                getParentFragmentManager().setFragmentResult("fragmentVerObjeto", bundle);
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_fragment_ver_objeto);
            }
        }));

        btn_cerrarSesion.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_inicioSesion);
            }
        }));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // Consumir el evento del botón de retroceso
                    return true;
                }
                return false;
            }
        });

        // Al pulsar el boton de gestionar peluqueros, te lleva al fragment listaPeluqueros
        btn_gestionPeluqueros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_listaPeluqueros);
            }
        });

        // Al pulsar el boton de gestionar citas, te lleva al fragment citasLista
        btn_gestionCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_citasLista);
            }
        });

        // Al pulsar el boton de chat, te lleva al fragment chat
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_chat_peluquero);
            }
        });
        return view;
    }

    public void irAActivityProfile() {
        String volverAMenuPeluquero = "Peluquero";
        Bundle bundle = new Bundle();
        bundle.putString("email", emailActual);
        getParentFragmentManager().setFragmentResult("menuPeluquero_to_activityProfile", bundle);
        Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_activity_profile);
    }
}