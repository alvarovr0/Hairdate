package com.example.hairdate;

<<<<<<< Updated upstream
=======
import android.app.AlertDialog;
>>>>>>> Stashed changes
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

<<<<<<< Updated upstream
=======
import androidx.annotation.NonNull;
>>>>>>> Stashed changes
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

/**
 * A simple {@link Fragment} subclass.
<<<<<<< Updated upstream
 * Use the {@link principal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class principal extends Fragment {
    /*
    *
    * Este Fragment nos servirá para que el usuario seleccione una opción, ya sea peluquero o cliente y se le mandará un login distinto
    *
    */
=======
 * Use the {@link Principal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Principal extends Fragment {

>>>>>>> Stashed changes
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button botonPeluquero;
<<<<<<< Updated upstream
    private Button botonCliente;

    public principal() {
=======

    private Button botonCliente;

    public Principal() {
>>>>>>> Stashed changes
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
<<<<<<< Updated upstream
     * @return A new instance of fragment principal.
     */
    // TODO: Rename and change types and number of parameters
    public static principal newInstance(String param1, String param2) {
        principal fragment = new principal();
=======
     * @return A new instance of fragment Principal.
     */
    // TODO: Rename and change types and number of parameters
    public static Principal newInstance(String param1, String param2) {
        Principal fragment = new Principal();
>>>>>>> Stashed changes
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
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        botonPeluquero = (Button)view.findViewById(R.id.btn_pelu);
        botonPeluquero.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
<<<<<<< Updated upstream
                //Esto nos servirá para crear una ventana de diálogo para asegurar que si está seguro de la opción elegida
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) principal.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Seguro que eres peluquero?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(getView()).navigate(R.id.action_principal_to_inicioSesion_Peluquero);
                        //Falla aquí despues de comprobar que no hay ningún usuario que haya iniciado sesión
=======
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context)Principal.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Seguro que eres peluquero?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(view).navigate(R.id.action_principal_to_inicioSesion_Peluquero);
>>>>>>> Stashed changes
                    }
                })).setNegativeButton((CharSequence)"No", (android.content.DialogInterface.OnClickListener)null);
                android.app.AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence)"¿Quieres cambiar de ventana?");
                alerta.show();
            }
        }));

        botonCliente = (Button)view.findViewById(R.id.btn_cli);
        botonCliente.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
<<<<<<< Updated upstream
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context) principal.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Seguro que eres cliente?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(it).navigate(R.id.action_principal_to_inicioSesion_Cliente);
=======
                android.app.AlertDialog.Builder constructorDialogo = new android.app.AlertDialog.Builder((Context)Principal.this.requireActivity());
                constructorDialogo.setMessage((CharSequence)"¿Seguro que eres cliente?").setCancelable(false).setPositiveButton((CharSequence)"Sí", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogo, int id) {
                        Navigation.findNavController(view).navigate(R.id.action_principal_to_inicioSesion_Cliente);
>>>>>>> Stashed changes
                    }
                })).setNegativeButton((CharSequence)"No", (android.content.DialogInterface.OnClickListener)null);
                android.app.AlertDialog alerta = constructorDialogo.create();
                alerta.setTitle((CharSequence)"¿Quieres cambiar de ventana?");
                alerta.show();
            }
        }));
    }
}