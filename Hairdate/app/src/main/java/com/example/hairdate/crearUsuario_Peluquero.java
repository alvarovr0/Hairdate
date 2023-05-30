package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;

import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crearUsuario_Peluquero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crearUsuario_Peluquero extends Fragment {
    /*
     *
     * Este Fragment nos servirá para que el usuario (Peluquero) se cree una cuenta
     *
     */
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean ojoAbierto = false;
    private EditText nombre, cif, usuario, email, contrasena, direccion, num_tlf, horario;
    private Button botonRegistro;
    private ImageButton btn_eyeContrasena_inicio;
    private FirebaseAuth mAuth;
    private String uid;
    private CheckBox checkboxCorte, checkboxTinte, checkboxPeinado, checkboxCorteTinte;

    public crearUsuario_Peluquero() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment crearUsuario_Peluquero.
     */
    // TODO: Rename and change types and number of parameters
    public static crearUsuario_Peluquero newInstance(String param1, String param2) {
        crearUsuario_Peluquero fragment = new crearUsuario_Peluquero();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = mAuth.getUid();
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Creamos una instancia de nuestra BBDD en este caso FireBase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference referencia = db.collection("Peluqueria").document();
        View view = inflater.inflate(R.layout.fragment_crear_usuario_peluquero, container, false);
        Spinner spn = (Spinner) view.findViewById(R.id.spinnerCalle_peluquero);
        nombre = (EditText) view.findViewById(R.id.edTxt_nombre_peluquero);
        cif = (EditText) view.findViewById(R.id.edTxt_cif_peluquero);
        usuario = (EditText) view.findViewById(R.id.edTxt_usuario_peluquero);
        email = (EditText) view.findViewById(R.id.edTxt_Email_peluquero);
        direccion = (EditText) view.findViewById(R.id.edTxt_Direccion_peluquero);
        contrasena = (EditText) view.findViewById(R.id.edTxt_contrasena_peluquero);
        num_tlf = (EditText) view.findViewById(R.id.edTxt_num_peluquero);
        horario = (EditText) view.findViewById(R.id.edTxt_horario_peluquero);
        checkboxCorte = view.findViewById(R.id.checkboxCorte);
        checkboxTinte = view.findViewById(R.id.checkboxTinte);
        checkboxPeinado = view.findViewById(R.id.checkboxPeinado);
        checkboxCorteTinte = view.findViewById(R.id.checkboxCorteTinte);
        botonRegistro = (Button) view.findViewById(R.id.btn_registro_peluquero);
        btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.ojoBoton_peluquero);
        btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                // Si el ojo está abierto, lo cambia a cerrado, y la contraseña se deja de ver
                if (ojoAbierto) {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_closed);
                    contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ojoAbierto = false;
                    // Si el ojo está cerrado, lo cambia a abierto y se empieza a ver la contraseña
                } else {
                    btn_eyeContrasena_inicio.setImageResource(R.drawable.eye_open);
                    contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    ojoAbierto = true;
                }
            }
        }));
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailvalidator = email.getText().toString();
                String direccion_completa = spn.getSelectedItem().toString() + "   " + direccion.getText().toString();
                String password = contrasena.getText().toString();
                String corte = checkboxCorte.isChecked() ? "si" : "no";
                String corteTinte = checkboxCorteTinte.isChecked() ? "si" : "no";
                String tinte = checkboxTinte.isChecked() ? "si" : "no";
                String peinado = checkboxPeinado.isChecked() ? "si" : "no";

                if (!emailvalidator.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailvalidator).matches() && password.length() >= 6) {
                    // Verificar si el correo ya existe en la base de datos
                    Query query = db.collection("Peluqueria").whereEqualTo("email", emailvalidator);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    // El correo ya existe en la base de datos
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle("Correo en uso")
                                            .setMessage("El correo que has introducido ya está en uso, prueba con otro o inicia sesión.")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    // El correo no existe en la base de datos
                                    createAccount(emailvalidator, password);
                                    Toast.makeText(view.getContext(), "Correo válido", Toast.LENGTH_LONG).show();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("nombre", nombre.getText().toString());
                                    user.put("CIF", cif.getText().toString());
                                    user.put("usuario", usuario.getText().toString());
                                    user.put("email", emailvalidator);
                                    user.put("direccion", direccion_completa);
                                    user.put("numeroTelefono", num_tlf.getText().toString());
                                    user.put("horario", horario.getText().toString());
                                    user.put("corte", corte);
                                    user.put("corte_tinte", corteTinte);
                                    user.put("tinte", tinte);
                                    user.put("peinado", peinado);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            user.put("UID", uid);
                                            referencia.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Navigation.findNavController(v).navigate(R.id.action_crearUsuario_Peluquero_to_inicioSesion);
                                                    }
                                                }
                                            });
                                        }
                                    }, 3000);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    if (password.length() < 6) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Contraseña inválida")
                                .setMessage("La contraseña debe tener al menos 6 caracteres.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(view.getContext(), "Email no válido", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return view;


    }


    private void reload() {
    }

    private void updateUI(FirebaseUser user) {

    }
}