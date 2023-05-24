package com.example.hairdate;

import static java.time.LocalTime.now;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link preferenciaCitasCliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class preferenciaCitasCliente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_confirmar;
    private FirebaseAuth firebaseAuth;


    public preferenciaCitasCliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment preferenciaCitasCliente.
     */
    // TODO: Rename and change types and number of parameters
    public static preferenciaCitasCliente newInstance(String param1, String param2) {
        preferenciaCitasCliente fragment = new preferenciaCitasCliente();
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
        View view = inflater.inflate(R.layout.fragment_preferencia_citas_cliente, container, false);
        btn_confirmar = (Button) view.findViewById(R.id.btn_confirmar);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        DocumentReference referencia = db.collection("Citas").document();
        Map<String, Object> cita = new HashMap<>();

        //Temas de conversación
        CheckBox checkboxFut = view.findViewById(R.id.checkboxFut);
        CheckBox checkboxCin = view.findViewById(R.id.checkboxCin);
        CheckBox checkboxPol = view.findViewById(R.id.checkboxPol);
        CheckBox checkboxMas = view.findViewById(R.id.checkboxMas);
        CheckBox checkboxMedaigual = view.findViewById(R.id.checkboxMedaigual);
        CheckBox checkboxNada = view.findViewById(R.id.checkboxNada);
        CheckBox checkboxSer = view.findViewById(R.id.checkboxSer);
        EditText edtConver = view.findViewById(R.id.editTextCon);

        //Música
        CheckBox checkboxPop = view.findViewById(R.id.checkboxPop);
        CheckBox checkboxRap = view.findViewById(R.id.checkboxRap);
        CheckBox checkboxReggae = view.findViewById(R.id.checkboxReggae);
        CheckBox checkboxReggaeton = view.findViewById(R.id.checkboxReggaeton);
        CheckBox checkboxMusDaigua = view.findViewById(R.id.checkboxMusDaigua);
        CheckBox checkboxMusNada = view.findViewById(R.id.checkboxMusNada);
        CheckBox checkboxJazz = view.findViewById(R.id.checkboxJazz);
        EditText edtMus = view.findViewById(R.id.editTextMus);


        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // Exporta los datos del fragment que hemos solicitado antes y muestra el nombre del usuario insertado
                String result = bundle.getString("bundleKey");
                TextView fecha_hora = (TextView) view.findViewById(R.id.txt_fecha);
                fecha_hora.setText(result);
                btn_confirmar.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                    public final void onClick(View it) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Temas
                                String futbol = checkboxFut.isChecked() ? "si" : "no";
                                String cine = checkboxCin.isChecked() ? "si" : "no";
                                String politica = checkboxPol.isChecked() ? "si" : "no";
                                String temaMedaigual = checkboxMedaigual.isChecked() ? "si" : "no";
                                String temaNada = checkboxNada.isChecked() ? "si" : "no";
                                String series = checkboxSer.isChecked() ? "si" : "no";
                                String mascotas = checkboxMas.isChecked() ? "si" : "no";
                                //Música
                                String pop = checkboxPop.isChecked() ? "si" : "no";
                                String rap = checkboxRap.isChecked() ? "si" : "no";
                                String reggaeton = checkboxReggaeton.isChecked() ? "si" : "no";
                                String reggae = checkboxReggae.isChecked() ? "si" : "no";
                                String jazz = checkboxJazz.isChecked() ? "si" : "no";
                                String musMed = checkboxMusDaigua.isChecked() ? "si" : "no";
                                String musNada = checkboxMusNada.isChecked() ? "si" : "no";
                                cita.put("Fecha_Hora", result);
                                //Tema
                                cita.put("Tema_futbol", futbol);
                                cita.put("Tema_politica", politica);
                                cita.put("Tema_cine", cine);
                                cita.put("Tema_MeDaIgual",temaMedaigual);
                                cita.put("Tema_Nada", temaNada);
                                cita.put("Tema_Series", series);
                                cita.put("Tema_Mascotas", mascotas);
                                cita.put("Tema_Otro", edtConver.getText().toString());
                                //Música
                                cita.put("Musica_Pop", pop);
                                cita.put("Musica_Rap", rap);
                                cita.put("Musica_Reggaeton", reggaeton);
                                cita.put("Musica_Reggae",reggae);
                                cita.put("Musica_Jazz", jazz);
                                cita.put("Musica_MeDaIgual", musMed);
                                cita.put("Musica_Nada", musNada);
                                cita.put("Musica_Otro", edtMus.getText().toString());
                                //final
                                cita.put("UID", uid);
                                referencia.set(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Navigation.findNavController(getView()).navigate(R.id.action_preferenciaCitasCliente_to_menu_cliente);
                                        }
                                    }
                                });
                            }
                        }, 3000);
                    }
                }));

            }
        });
        return view;
    }
}