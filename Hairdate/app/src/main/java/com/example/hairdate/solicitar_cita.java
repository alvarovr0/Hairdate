package com.example.hairdate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class solicitar_cita extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnSeleccionarHora;
    private CalendarPickerView calendarPickerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public solicitar_cita() {
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
    public static solicitar_cita newInstance(String param1, String param2) {
        solicitar_cita fragment = new solicitar_cita();
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
        View view = inflater.inflate(R.layout.fragment_solicitar_cita, container, false);

        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);
        calendarPickerView = view.findViewById(R.id.calendarPickerView);
        Date today = new Date();
        Calendar minDate = Calendar.getInstance();
        minDate.setTime(today);
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTime(today);
        maxDate.add(Calendar.YEAR, 1);

        // Inicializar el calendario con las fechas mínima y máxima
        calendarPickerView.init(minDate.getTime(), maxDate.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(today);

        // Obtener la fecha seleccionada
        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                // Crear un formato de fecha
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Obtener la fecha seleccionada en formato de cadena
                String fechaSeleccionada = dateFormat.format(date);

                // Crear un Bundle para pasar los datos al otro fragment
                Bundle result = new Bundle();
                result.putString("fecha", fechaSeleccionada);
                getParentFragmentManager().setFragmentResult("requestKey", result);

                // Navegar al fragmento de Hora y pasar los datos a través del Bundle
                Navigation.findNavController(view).navigate(R.id.action_solicitar_cita_to_horario);
            }

            @Override
            public void onDateUnselected(Date date) {
                // No se necesita implementar en este caso
            }
        });
        return view;
    }

}