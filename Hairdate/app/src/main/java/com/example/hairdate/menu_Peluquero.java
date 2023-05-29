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

    String nombreUsuario;
    String emailActual;
    boolean IrADetallerCitas;
    private TextView usuario;
    private Button btn_controlStock;
    private Button btn_cerrarSesion;
    private View view;

    ImageView profileImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    RecyclerView mRecycler;
    CitasAdapter mAdapter;
    FirebaseFirestore mFirestore;


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
        btn_controlStock = view.findViewById(R.id.btn_comprobarStock);
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = view.findViewById(R.id.rvw_PeluqueroCitas);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = mFirestore.collection("Citas");

        FirestoreRecyclerOptions<Citas> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Citas>().setQuery(query, Citas.class).build();

        mAdapter = new CitasAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        // Permite hacer click en las citas y te envia a detalles_citas
        mAdapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String horarioCita = ((TextView) mRecycler.findViewHolderForAdapterPosition(mRecycler.getChildAdapterPosition(view)).itemView.findViewById(R.id.txt_horario)).getText().toString();
                // Envía el email y el horario a detalles_citas, para que este pueda cargar la cita correspondiente
                Bundle result = new Bundle();
                result.putString("email", emailActual);
                result.putString("horario", horarioCita);
                getParentFragmentManager().setFragmentResult("menuPeluquero_to_detallesCitas", result);
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_detalles_citas);
            }
        });




        // Al pulsar en el nombre de usuario se envía a cambiarse la imagen de perfil
        usuario.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                String volverAMenuPeluquero = "Peluquero";
                Bundle bundle = new Bundle();
                bundle.putString("email", emailActual);
                bundle.putString("ADondeVolver", volverAMenuPeluquero);
                getParentFragmentManager().setFragmentResult("menuPeluquero_to_activityProfile", bundle);
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_activity_profile);
            }
        }));

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();

        storageReference = FirebaseStorage.getInstance().getReference();
        // Recoge la informacion importante que se mande desde otros fragments cada vez que se abre este fragment
        getParentFragmentManager().setFragmentResultListener("menuPeluquero", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                emailActual = result.getString("email");
                // Coloca el email como nombre de usuario
                usuario.setText(emailActual);
                // Si existe una imagen, la coloca como imagen de perfil
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

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}