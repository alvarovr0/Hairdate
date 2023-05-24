package com.example.hairdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class solicitar_cita extends Fragment {
    private GridView gridView;
    private ArrayAdapter<String> adapter;
    private Calendar selectedDate;
    private List<Date> dates;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitar_cita, container, false);
        gridView = view.findViewById(R.id.gridView);
        selectedDate = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Obtener las fechas disponibles
        dates = getAvailableDates();

        // Crear el adaptador y configurarlo en el GridView
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getFormattedDates(dates));
        gridView.setAdapter(adapter);

        // Manejar el evento de selección de fecha
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedDate.setTime(dates.get(position));
            // Mostrar los campos correspondientes según la fecha seleccionada
            showFieldsForSelectedDate();
        });

        // Mostrar los campos iniciales (hoy)
        showFieldsForSelectedDate();

        return view;
    }

    private void showFieldsForSelectedDate() {
        // Obtener la fecha seleccionada en el formato deseado
        String selectedDateString = dateFormat.format(selectedDate.getTime());

        // Verificar si la fecha y hora seleccionadas ya han sido ocupadas en la base de datos
        final boolean[] isDateAndTimeUnavailable = {false};

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Citas").whereEqualTo("Fecha_Hora", selectedDateString);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null && !task.getResult().isEmpty()) {
                    // La fecha y hora seleccionadas están ocupadas
                    isDateAndTimeUnavailable[0] = true;
                } else {
                    // La fecha y hora seleccionadas no están ocupadas
                    isDateAndTimeUnavailable[0] = false;
                }
            } else {
                // Error al consultar la base de datos
                // Puedes manejar el error aquí, mostrar un mensaje de error, etc.
            }

            // Mostrar los campos correspondientes según la fecha seleccionada y su disponibilidad
            if (isDateAndTimeUnavailable[0]) {
                showUnavailableFields();
            } else {
                showDefaultFields();
            }
        });
    }

    private void showUnavailableFields() {
        // Mostrar los campos correspondientes cuando la fecha y hora seleccionadas no están disponibles
        Toast.makeText(requireContext(), "Esta fecha y hora no están disponibles", Toast.LENGTH_SHORT).show();
        // Puedes realizar alguna acción adicional aquí, como desactivar los campos de selección
    }

    private void showDefaultFields() {
        // Mostrar los campos por defecto para cualquier otra fecha
        List<String> fields = new ArrayList<>();
        fields.add("09:00");
        fields.add("09:30");
        fields.add("10:00");
        fields.add("10:30");
        fields.add("11:00");
        fields.add("11:30");
        fields.add("12:00");
        fields.add("12:30");
        fields.add("13:00");
        fields.add("13:30");
        fields.add("14:00");
        fields.add("14:30");
        fields.add("15:00");
        fields.add("15:30");
        fields.add("16:00");
        fields.add("16:30");
        fields.add("17:00");
        fields.add("17:30");
        fields.add("18:00");
        fields.add("18:30");
        fields.add("19:00");
        fields.add("19:30");
        fields.add("20:00");
        // Actualizar el adaptador con los campos correspondientes
        adapter.clear();
        adapter.addAll(fields);
        adapter.notifyDataSetChanged();
    }

    private List<Date> getAvailableDates() {
        // Obtener las fechas disponibles desde la base de datos o cualquier otra fuente de datos
        // Aquí se muestra un ejemplo con fechas de los próximos 7 días
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dates;
    }

    private List<String> getFormattedDates(List<Date> dates) {
        List<String> formattedDates = new ArrayList<>();
        for (Date date : dates) {
            formattedDates.add(dateFormat.format(date));
        }
        return formattedDates;
    }
}