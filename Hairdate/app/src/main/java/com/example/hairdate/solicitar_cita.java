package com.example.hairdate;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class solicitar_cita extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnSelectDateTime;
    private FirebaseFirestore db;


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

        // Inicializar las vistas
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        btnSelectDateTime = view.findViewById(R.id.btnSelectDateTime);

        // Obtener referencia a la base de datos de Firebase
        db = FirebaseFirestore.getInstance();

        // Configurar límites para la selección de la fecha y hora
        // Configurar límites para la selección de la fecha y hora
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(currentYear, currentMonth, currentDay, 9, 0);
        long minDateTime = calendar.getTimeInMillis();
        calendar.set(2023, Calendar.DECEMBER, 31, 20, 0); // Establecer la fecha máxima como el 31/12/2023
        long maxDateTime = calendar.getTimeInMillis();
        datePicker.setMinDate(System.currentTimeMillis()); // Establecer la fecha mínima como hoy
        datePicker.setMaxDate(maxDateTime);

        // Configurar el intervalo de tiempo para el TimePicker
        timePicker.setIs24HourView(true);
        timePicker.setMinute(0);
        timePicker.setHour(9);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (minute % 30 != 0) {
                    // Mostrar Toast si la hora seleccionada no está en intervalos de 30 minutos
                    showToast("Debes seleccionar una hora en intervalos de 30 minutos");
                }
            }
        });

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Actualizar la fecha seleccionada
                calendar.set(year, monthOfYear, dayOfMonth);

                // Obtener la fecha seleccionada en el formato deseado
                String selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

                // Resto del código para manejar la fecha seleccionada...
                // Por ejemplo, puedes mostrarla en un Toast
                showToast("Fecha seleccionada: " + selectedDateString);

                // Crear el Bundle y establecer el FragmentResult
                Bundle result = new Bundle();
                result.putString("selectedDate", selectedDateString);
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });

        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedYear = datePicker.getYear();
                int selectedMonth = datePicker.getMonth();
                int selectedDay = datePicker.getDayOfMonth();
                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();

                // Validar si la hora seleccionada está dentro de la franja horaria permitida
                if (selectedHour < 9 || selectedHour > 20) {
                    showToast("Debes seleccionar una hora entre las 09:00 y las 20:00");
                    return;
                }

                if (selectedMinute % 30 != 0) {
                    showToast("Debes seleccionar una hora en intervalos de 30 minutos");
                    return;
                }

                calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
                String selectedDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime());

                Bundle result = new Bundle();
                result.putString("selectedDateTime", selectedDateTimeString);

                // Verificar si la fecha y hora están en la base de datos de Firebase
                Query queryPrueba = db.collection("Citas").whereEqualTo("Fecha_Hora", selectedDateTimeString);
                queryPrueba.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                if (querySnapshot.isEmpty()) {
                                    // No se encontraron documentos con la fecha y hora especificadas
                                    getParentFragmentManager().setFragmentResult("requestKey", result);
                                    Navigation.findNavController(getView()).navigate(R.id.action_solicitar_cita_to_preferenciaCitasCliente);
                                } else {
                                    // Se encontró al menos un documento con la fecha y hora especificadas
                                    showToast("En esta fecha y hora ya hay una cita programada");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("MyApp", "Error al obtener los documentos: ", e);
                            }
                        });
            }
        });


        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}