package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.accessibilityservice.GestureDescription;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menu_Peluquero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_Peluquero extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView usuario;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_peluquero, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*String usuarioId = "kol8CQdXGHHFIXT3L9Q7"; // ID del usuario que deseas cargar
        usuario = view.findViewById(R.id.txt_nombrePeluquero);

        // Obtener una referencia al documento del usuario en la colección "usuarios"
        DocumentReference usuarioRef = db.collection("Peluquero").document(usuarioId);

        // Obtener los datos del usuario de Firestore
        usuarioRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // El documento del usuario existe en Firestore
                        String nombreUsuario = documentSnapshot.getString("nombre");
                        // Utilizar el nombre de usuario cargado
                        Log.d(TAG, "Nombre de usuario cargado: " + nombreUsuario);
                        TextView nombre = (TextView) view.findViewById(R.id.txt_nombrePeluquero);
                        nombre.setText("Hola, " + nombreUsuario);
                    } else {
                        // El documento del usuario no existe en Firestore
                        Log.w(TAG, "El documento del usuario no existe");
                    }
                })
                .addOnFailureListener(e -> {
                    // Error al cargar los datos del usuario desde Firestore
                    Log.w(TAG, "Error al cargar los datos del usuario", e);
                });
        usuario.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_menu_Peluquero_to_activity_profile);
            }
        }));*/
        db.collection("Peluquero")
                .whereEqualTo("usuario", usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TextView nombre = (TextView) view.findViewById(R.id.txt_nombrePeluquero);
                                nombre.setText("Hola, " + db.collection("Peluquero").getPath("nombre");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return view;
    }

}