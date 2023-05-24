package com.example.hairdate;

<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
import static android.content.ContentValues.TAG;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.DialogInterface;
=======
import android.net.Uri;
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentKt;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.text.InputType;
=======
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

>>>>>>> Stashed changes
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
import android.widget.EditText;
=======
import android.widget.ImageView;
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
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
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
=======
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
=======
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
>>>>>>> Stashed changes

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link principal_cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
public class menu_cliente extends Fragment{
=======
public class principal_cliente extends Fragment {
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
=======
public class principal_cliente extends Fragment {
>>>>>>> Stashed changes

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
    private String emailActual;
    private String nombreUsuario;
    ImageView profileImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java

    public menu_cliente() {
=======
    public principal_cliente() {
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
=======
    public principal_cliente() {
>>>>>>> Stashed changes
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu_cliente.
     */
    // TODO: Rename and change types and number of parameters
    public static principal_cliente newInstance(String param1, String param2) {
        principal_cliente fragment = new principal_cliente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< Updated upstream

=======
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
>>>>>>> Stashed changes
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_cliente, container, false);
=======
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_cliente, container, false);
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuario = view.findViewById(R.id.txt_nombreCliente);
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        getParentFragmentManager().setFragmentResultListener("menuPeluquero", this, new FragmentResultListener() {
=======
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_cliente, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuario = view.findViewById(R.id.txt_nombrePeluquero);
        btn_cerrarSesion = view.findViewById(R.id.btn_cerrarSesion);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
>>>>>>> Stashed changes
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                String result = bundle.getString("bundleKey");
<<<<<<< Updated upstream
<<<<<<< Updated upstream:Hairdate/app/src/main/java/com/example/hairdate/menu_cliente.java
                Query query = db.collection("Peluquero").whereEqualTo("UID", result);
=======
                emailActual = bundle.getString("email");
                Query query = db.collection("cliente").whereEqualTo("UID", result);
>>>>>>> Stashed changes:Hairdate/app/src/main/java/com/example/hairdate/principal_cliente.java
=======
                emailActual = bundle.getString("email");
                Query query = db.collection("Peluquero").whereEqualTo("UID", result);
>>>>>>> Stashed changes
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                // Iterar a través de los documentos
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    // Obtener el valor en concreto que necesitas
                                    nombreUsuario = document.getString("nombre");
                                    usuario.setText(emailActual);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", emailActual);
                                    getParentFragmentManager().setFragmentResult("fragmentVerObjeto", bundle);

                                    StorageReference profileRef = storageReference.child(emailActual + "_profile.jpg");
                                    profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get().load(uri).into(profileImage);
                                        }
                                    });
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
<<<<<<< Updated upstream
        /*usuario.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_cliente_to_activity_profile);
            }
        }));*/

        /*btn_controlStock.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_stock_Peluquero);
            }
        }));*/
=======

        usuario.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(getView()).navigate(R.id.action_menu_cliente_to_activity_profile);
            }
        }));
>>>>>>> Stashed changes

        btn_cerrarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_menu_cliente_to_inicioSesion_Cliente);
            }
        }));

        return view;
    }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
}