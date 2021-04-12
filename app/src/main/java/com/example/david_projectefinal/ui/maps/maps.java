package com.example.david_projectefinal.ui.maps;

import android.database.Cursor;
import android.graphics.Color;
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

import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.DadaMaps;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.ui.maquina.MaquinaFragment;
import com.example.david_projectefinal.ui.zona.ZonaFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class maps extends Fragment {
    public BuidemDataSource bd;
    String ciutat1 = null;
    List<Address> adress = null;
    int maxResultados = 1;
    String[] dadesRebudes;
    ArrayList<DadaMaps> dades;
    private ArrayList<LatLng> locationArrayList;
    String marksVaries;
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


            if(dadesRebudes!=null)
            {
                try {
                    adress = geo.getFromLocationName(ciutat1, maxResultados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng ciutat = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                googleMap.addMarker(new MarkerOptions().position(ciutat).icon(getMarkerIcon(dadesRebudes[2])).title(dadesRebudes[3] + ", " + dadesRebudes[4]));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciutat,10));
            }
            else{
                if(marksVaries!=null)
                {
                    for(int i =0; i < dades.size();i++)
                    {

                        try {
                            adress = geo.getFromLocationName(dades.get(i).getPob(), maxResultados);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LatLng ciutat = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(ciutat).icon(getMarkerIcon(dades.get(i).getColor())).title(dades.get(i).getNumS() + ", " + dades.get(i).getNomT()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciutat,10));
                    }
                }
                else{
                    try {
                        adress = geo.getFromLocationName(ciutat1, maxResultados);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LatLng ciutat = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(ciutat).title("Madrid"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciutat,5));
                }
            }
        }
    };
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    public void agafarNom()
    {
        marksVaries = "";
        dadesRebudes = new String[5];
        locationArrayList = new ArrayList<>();
        if(getArguments()!=null)
        {
            dadesRebudes = getArguments().getStringArray("nom");
            marksVaries = getArguments().getString("nomZ");
        }
        else{
            dadesRebudes = null;
            marksVaries = null;
        }
        if(dadesRebudes!=null)
        {
            ciutat1= dadesRebudes[1];
        }
        else{
            if(marksVaries!=null)
            {
                long aux = Long.parseLong(marksVaries);
                Cursor curPoblAll = bd.agafarPoblacionsZones(aux);
                dades = new ArrayList<DadaMaps>();
                DadaMaps objDades;
                while(curPoblAll.moveToNext())
                {
                    String pob = curPoblAll.getString(curPoblAll.getColumnIndex(BuidemDataSource.poblacioM));
                    String numSer = curPoblAll.getString(curPoblAll.getColumnIndex(BuidemDataSource.numM));
                    String nomT = curPoblAll.getString(curPoblAll.getColumnIndex(BuidemDataSource.nomT));
                    String color = curPoblAll.getString(curPoblAll.getColumnIndex(BuidemDataSource.colorT));
                    objDades = new DadaMaps(pob,numSer,nomT,color);
                    dades.add(objDades);
                }
            }
            else{
                ciutat1= "Madrid";
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bd = new BuidemDataSource(getContext());

        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        agafarNom();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map44);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
