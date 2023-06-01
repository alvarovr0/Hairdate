package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
 * Use the {@link peluqueria_mod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class peluqueria_mod extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edTxtNuevoNombrePeluquero;
    private EditText edTxtNuevoCifPeluquero;
    private EditText edTxtNuevoUsuarioPeluquero;
    private EditText edTxtNuevoDireccionPeluquero;
    private EditText edTxtNuevoNumPeluquero;
    private EditText edTxtNuevoHorarioPeluquero;
    private CheckBox checkboxCorte, checkboxTinte, checkboxPeinado, checkboxCorteTinte;

    // Otras variables necesarias
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uid;


    public peluqueria_mod() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment peluqueria_mod.
     */
    // TODO: Rename and change types and number of parameters
    public static peluqueria_mod newInstance(String param1, String param2) {
        peluqueria_mod fragment = new peluqueria_mod();
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
        View view = inflater.inflate(R.layout.fragment_peluqueria_mod, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Obtener UID del usuario actualmente autenticado
        uid = firebaseAuth.getCurrentUser().getUid();

        // Referenciar las vistas del XML
        edTxtNuevoNombrePeluquero = view.findViewById(R.id.edTxt_nuevo_nombre_peluquero);
        edTxtNuevoCifPeluquero = view.findViewById(R.id.edTxt_nuevo_cif_peluquero);
        edTxtNuevoUsuarioPeluquero = view.findViewById(R.id.edTxt_nuevo_usuario_peluquero);
        edTxtNuevoDireccionPeluquero = view.findViewById(R.id.edTxt_nuevo_Direccion_peluquero);
        edTxtNuevoNumPeluquero = view.findViewById(R.id.edTxt_nuevo_num_peluquero);
        edTxtNuevoHorarioPeluquero = view.findViewById(R.id.edTxt_nuevo_horario_peluquero);
        checkboxCorte = view.findViewById(R.id.checkbox_nuevo_Corte);
        checkboxTinte = view.findViewById(R.id.checkbox_nuevo_Tinte);
        checkboxPeinado = view.findViewById(R.id.checkbox_nuevo_Peinado);
        checkboxCorteTinte = view.findViewById(R.id.checkbox_nuevo_CorteTinte);

        // Obtener el UID del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        } else {
            // Manejar el caso en el que el usuario no esté autenticado
            return view;
        }

        CollectionReference usuariosRef = firestore.collection("Peluqueria");
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
                        String cif = documentSnapshot.getString("cif");
                        String usuario = documentSnapshot.getString("usuario");
                        String direccion = documentSnapshot.getString("direccion");
                        String numTelefono = documentSnapshot.getString("numeroTelefono");
                        String horario = documentSnapshot.getString("horario");

                        // Mostrar los datos en los campos correspondientes
                        edTxtNuevoNombrePeluquero.setText(nombre);
                        edTxtNuevoCifPeluquero.setText(cif);
                        edTxtNuevoUsuarioPeluquero.setText(usuario);
                        edTxtNuevoDireccionPeluquero.setText(direccion);
                        edTxtNuevoNumPeluquero.setText(numTelefono);
                        edTxtNuevoHorarioPeluquero.setText(horario);
                    }
                }
            }
        });

        // Referenciar el botón de guardar
        Button btnGuardar = view.findViewById(R.id.btn_registro_peluquero);
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
        String nuevoNombre = edTxtNuevoNombrePeluquero.getText().toString().trim();
        String nuevoCif = edTxtNuevoCifPeluquero.getText().toString().trim();
        String nuevoUsuario = edTxtNuevoUsuarioPeluquero.getText().toString().trim();
        String nuevaDireccion = edTxtNuevoDireccionPeluquero.getText().toString().trim();
        String nuevoNumTelefono = edTxtNuevoNumPeluquero.getText().toString().trim();
        String nuevoHorario = edTxtNuevoHorarioPeluquero.getText().toString().trim();

        // Validar que se ingresen todos los campos requeridos
        if (nuevoNombre.isEmpty() || nuevoCif.isEmpty() || nuevoUsuario.isEmpty() || nuevaDireccion.isEmpty()
                || nuevoNumTelefono.isEmpty() || nuevoHorario.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar los datos en la base de datos
        CollectionReference usuariosRef = firestore.collection("Peluqueria");
        Query query = usuariosRef.whereEqualTo("UID", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Spinner direccion = getView().findViewById(R.id.spinner_nuevo_Calle_peluquero);
                        Spinner tipo = getView().findViewById(R.id.spinner_nuevo_tipo_peluqueria);
                        String corte = checkboxCorte.isChecked() ? "si" : "no";
                        String corteTinte = checkboxCorteTinte.isChecked() ? "si" : "no";
                        String tinte = checkboxTinte.isChecked() ? "si" : "no";
                        String peinado = checkboxPeinado.isChecked() ? "si" : "no";
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        DocumentReference usuarioRef = usuariosRef.document(documentSnapshot.getId());
                        usuarioRef.update("nombre", nuevoNombre);
                        usuarioRef.update("cif", nuevoCif);
                        usuarioRef.update("usuario", nuevoUsuario);
                        usuarioRef.update("direccion", direccion.getSelectedItem().toString() + nuevaDireccion);
                        usuarioRef.update("numeroTelefono", nuevoNumTelefono);
                        usuarioRef.update("horario", nuevoHorario);
                        usuarioRef.update("corte", corte);
                        usuarioRef.update("corte_tinte", corteTinte);
                        usuarioRef.update("tinte", tinte);
                        usuarioRef.update("peinado", peinado);
                        usuarioRef.update("tipo", tipo.getSelectedItem().toString());

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