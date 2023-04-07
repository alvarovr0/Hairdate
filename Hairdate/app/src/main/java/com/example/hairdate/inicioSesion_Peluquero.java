package com.example.hairdate;

import static android.content.ContentValues.TAG;

import static com.example.hairdate.crearUsuario_Peluquero.generarClave;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import kotlin.jvm.internal.Intrinsics;

public class inicioSesion_Peluquero extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView crear;
    ImageButton btn_eyeContrasena_inicio;
    EditText edTxt_contrasena_inicio;
    boolean ojoAbierto;
    private EditText usuario, contrasena;
    private Button boton_inicio;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public inicioSesion_Peluquero() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicioSesion_Peluquero.
     */
    // TODO: Rename and change types and number of parameters
    public static inicioSesion_Peluquero newInstance(String param1, String param2) {
        inicioSesion_Peluquero fragment = new inicioSesion_Peluquero();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_sesion__peluquero, container, false);
        return view;
    }


    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.ojoAbierto = false;
        this.crear = (TextView) view.findViewById(R.id.txt_crear2_inicio);
        boton_inicio = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
        usuario = (EditText) view.findViewById(R.id.edTxt_usuario_inicio);
        contrasena = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
        String contrasenaCifrada;
        crear.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context)inicioSesion_Peluquero.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Quieres crear una cuenta?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(view).navigate(R.id.action_inicioSesion_Peluquero_to_crearUsuario_Peluquero);
                    }
                })).setNegativeButton((CharSequence)"No", (android.content.DialogInterface.OnClickListener)null);
                android.app.AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence)"¿Quieres cambiar de ventana?");
                alerta.show();
            }
        }));
        String cifrada = "";
        try {
            // Cifra la contraseña y la guarda en la variable contrasenaCifrada
            byte[] clave = generarClave();
            cifrada = codificarBase64(cifrarDatos(contrasena.getText().toString().getBytes("UTF-8"), clave));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalCifrada = cifrada;
        boton_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                startSignIn(usuario.getText().toString(), finalCifrada);
            }
        }));
        this.btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.btn_eyeContrasena_inicio);
        this.edTxt_contrasena_inicio = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
        this.btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (ojoAbierto) {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_closed);
                    edTxt_contrasena_inicio.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ojoAbierto = false;
                    // Si el ojo está cerrado, lo cambia a abierto y se empieza a ver la contraseña
                } else {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_open);
                    edTxt_contrasena_inicio.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    ojoAbierto = true;
                }
            }
        }));
    }

    private void startSignIn(String usuario, String contrasena) {
        db.collection("Peluquero")
                .whereEqualTo("usuario", usuario)
                .whereEqualTo("contrasena",contrasena)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Navigation.findNavController(getView()).navigate(R.id.action_inicioSesion_Peluquero_to_menu_Peluquero);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public static byte[] cifrarDatos(byte[] datos, byte[] clave) throws Exception {
        Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec claveSecreta = new SecretKeySpec(clave, "AES");
        cifrador.init(Cipher.ENCRYPT_MODE, claveSecreta);
        return cifrador.doFinal(datos);
    }
    public static String codificarBase64(byte[] datosCifrados) {
        return Base64.encodeToString(datosCifrados, Base64.DEFAULT);
    }

    public EditText getUsuario() {
        return usuario;
    }

    public void setUsuario(EditText usuario) {
        this.usuario = usuario;
    }
}