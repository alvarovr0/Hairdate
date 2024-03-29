package com.example.hairdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hairdate.adapter.CitasAdapter;
import com.example.hairdate.model.Citas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitasLista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitasLista extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String nombreUsuario;
    String emailActual;
    boolean IrADetallerCitas;
    private TextView usuario;
    private Button btn_controlStock;
    private Button btn_cerrarSesion;
    private View view;
    private Button citas;
    ImageView profileImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    RecyclerView mRecycler;
    CitasAdapter mAdapter;
    FirebaseFirestore mFirestore;
    RecyclerView recyclerView;
    CitasAdapter adapter;

    public CitasLista() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Citas.
     */
    // TODO: Rename and change types and number of parameters
    public static CitasLista newInstance(String param1, String param2) {
        CitasLista fragment = new CitasLista();
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

        View view = inflater.inflate(R.layout.fragment_citas, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obtener fecha actual
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Formatear la fecha actual como "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        // Consultar todas las peluquerías
        Query query = db.collection("Citas").whereGreaterThanOrEqualTo("Fecha_Hora", formattedDate);;
        FirestoreRecyclerOptions<Citas> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Citas>().setQuery(query, Citas.class).build();
        adapter = new CitasAdapter(firestoreRecyclerOptions);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);

                Citas cita = adapter.getItem(position);

                String horarioCita = ((TextView) recyclerView.findViewHolderForAdapterPosition(recyclerView.getChildAdapterPosition(view)).itemView.findViewById(R.id.txt_horario)).getText().toString();
                // Envía el email y el horario a detalles_citas, para que este pueda cargar la cita correspondiente
                Bundle result = new Bundle();
                result.putString("email", emailActual);
                result.putString("horario", horarioCita);
                getParentFragmentManager().setFragmentResult("menuPeluquero_to_detallesCitas", result);
                Navigation.findNavController(view).navigate(R.id.action_citasLista_to_detalles_citas, result);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}