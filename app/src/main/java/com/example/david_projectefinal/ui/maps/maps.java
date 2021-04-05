package com.example.david_projectefinal.ui.maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.david_projectefinal.R;
import com.example.david_projectefinal.ui.maquina.MaquinaFragment;
import com.example.david_projectefinal.ui.zona.ZonaFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class maps extends Fragment {
    String ciutat1;
    List<Address> adress = null;
    int maxResultados = 1;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //agafarNom();
            Geocoder geo = new Geocoder(getContext());

            //ciutat1 = "mataro";

            try {
                adress = geo.getFromLocationName(ciutat1, maxResultados);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LatLng ciutat = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
            googleMap.addMarker(new MarkerOptions().position(ciutat).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciutat,10));
        }
    };
    public void agafarNom()
    {
       /* Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        ciutat1 = "mataro";//datosRecuperados.getString("nom");

        getParentFragmentManager().setFragmentResultListener("key", this, new MaquinaFragment() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result...
            }
        });*/
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ciutat1 = getArguments().getString("nom");

        /*Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }

        // Y ahora puedes recuperar usando get en lugar de put

        String nombre = datosRecuperados.getString("nom");*/

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map44);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
