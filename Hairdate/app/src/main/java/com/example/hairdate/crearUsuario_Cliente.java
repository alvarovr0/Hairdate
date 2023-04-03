package com.example.hairdate;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crearUsuario_Cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crearUsuario_Cliente extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean ojoAbierto = false;
    private EditText nombre, cif, usuario, email, contrasena, direccion;
    private Button botonRegistro;
    private ImageButton btn_eyeContrasena_inicio;

    public crearUsuario_Cliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment crearUsuario_Cliente.
     */
    // TODO: Rename and change types and number of parameters
    public static crearUsuario_Cliente newInstance(String param1, String param2) {
        crearUsuario_Cliente fragment = new crearUsuario_Cliente();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_crear_usuario_cliente, container, false);
        nombre = (EditText) view.findViewById(R.id.edTxt_nombre_cliente);
        usuario = (EditText) view.findViewById(R.id.edTxt_usuario_cliente);
        email = (EditText) view.findViewById(R.id.edTxt_Email_cliente);
        contrasena = (EditText) view.findViewById(R.id.edTxt_contrasena_cliente);
        botonRegistro = (Button) view.findViewById(R.id.btn_registro_cliente);
       btn_eyeContrasena_inicio = (ImageButton) view.findViewById(R.id.ojoBoton_cliente);

        btn_eyeContrasena_inicio.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
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
                if(!emailvalidator.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailvalidator).matches()){
                    Toast.makeText(view.getContext(), "Email valido", Toast.LENGTH_LONG).show();
                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("nombre", nombre.getText().toString());
                    user.put("usuario", usuario.getText().toString());
                    user.put("email", emailvalidator);
                    user.put("contrasena", contrasena.getText().toString());
                    // Add a new document with a generated ID
                    db.collection("Clientes")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Navigation.findNavController(v).navigate(R.id.action_crearUsuario_Cliente_to_inicioSesion_Cliente);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                } else{
                    Toast.makeText(view.getContext(), "Email no valido", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

}