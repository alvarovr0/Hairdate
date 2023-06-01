package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cliente_mod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cliente_mod extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edTxtNuevoNombreCliente;
    private EditText edTxtNuevoUsuarioCliente;
    // Otras variables necesarias

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uid;

    public cliente_mod() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cliente_mod.
     */
    // TODO: Rename and change types and number of parameters
    public static cliente_mod newInstance(String param1, String param2) {
        cliente_mod fragment = new cliente_mod();
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
        View view = inflater.inflate(R.layout.fragment_cliente_mod, container, false);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Obtener UID del usuario actualmente autenticado
        uid = firebaseAuth.getCurrentUser().getUid();

        // Referenciar las vistas del XML
        edTxtNuevoNombreCliente = view.findViewById(R.id.edTxt_nuevo_nombre_cliente);
        edTxtNuevoUsuarioCliente = view.findViewById(R.id.edTxt_nuevo_usuario_cliente);

        // Obtener el UID del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        } else {
            // Manejar el caso en el que el usuario no esté autenticado
            return view;
        }

        CollectionReference usuariosRef = firestore.collection("Cliente");
        Query query = usuariosRef.whereEqualTo("UID", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                        // Obtener los datos existentes del documento
                        String nombre = documentSnapshot.getString("nombre");
                        String usuario = documentSnapshot.getString("usuario");


                        // Mostrar los datos en los campos correspondientes
                        edTxtNuevoNombreCliente.setText(nombre);
                        edTxtNuevoUsuarioCliente.setText(usuario);
                    }
                }
            }
        });


        // Referenciar el botón de guardar
        Button btnGuardar = view.findViewById(R.id.btn_mod_cliente);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });
        return view;
    }
    private void guardarDatos() {
        // Obtener los nuevos datos ingresados por el usuario
        String nuevoNombre = edTxtNuevoNombreCliente.getText().toString().trim();
        String nuevoUsuario = edTxtNuevoUsuarioCliente.getText().toString().trim();

        // Validar que se ingresen todos los campos requeridos
        if (nuevoNombre.isEmpty() || nuevoUsuario.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar los datos en la base de datos
        CollectionReference usuariosRef = firestore.collection("Cliente");
        Query query = usuariosRef.whereEqualTo("UID", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        DocumentReference usuarioRef = usuariosRef.document(documentSnapshot.getId());
                        usuarioRef.update("nombre", nuevoNombre);
                        usuarioRef.update("usuario", nuevoUsuario);

                        Toast.makeText(getContext(), "Datos guardados exitosamente.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_cliente_mod_to_principal_cliente);
                    } else {
                        Toast.makeText(getContext(), "No se encontró el documento.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}