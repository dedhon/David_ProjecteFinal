package com.example.david_projectefinal.ui.maps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.DadaMaps;
import com.example.david_projectefinal.Main;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.Temps;
import com.example.david_projectefinal.Weather;
import com.example.david_projectefinal.ui.tipus.TipusFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class maps extends Fragment {
    Weather wea1;
    View laView;
    public BuidemDataSource bd;
    String ciutat1 = null;
    List<Address> adress = null;
    int maxResultados = 1;
    String[] dadesRebudes;
    ArrayList<DadaMaps> dades;
    ListView listView;
    String marksVaries;
    ArrayList<Temps> dadesTempsMaps;
    String keyAPI="06760630e74a1ad03ea88374a3562edc";
    String idioma = "ca";
    String temp = null;
    String temp2,temp1;
    public void crearConexioApi(String ciutat,String color)
    {
        AsyncHttpClient tempsAsync = new AsyncHttpClient();
        tempsAsync.setMaxRetriesAndTimeout(0,10000);
        //Predefinim la URL amb una ciutat que ens ficara el usuari i la APIkey que hens donen al registrarnos
        String Url = "http://api.openweathermap.org/data/2.5/weather?q=" + ciutat + "&appid="+keyAPI+"&lang="+idioma;
        tempsAsync.get(getActivity(),Url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                JSONObject dadesTemps = null;

                String str = new String(responseBody);

                //Deserielitzem les dades del Json
                try {
                    dadesTemps = new JSONObject(str);
                    wea1 = new Weather(dadesTemps.getJSONArray("weather").getJSONObject(0).getInt("id"),
                            dadesTemps.getJSONArray("weather").getJSONObject(0).getString("main"),
                            dadesTemps.getJSONArray("weather").getJSONObject(0).getString("description"),
                            dadesTemps.getJSONArray("weather").getJSONObject(0).getString("icon"));
                    Main main1 = new Main(dadesTemps.getJSONObject("main").getDouble("temp"),
                            dadesTemps.getJSONObject("main").getDouble("feels_like"),
                            dadesTemps.getJSONObject("main").getDouble("temp_min"),
                            dadesTemps.getJSONObject("main").getDouble("temp_max"),
                            dadesTemps.getJSONObject("main").getDouble("pressure"),
                            dadesTemps.getJSONObject("main").getDouble("humidity"));

                    //Temps
                    double tempCelsius = main1.getTemp();
                    double tempCel = tempCelsius - 273.15;
                    String sCadena = String.valueOf(tempCel);
                    String sSubCadena = sCadena.substring(0,2);
                    temp = sSubCadena+"º";
                    //Conversio graus kelvin a celsius tempMax
                    double tempCelsius1 = main1.getTemp_max();
                    double tempCel1 = tempCelsius1 - 273.15;
                    String sCadena1 = String.valueOf(tempCel1);
                    String sSubCadena1 = sCadena1.substring(0,2);
                    temp1 = sSubCadena1+"º";

                    //Conversio graus kelvin a celsius tempMax
                    double tempCelsius2 = main1.getTemp_min();
                    double tempCel2 = tempCelsius2 - 273.15;
                    String sCadena2 = String.valueOf(tempCel2);
                    String sSubCadena2 = sCadena2.substring(0,2);
                    temp2 = sSubCadena2+"º";
                    String climaIcone = wea1.getMain();

                    Temps objecteTemps = new Temps(ciutat,temp,temp1,temp2,climaIcone,color);
                    dadesTempsMaps.add(objecteTemps);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {


            }
        });
    }
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
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciutat,8));
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    implementacioListView(laView);
                }
            },1000);


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
            crearConexioApi(ciutat1,dadesRebudes[2]);
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
                    crearConexioApi(pob,color);

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
        dadesTempsMaps = new ArrayList<Temps>();

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }
    public void implementacioListView(View root) {

        //Instanciem el listView
        listView = (ListView) root.findViewById(R.id.list2);

        MyAdapter myAdapter = new MyAdapter(getContext(), R.layout.row_temps, dadesTempsMaps,maps.this);
        listView.setAdapter(myAdapter);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laView=view;

        agafarNom();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map44);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

}
class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Temps> names;
    private maps aTimaps;

    public MyAdapter(Context context, int layout, ArrayList<Temps> dadesTemps,maps propi) {
        this.context = context;
        this.layout = layout;
        this.names = dadesTemps;
        aTimaps=propi;
    }

    @Override
    public int getCount() {
        return this.names.size();
    }

    @Override
    public Object getItem(int position) {
        return this.names.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Copiamos la vista
        View v = convertView;

        //Inflamos la vista con nuestro propio layout
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        v = layoutInflater.inflate(R.layout.row_temps, null);
        // Valor actual según la posición

        String nomPob = names.get(position).getPoblacio();
        String temperatura = names.get(position).getTemp();
        String tempMAX = names.get(position).getTempMax();
        String tempMIN = names.get(position).getTempMin();
        String icon = names.get(position).getIcon();
        String color = names.get(position).getColor();

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView tvPobl = (TextView) v.findViewById(R.id.lblCiutat);
        tvPobl.setText(String.valueOf(nomPob));
        TextView tvTemp = (TextView) v.findViewById(R.id.lblTemp);
        tvTemp.setText(String.valueOf(temperatura));
        TextView tvTempMax = (TextView) v.findViewById(R.id.lblMax);
        tvTempMax.setText(String.valueOf(tempMAX));
        TextView tvTempMin = (TextView) v.findViewById(R.id.lblMin);
        tvTempMin.setText(String.valueOf(tempMIN));
        ImageView logo = (ImageView)v.findViewById(R.id.imgIcontemps);
        tvPobl.setBackgroundColor(Color.parseColor(color));
        tvTemp.setBackgroundColor(Color.parseColor(color));
        tvTempMax.setBackgroundColor(Color.parseColor(color));
        tvTempMin.setBackgroundColor(Color.parseColor(color));


        switch(icon)
        {
            case "Rain":
                Glide.with(aTimaps.getContext()).load(R.drawable.gif123).into(logo);
                break;
            case "Clear":
                Glide.with(aTimaps.getContext()).load(R.drawable.gifsol).into(logo);
                break;
            case "Clouds":
                Glide.with(aTimaps.getContext()).load(R.drawable.gifnuvol1).into(logo);
                break;
            case "Mist":
                Glide.with(aTimaps.getContext()).load(R.drawable.neblina).into(logo);
                break;
            case "Fog":
                Glide.with(aTimaps.getContext()).load(R.drawable.neblina).into(logo);
                break;
            case "Haze":
                Glide.with(aTimaps.getContext()).load(R.drawable.neblina).into(logo);
                break;
        }






        return v;
    }
}