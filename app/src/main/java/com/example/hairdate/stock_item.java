package com.example.hairdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stock_item#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stock_item extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_modi, btn_guardar;
    private EditText nombreEditText, precioEditText, cantidadEditText;

    public stock_item() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment stock_item.
     */
    // TODO: Rename and change types and number of parameters
    public static stock_item newInstance(String param1, String param2) {
        stock_item fragment = new stock_item();
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
        View view = inflater.inflate(R.layout.fragment_stock_item, container, false);
        nombreEditText = view.findViewById(R.id.nombreEditText);
        precioEditText = view.findViewById(R.id.precioEditText);
        cantidadEditText = view.findViewById(R.id.cantidadEditText);
        btn_modi.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                nombreEditText.setEnabled(true);
                precioEditText.setEnabled(true);
                cantidadEditText.setEnabled(true);
                btn_modi.setVisibility(View.INVISIBLE);
                btn_guardar.setVisibility(View.VISIBLE);
                btn_guardar.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                    public final void onClick(View it) {
                        getParentFragmentManager().setFragmentResultListener("documentoID", getViewLifecycleOwner(), new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                                // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                                String result = bundle.getString("idDocumento");
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Query query = db.collection("Stock").whereEqualTo("PeluqueroID", "/Peluquero/" + result);
                                query.get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot querySnapshot) {

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
                    }
                }));
            }
        }));
        return view;
    }
}