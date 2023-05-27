package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perfil_peluqueria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfil_peluqueria extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;

    private TextView direccionTextView;
    private TextView horarioTextView;
    private TextView numeroTelefonoTextView;
    private TextView nombreTextView;

    public perfil_peluqueria() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfil_peluqueria.
     */
    // TODO: Rename and change types and number of parameters
    public static perfil_peluqueria newInstance(String param1, String param2) {
        perfil_peluqueria fragment = new perfil_peluqueria();
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

        View rootView = inflater.inflate(R.layout.fragment_perfil_peluqueria, container, false);

        db = FirebaseFirestore.getInstance();

        direccionTextView = rootView.findViewById(R.id.txtDireccion);
        horarioTextView = rootView.findViewById(R.id.txtHorario);
        numeroTelefonoTextView = rootView.findViewById(R.id.txtNumTlf);
        nombreTextView = rootView.findViewById(R.id.txtNombre);

        // Obtener los argumentos pasados desde el fragmento anterior
        if (getArguments() != null) {
            String direccion = getArguments().getString("direccion");
            String horario = getArguments().getString("horario");
            String numeroTelefono = getArguments().getString("numeroTelefono");
            String nombre = getArguments().getString("nombre");

            // Mostrar los datos en los TextView correspondientes
            direccionTextView.setText(direccion);
            horarioTextView.setText(horario);
            numeroTelefonoTextView.setText(numeroTelefono);
            nombreTextView.setText(nombre);
        }
        return rootView;
    }
}