<<<<<<< Updated upstream

package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
=======
package com.example.hairdate;

>>>>>>> Stashed changes
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
<<<<<<< Updated upstream
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
=======
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
>>>>>>> Stashed changes
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

<<<<<<< Updated upstream
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public class inicioSesion_Cliente extends Fragment {
    /*
     *
     * Este Fragment nos servirá para que el usuario (Cliente) inicie sesión o se cree la cuenta
     *
     */
=======
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import kotlin.jvm.internal.Intrinsics;

public class inicioSesion_Cliente extends Fragment {

>>>>>>> Stashed changes
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
<<<<<<< Updated upstream
    private TextView crear;
    private ImageButton btn_eyeContrasena_inicio;
    private EditText edTxt_contrasena_Cliente, edTxt_usuarioCliente;
    private boolean ojoAbierto;
    private Button btn_iniciarSesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText clienteEmail, clientePass;
=======
    TextView crear;
    ImageButton btn_eyeContrasena_inicio;
    EditText edTxt_contrasena_inicio;
    private EditText contrasena;
    boolean ojoAbierto;
>>>>>>> Stashed changes
    public inicioSesion_Cliente() {
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
    public static inicioSesion_Cliente newInstance(String param1, String param2) {
        inicioSesion_Cliente fragment = new inicioSesion_Cliente();
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
<<<<<<< Updated upstream
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
=======
>>>>>>> Stashed changes
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
<<<<<<< Updated upstream
        View view = inflater.inflate(R.layout.fragment_inicio_sesion_cliente, container, false);
=======
        View view = inflater.inflate(R.layout.fragment_inicio_sesion__cliente, container, false);


>>>>>>> Stashed changes
        return view;
    }


    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.ojoAbierto = false;
        this.crear = (TextView) view.findViewById(R.id.txt_crear2_inicio);
<<<<<<< Updated upstream
        btn_iniciarSesion = (Button) view.findViewById(R.id.btn_iniciarSesion_inicio);
=======

        contrasena = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
>>>>>>> Stashed changes
        crear.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) inicioSesion_Cliente.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Quieres crear una cuenta?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (DialogInterface.OnClickListener)(new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(view).navigate(R.id.action_inicioSesion_Cliente_to_crearUsuario_Cliente);
                    }
                })).setNegativeButton((CharSequence)"No", (DialogInterface.OnClickListener)null);
                android.app.AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence)"¿Quieres cambiar de ventana?");
                alerta.show();
            }
        }));
<<<<<<< Updated upstream
        this.btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.btn_eyeContrasena_inicio);
        this.edTxt_contrasena_Cliente = (EditText) view.findViewById(R.id.edTxt_contrasena_cliente);
        this.edTxt_usuarioCliente = (EditText) view.findViewById(R.id.edTxt_usuario_cliente);
=======
        String cifrada = "";
        try {
            // Cifra la contraseña y la guarda en la variable contrasenaCifrada
            byte[] clave = generarClave();
            cifrada = codificarBase64(cifrarDatos(contrasena.getText().toString().getBytes("UTF-8"), clave));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalCifrada = cifrada;
        this.btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.btn_eyeContrasena_inicio);
        this.edTxt_contrasena_inicio = (EditText) view.findViewById(R.id.edTxt_contrasena_inicio);
>>>>>>> Stashed changes
        this.btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (ojoAbierto) {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_closed);
<<<<<<< Updated upstream
                    edTxt_contrasena_Cliente.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
=======
                    edTxt_contrasena_inicio.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
>>>>>>> Stashed changes
                    ojoAbierto = false;
                    // Si el ojo está cerrado, lo cambia a abierto y se empieza a ver la contraseña
                } else {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_open);
<<<<<<< Updated upstream
                    edTxt_contrasena_Cliente.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
=======
                    edTxt_contrasena_inicio.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
>>>>>>> Stashed changes
                    ojoAbierto = true;
                }
            }
        }));
<<<<<<< Updated upstream
        clienteEmail = (EditText) view.findViewById(R.id.edTxt_usuario_cliente);
        clientePass = (EditText) view.findViewById(R.id.edTxt_contrasena_cliente);
        btn_iniciarSesion.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                db =  FirebaseFirestore.getInstance();
                Query query = db.collection("Cliente").whereEqualTo("email", clienteEmail.getText().toString().trim());
                if(!query.equals(null)){
                    Bundle result = new Bundle();
                    result.putString("bundleKey",mAuth.getUid());

                    result.putString("email", clienteEmail.getText().toString().trim());

                    Log.d("UID", String.valueOf(result));
                    getParentFragmentManager().setFragmentResult("menuPeluquero", result);
                    startSignIn(clienteEmail.getText().toString().trim(), clientePass.getText().toString());
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
                            Navigation.findNavController(getView()).navigate(R.id.action_inicioSesion_Cliente_to_menu_cliente);
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

=======
    }
    public static byte[] generarClave() {
        byte[] clave = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(clave);
        return clave;
    }

    // Cifra los datos usando AES
    public static byte[] cifrarDatos(byte[] datos, byte[] clave) throws Exception {
        Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec claveSecreta = new SecretKeySpec(clave, "AES");
        cifrador.init(Cipher.ENCRYPT_MODE, claveSecreta);
        return cifrador.doFinal(datos);
    }
    public static String codificarBase64(byte[] datosCifrados) {
        return Base64.encodeToString(datosCifrados, Base64.DEFAULT);
>>>>>>> Stashed changes
    }
}