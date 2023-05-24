package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import kotlin.jvm.internal.Intrinsics;

public class inicioSesion_Peluquero extends Fragment {
    /*
     *
     * Este Fragment nos servirá para que el usuario (Peluquero) inicie sesión o se cree la cuenta
     *
     */
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView crear;
    private ImageButton btn_eyeContrasena_inicio;
    private boolean ojoAbierto;
    private Button btn_iniciarSesion;
    private EditText peluqueroEmail, peluqueroPass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
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
        mAuth = FirebaseAuth.getInstance();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_sesion__peluquero, container, false);
        return view;
    }


    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.ojoAbierto = false;
        this.crear = (TextView) view.findViewById(R.id.txt_crear2_inicio);
        btn_iniciarSesion = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
        peluqueroEmail = (EditText) view.findViewById(R.id.edTxt_usuario_inicio);
        peluqueroPass = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
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
        btn_iniciarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                startSignIn(peluqueroEmail.getText().toString(), peluqueroPass.getText().toString());
                /*Bundle nos permitirá enviar datos de un fragment almacenandolo*/
                Bundle result = new Bundle();
                result.putString("bundleKey",peluqueroEmail.getText().toString());
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        }));
        this.btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.btn_eyeContrasena_inicio);
        this.btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (ojoAbierto) {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_closed);
                    peluqueroPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ojoAbierto = false;
                    // Si el ojo está cerrado, lo cambia a abierto y se empieza a ver la contraseña
                } else {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_open);
                    peluqueroPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    ojoAbierto = true;
                }
            }
        }));
        btn_iniciarSesion = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
        peluqueroEmail = (EditText) view.findViewById(R.id.edTxt_usuario_inicio);
        peluqueroPass = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
        btn_iniciarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                db =  FirebaseFirestore.getInstance();
                Query query = db.collection("Peluquero").whereEqualTo("email", peluqueroEmail.getText().toString().trim());
                if(!query.equals(null)){
                    startSignIn(peluqueroEmail.getText().toString().trim(), peluqueroPass.getText().toString());
                    Bundle result = new Bundle();
                    result.putString("bundleKey",mAuth.getUid());
                    Log.d("UID", String.valueOf(result));
                    getParentFragmentManager().setFragmentResult("requestKey", result);
                }
            }
        }));

    }
    private void startSignIn(String correo, String contrasena) {
        /*Comprueba que en la colección Peluquero el usuario y contraseña pasada por parametros existan, si existen se envía al menú principal, sino no hace nada*/
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Navigation.findNavController(getView()).navigate(R.id.action_inicioSesion_Peluquero_to_menu_Peluquero);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Error");
                            builder.setMessage("No es un email válido o no es un peluquero");
                            builder.setPositiveButton("Ok", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }



}