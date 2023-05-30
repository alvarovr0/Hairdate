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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tipo_servicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tipo_servicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button tinte, corte, tinteCorte, peinado;

    public tipo_servicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tipo_servicio.
     */
    // TODO: Rename and change types and number of parameters
    public static tipo_servicio newInstance(String param1, String param2) {
        tipo_servicio fragment = new tipo_servicio();
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
        View view = inflater.inflate(R.layout.fragment_tipo_servicio, container, false);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                        String result = bundle.getString("selectedDateTime");
                        Log.d("prueba", "Fecha seleccionada: " + result);
                        tinte = view.findViewById(R.id.btn_tinte);
                        corte = view.findViewById(R.id.btn_corte);
                        tinteCorte = view.findViewById(R.id.btn_tinteCorte);
                        peinado = view.findViewById(R.id.btn_peinado);

                        tinte.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                            public final void onClick(View it) {
                                Bundle resultservicio = new Bundle();
                                resultservicio.putString("bundleKey", tinte.getText().toString());
                                resultservicio.putString("fecha", result);
                                getParentFragmentManager().setFragmentResult("servicio", resultservicio);
                                Navigation.findNavController(getView()).navigate(R.id.action_tipo_servicio_to_preferenciaCitasCliente);
                            }
                        }));

                        corte.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                            public final void onClick(View it) {
                                Bundle resultservicio = new Bundle();
                                resultservicio.putString("bundleKey", corte.getText().toString());
                                resultservicio.putString("fecha", result);
                                getParentFragmentManager().setFragmentResult("servicio", resultservicio);
                                Navigation.findNavController(getView()).navigate(R.id.action_tipo_servicio_to_preferenciaCitasCliente);
                            }
                        }));

                        tinteCorte.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                            public final void onClick(View it) {
                                Bundle resultservicio = new Bundle();
                                resultservicio.putString("bundleKey", tinteCorte.getText().toString());
                                resultservicio.putString("fecha", result);
                                getParentFragmentManager().setFragmentResult("servicio", resultservicio);
                                Navigation.findNavController(getView()).navigate(R.id.action_tipo_servicio_to_preferenciaCitasCliente);
                            }
                        }));

                        peinado.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                            public final void onClick(View it) {
                                Bundle resultservicio = new Bundle();
                                resultservicio.putString("bundleKey", peinado.getText().toString());
                                resultservicio.putString("fecha", result);
                                getParentFragmentManager().setFragmentResult("servicio", resultservicio);
                                Navigation.findNavController(getView()).navigate(R.id.action_tipo_servicio_to_preferenciaCitasCliente);
                            }
                        }));
                    }
        });
        return view;
    }
}