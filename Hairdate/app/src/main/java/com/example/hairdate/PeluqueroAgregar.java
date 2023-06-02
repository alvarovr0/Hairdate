package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeluqueroAgregar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeluqueroAgregar extends Fragment {

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
    private EditText edTxtNuevoUsuarioPeluquero;
    private EditText NIF;
    private Button btn_registro_peluquero;
    FirebaseUser peluqueria;

    // Otras variables necesarias
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    public PeluqueroAgregar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeluqueroAgregar.
     */
    // TODO: Rename and change types and number of parameters
    public static PeluqueroAgregar newInstance(String param1, String param2) {
        PeluqueroAgregar fragment = new PeluqueroAgregar();
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
        View view = inflater.inflate(R.layout.fragment_peluquero_agregar, container, false);

        firestore = FirebaseFirestore.getInstance();
        NIF = view.findViewById(R.id.edTxt_nuevo_nif_peluquero);
        spn = view.findViewById(R.id.spinner_nuevo_tipo_peluquero);
        edTxt_nuevo_nombre_peluquero = view.findViewById(R.id.edTxt_nuevo_nombre_peluquero);
        btn_registro_peluquero = view.findViewById(R.id.btn_registro_peluquero);
        FirebaseUser peluqueria = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference referencia = firestore.collection("Peluquero").document();

        btn_registro_peluquero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("nombre", edTxt_nuevo_nombre_peluquero.getText().toString().trim());
                user.put("NIF", NIF.getText().toString().trim());
                user.put("peluqueria", peluqueria.getUid());
                user.put("especialidad", spn.getSelectedItem().toString());
                referencia.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Peluquero creado", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_peluqueroAgregar_to_listaPeluqueros);
                        }
                    }
                });
            }
        });


        return view;
    }



}