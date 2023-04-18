package com.example.hairdate;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_principal_usuario_cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_principal_usuario_cliente extends Fragment implements OnMapReadyCallback {

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

        // Obtener el objeto GoogleMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Obtener la lista de peluquerías cercanas
        peluquerias = obtenerPeluqueriasCercanas();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal_usuario_cliente, container, false);
    }

    private GoogleMap mMap;

    private List<Peluqueria> peluquerias; // lista de peluquerías cercanas

    private List<Peluqueria> obtenerPeluqueriasCercanas() {
        List<Peluqueria> peluquerias = new ArrayList<>();

        // Crear una instancia de la API de Google Places
        PlacesApi api = new PlacesApi.Builder()
                .apiKey("AIzaSyBf2GhzY-ggzbGnAjkrvjQkewgP1fBveU4")
                .build();

        // Obtener la ubicación actual del usuario
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitud = location.getLatitude();
        double longitud = location.getLongitude();

        // Buscar peluquerías cercanas a la ubicación del usuario
        List<Place> places = api.searchPlacesNearby(latitud, longitud, 5000, "hair care");

        // Convertir los resultados de la búsqueda en objetos Peluqueria
        for (Place place : places) {
            Peluqueria peluqueria = new Peluqueria();
            peluqueria.setNombre(place.getName());
            peluqueria.setDireccion(place.getAddress());
            peluqueria.setLatitud(place.getLatitude());
            peluqueria.setLongitud(place.getLongitude());
            peluquerias.add(peluqueria);
        }

        return peluquerias;
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
        double latitud = 0;
        double longitud = 0;
        LatLng ubicacionUsuario = new LatLng(latitud, longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 15));
    }


}