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
import android.view.KeyEvent;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import kotlin.jvm.internal.Intrinsics;

public class inicioSesion extends Fragment {

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
    private EditText Email, Pass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public inicioSesion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicioSesion.
     */
    // TODO: Rename and change types and number of parameters
    public static inicioSesion newInstance(String param1, String param2) {
        inicioSesion fragment = new inicioSesion();
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
        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
        return view;
    }


    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.ojoAbierto = false;
        this.crear = (TextView) view.findViewById(R.id.txt_crear2_inicio);
        btn_iniciarSesion = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
        Email = (EditText) view.findViewById(R.id.edTxt_usuario);
        Pass = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
        crear.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                AlertDialog.Builder constructorDialogo = new AlertDialog.Builder((Context) inicioSesion.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Quieres crear una cuenta?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (DialogInterface.OnClickListener)(new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(view).navigate(R.id.action_inicioSesion_to_tipo_cuenta);
                    }
                })).setNegativeButton((CharSequence)"No", (DialogInterface.OnClickListener)null);
                AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence)"¿Quieres cambiar de ventana?");
                alerta.show();
            }
        }));
        this.btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.btn_eyeContrasena_inicio);
        this.btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (ojoAbierto) {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_closed);
                    Pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ojoAbierto = false;
                    // Si el ojo está cerrado, lo cambia a abierto y se empieza a ver la contraseña
                } else {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_open);
                    Pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    ojoAbierto = true;
                }
            }
        }));

        btn_iniciarSesion = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
        Email = (EditText) view.findViewById(R.id.edTxt_usuario);
        Pass = (EditText) view.findViewById(R.id.edTxt_contrasena);
        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                String email = Email.getText().toString().trim();

                // Consulta para verificar si es un Peluquero
                Query peluqueroQuery = db.collection("Peluqueria").whereEqualTo("email", email);
                peluqueroQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                // Es un Peluquero
                                startSignIn(email, Pass.getText().toString(), new SignInCallback() {
                                    @Override
                                    public void onSignInSuccess() {
                                        Bundle result = new Bundle();
                                        result.putString("bundleKey", mAuth.getUid());
                                        result.putString("email", email);
                                        Log.d("UID", String.valueOf(result));
                                        getParentFragmentManager().setFragmentResult("menuPeluquero", result);
                                        Navigation.findNavController(getView()).navigate(R.id.action_inicioSesion_to_menu_Peluquero);
                                    }

                                    @Override
                                    public void onSignInFailure() {
                                        Toast.makeText(getContext(), "Error: Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // No es un Peluquero, verificar si es un Cliente
                                Query clienteQuery = db.collection("Cliente").whereEqualTo("email", email);
                                clienteQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                                // Es un Cliente
                                                startSignIn(email, Pass.getText().toString(), new SignInCallback() {
                                                    @Override
                                                    public void onSignInSuccess() {
                                                        Bundle result = new Bundle();
                                                        result.putString("bundleKey", mAuth.getUid());
                                                        result.putString("email", email);
                                                        Log.d("UID", String.valueOf(result));
                                                        getParentFragmentManager().setFragmentResult("menuCliente", result);
                                                        Navigation.findNavController(getView()).navigate(R.id.action_inicioSesion_to_menu_cliente);
                                                    }

                                                    @Override
                                                    public void onSignInFailure() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle("Error");
                                                        builder.setMessage("Inicio de sesión fallido");
                                                        builder.setPositiveButton("OK", null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                });
                                            } else {
                                                // No es un Cliente ni un Peluquero válido
                                                Toast.makeText(getContext(), "No es un usuario válido", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Error al ejecutar la consulta de Cliente
                                            // Manejar el error en consecuencia
                                        }
                                    }
                                });
                            }
                        } else {
                            // Error al ejecutar la consulta de Peluquero
                            // Manejar el error en consecuencia
                        }
                    }
                });
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // Consumir el evento del botón de retroceso
                    return true;
                }
                return false;
            }
        });

    }

    private void startSignIn(String correo, String contrasena, final SignInCallback callback) {
        if(contrasena.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error");
            builder.setMessage("Inserta una contraseña");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    callback.onSignInSuccess();
                                } else {
                                    // User is null, handle sign-in failure
                                    Log.w(TAG, "signInWithEmail:failure - User is null");
                                    callback.onSignInFailure();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                callback.onSignInFailure();
                            }
                        }
                    });
        }

    }

    interface SignInCallback {
        void onSignInSuccess();
        void onSignInFailure();
    }
    private void reload() { }

}