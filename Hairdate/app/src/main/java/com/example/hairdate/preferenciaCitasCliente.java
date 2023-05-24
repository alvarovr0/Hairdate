package com.example.hairdate;

import static java.time.LocalTime.now;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

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
    private FirebaseAuth mAuth;
    private String uid;


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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference referencia = db.collection("Cita").document();
        Map<String, Object> cita = new HashMap<>();
        uid = mAuth.getUid();
        CheckBox checkboFut = view.findViewById(R.id.checkboFut);
        CheckBox checkboxCin = view.findViewById(R.id.checkboxCin);
        CheckBox checkboxPol = view.findViewById(R.id.checkboxPol);
        CheckBox checkboxMas = view.findViewById(R.id.checkboxMas);
        CheckBox checkboxMedaigual = view.findViewById(R.id.checkboxMedaigual);
        CheckBox checkboxNada = view.findViewById(R.id.checkboxNada);
        CheckBox checkboxSer = view.findViewById(R.id.checkboxSer);
        EditText edtConver = view.findViewById(R.id.editTextCon);
        StringBuilder temasSeleccionados = new StringBuilder();
        if (checkboFut.isChecked()) {
            temasSeleccionados.append("Fútbol ");
        }
        if (checkboxCin.isChecked()) {
            temasSeleccionados.append("Cine ");
        }
        if (checkboxPol.isChecked()) {
            temasSeleccionados.append("Política ");
        }
        if (checkboxMas.isChecked()) {
            temasSeleccionados.append("Mascotas ");
        }
        if (checkboxMedaigual.isChecked()) {
            temasSeleccionados.append("Me da igual ");
        }
        if (checkboxNada.isChecked()) {
            temasSeleccionados.append("De nada ");
        }
        if (checkboxSer.isChecked()) {
            temasSeleccionados.append("Series ");
        }
        String datosSeleccionados = temasSeleccionados.toString();

        CheckBox checkboxPop = view.findViewById(R.id.checkboxPop);
        CheckBox checkboxRap = view.findViewById(R.id.checkboxRap);
        CheckBox checkboxReggae = view.findViewById(R.id.checkboxReggae);
        CheckBox checkboxReggaeton = view.findViewById(R.id.checkboxReggaeton);
        CheckBox checkboxMusDaigua = view.findViewById(R.id.checkboxMusDaigua);
        CheckBox checkboxMusNada = view.findViewById(R.id.checkboxMusNada);
        CheckBox checkboxJazz = view.findViewById(R.id.checkboxJazz);
        EditText edtMus = view.findViewById(R.id.editTextMus);
        StringBuilder musicasSeleccionadas = new StringBuilder();
        if (checkboxPop.isChecked()) {
            musicasSeleccionadas.append("Pop ");
        }
        if (checkboxRap.isChecked()) {
            musicasSeleccionadas.append("Rap ");
        }
        if (checkboxReggae.isChecked()) {
            musicasSeleccionadas.append("Reggae ");
        }
        if (checkboxReggaeton.isChecked()) {
            musicasSeleccionadas.append("Reggaeton ");
        }
        if (checkboxMusDaigua.isChecked()) {
            musicasSeleccionadas.append("Me da igual ");
        }
        if (checkboxMusNada.isChecked()) {
            musicasSeleccionadas.append("De nada ");
        }
        if (checkboxJazz.isChecked()) {
            musicasSeleccionadas.append("Jazz ");
        }
        String musica = musicasSeleccionadas.toString();
        /*cita.put("Fecha_Hora", Aquí va la fecha seleccionada);*/
        cita.put("Temas_Conver", datosSeleccionados + edtConver.getText().toString());
        cita.put("Musica", musica + edtMus.getText().toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cita.put("UID", uid);
                referencia.set(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Aquí va al menú Cliente
                        }
                    }
                });
            }
        }, 3000);
        return view;
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}