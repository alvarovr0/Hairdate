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

import com.example.hairdate.model.Peluquero;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaPeluqueros#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPeluqueros extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    FirebaseFirestore db;
    RecyclerView recyclerView;
    PeluqueroAdapter adapter;
    private StorageReference storageReference;

    public ListaPeluqueros() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaPeluqueros.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaPeluqueros newInstance(String param1, String param2) {
        ListaPeluqueros fragment = new ListaPeluqueros();
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
        View view = inflater.inflate(R.layout.fragment_lista_peluqueros, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //profileImage = view.findViewById(R.id.img_imagenPerfilCliente);
        recyclerView = view.findViewById(R.id.recyclerViewPeluqueros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obtener el UID del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Consultar las peluquerías favoritas del usuario
            Query queryBarber = db.collection("Peluquero");
            FirestoreRecyclerOptions<Peluquero> firestoreRecyclerOptionsFav = new FirestoreRecyclerOptions.Builder<Peluquero>().setQuery(queryBarber, Peluquero.class).build();

            adapter = new PeluqueroAdapter(firestoreRecyclerOptionsFav);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            adapter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    Peluquero peluquero = adapter.getItem(position);

                    // Navegar al siguiente fragmento y pasar los datos como argumentos
                    Bundle args = new Bundle();
                    args.putString("especialidad", peluquero.getEspecialidad());
                    args.putString("horario", peluquero.getHorario());
                    args.putString("nombre", peluquero.getNombre());
                    getParentFragmentManager().setFragmentResult("resquestKey", args);
                    Navigation.findNavController(view).navigate(R.id.action_listaPeluqueros_to_peluquero_detalle, args);
                }
            });
        }
        return view;
    }
}