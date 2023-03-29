package com.example.hairdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_principal_usuario_cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_principal_usuario_cliente extends Fragment AppCompatActivity implements OnMapReadyCallback{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_principal_usuario_cliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_principal_usuario_cliente.
     */
    public static fragment_principal_usuario_cliente newInstance(String param1, String param2) {
        fragment_principal_usuario_cliente fragment = new fragment_principal_usuario_cliente();
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
        return inflater.inflate(R.layout.fragment_principal_usuario_cliente, container, false);
    }

        private GoogleMap mMap;
        private List<Peluqueria> peluquerias; // lista de peluquerías cercanas

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Obtener el objeto GoogleMap
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(this);

            // Obtener la lista de peluquerías cercanas
            peluquerias = obtenerPeluqueriasCercanas();
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Configurar el mapa
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // Mostrar las peluquerías en el mapa
            for (Peluqueria peluqueria : peluquerias) {
                LatLng ubicacion = new LatLng(peluqueria.getLatitud(), peluqueria.getLongitud());
                mMap.addMarker(new MarkerOptions().position(ubicacion).title(peluqueria.getNombre()));
            }

            // Centrar el mapa en la ubicación del usuario
            LatLng ubicacionUsuario = new LatLng(latitud, longitud);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 15));
        }

}