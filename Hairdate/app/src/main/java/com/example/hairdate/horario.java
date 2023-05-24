package com.example.hairdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link horario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class horario extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnSeleccionarHora;
    private TimePicker timePicker;
    private static Date fechaSeleccionada;

    public horario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment horario.
     */
    // TODO: Rename and change types and number of parameters
    public static horario newInstance(String param1, String param2) {
        horario fragment = new horario();
        Bundle args = new Bundle();
        args.putSerializable("fechaSeleccionada", fechaSeleccionada);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            fechaSeleccionada = (Date) arguments.getSerializable("fechaSeleccionada");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario, container, false);

        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);
        timePicker = view.findViewById(R.id.timePicker);

        // Configurar el formato de 24 horas en el TimePicker
        timePicker.setIs24HourView(true);

        btnSeleccionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                // Validar la hora seleccionada y la fecha
                if (validateHour(hour, minute) && validateDate(fechaSeleccionada, currentDate)) {
                    if (fechaSeleccionada != null) {
                        // Crear una nueva fecha con la hora seleccionada
                        Calendar calendarFechaHoraSeleccionada = Calendar.getInstance();
                        calendarFechaHoraSeleccionada.setTime(fechaSeleccionada);
                        calendarFechaHoraSeleccionada.set(Calendar.HOUR_OF_DAY, hour);
                        calendarFechaHoraSeleccionada.set(Calendar.MINUTE, minute);
                        Date fechaHoraSeleccionada = calendarFechaHoraSeleccionada.getTime();

                        // Crear un formato de fecha y hora
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        // Obtener la fecha y hora seleccionadas en formato de cadena
                        String fechaHoraSeleccionadaStr = dateFormat.format(fechaHoraSeleccionada);

                        // Crear un Bundle para pasar los datos al otro fragment
                        Bundle result = new Bundle();
                        result.putString("fechaHoraSeleccionada", fechaHoraSeleccionadaStr);
                        getParentFragmentManager().setFragmentResult("requestKey", result);

                        // Navegar al fragmento de PreferenciaCitasCliente y pasar los datos a través del Bundle
                        Navigation.findNavController(view).navigate(R.id.action_horario_to_preferenciaCitasCliente);
                    } else {
                        Toast.makeText(getContext(), "Fecha no válida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hora no válida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean validateHour(int hour, int minute) {
        return hour >= 9 && hour <= 20 && minute % 30 == 0;
    }

    private Date createDateTime(Date date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
    private boolean validateDate(Date selectedDate, Date currentDate) {
        // Aquí puedes implementar tu lógica de validación de fecha
        // Puedes comparar la fecha seleccionada con la fecha actual y aplicar tus reglas de validación
        // Por ejemplo, si deseas permitir solo fechas futuras, puedes usar la siguiente validación:
        return selectedDate.after(currentDate);
    }

}