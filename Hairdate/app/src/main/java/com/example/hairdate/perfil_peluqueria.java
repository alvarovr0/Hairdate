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
        getParentFragmentManager().setFragmentResultListener("requestKey", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                String result = bundle.getString("bundleKey");
                Query query = db.collection("Peluqueria").whereEqualTo(FieldPath.documentId(), result);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                // Iterar a través de los documentos
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    TextView direccion = (TextView) rootView.findViewById(R.id.txtDireccion);
                                    TextView horario = (TextView) rootView.findViewById(R.id.txtHorario);
                                    TextView numeroTelefono = (TextView) rootView.findViewById(R.id.txtNumTlf);
                                    TextView nombre = (TextView) rootView.findViewById(R.id.txtNombre);

                                    direccion.setText(document.getString("direccion"));
                                    horario.setText(document.getString("horario"));
                                    numeroTelefono.setText(document.getString("numeroTelefono"));
                                    nombre.setText(document.getString("nombre"));
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

        // Inflate the layout for this fragment
        return rootView;
    }
}