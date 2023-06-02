package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Peluquero_detalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Peluquero_detalle extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spn;
    private EditText edTxt_nuevo_nombre_peluquero;
    private EditText edTxt_nuevo_usuario_peluquero;

    // Otras variables necesarias
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uid;

    public Peluquero_detalle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Peluquero_detalle.
     */
    // TODO: Rename and change types and number of parameters
    public static Peluquero_detalle newInstance(String param1, String param2) {
        Peluquero_detalle fragment = new Peluquero_detalle();
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
        View view = inflater.inflate(R.layout.fragment_peluquero_detalle, container, false);

        spn = view.findViewById(R.id.spinner_nuevo_tipo_peluquero);
        edTxt_nuevo_nombre_peluquero = view.findViewById(R.id.edTxt_nuevo_nombre_peluquero);

        guardarDatos();

        return view;
    }

    private void guardarDatos() {
        // Obtener los nuevos datos ingresados por el usuario
        String nuevoNombre = edTxt_nuevo_nombre_peluquero.getText().toString().trim();
        String nuevoUsuario = edTxt_nuevo_usuario_peluquero.getText().toString().trim();
        String tipo = spn.getSelectedItem().toString().trim();

        // Validar que se ingresen todos los campos requeridos
        if (nuevoNombre.isEmpty() || nuevoUsuario.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
        }

        // Actualizar los datos en la base de datos

        CollectionReference usuariosRef = firestore.collection("Peluquero");
        Query query = usuariosRef.whereEqualTo("UID", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Spinner direccion = getView().findViewById(R.id.spinner_nuevo_Calle_peluquero);
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        DocumentReference usuarioRef = usuariosRef.document(documentSnapshot.getId());
                        usuarioRef.update("nombre", nuevoNombre);
                        usuarioRef.update("usuario", nuevoUsuario);
                        usuarioRef.update("tipo", spn.getSelectedItem().toString());

                        Toast.makeText(getContext(), "Datos guardados exitosamente.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_peluqueria_mod_to_menu_Peluquero);
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