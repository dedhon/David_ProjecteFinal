package com.example.david_projectefinal.ui.zona;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;
import com.example.david_projectefinal.ui.maps.maps;
import com.example.david_projectefinal.ui.tipus.TipusFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ZonaFragment extends Fragment {
    public static BuidemDataSource bd;
    static String nom;
    Snackbar snackbar;
    final String colorVermell = "#FF0000";
    final String colorVerd = "#4BFE26";
    public void missatgeSnackBar(View v, String missatge, String color) {
        snackbar = Snackbar.make(v, missatge, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();
    }
    String[] columnesZones = new String[]{
            BuidemDataSource.nomZ
    };
    int[] toZones = new int[]{
            R.id.lblNomZona
    };
    ListView listView;
    View root;

    public static adaptadorZona dataAdapter;
    private filtratge filtreAplicat;
    private void actualitzarZona() {

        // Demanem totes les tasques
        Cursor cursorMaquines = bd.mostrarAllZones();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filtreAplicat) {
            case FILTRE_TOT:
                cursorMaquines = bd.mostrarAllZones();
                break;
        }
        // Now create a simple cursor adapter and set it to display
        dataAdapter.changeCursor(cursorMaquines);
        dataAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionbar_zona, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMacZ: {
                crearZona();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_zona, container, false);

        bd = new BuidemDataSource(getContext());
        filtreAplicat = filtratge.FILTRE_TOT;
        implementacioListView(root);

        setHasOptionsMenu(true);
        return root;
    }

    public void crearZona()
    {
        AlertDialog.Builder Zona = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_zona, null);


        final EditText etNomZona = v2.findViewById(R.id.etZonaNom);
        Zona.setView(v2).setPositiveButton("Afegir Zona", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomZona = etNomZona.getText().toString();
                if (nomZona.trim().equals("")) {
                    missatgeSnackBar(root, "El nom ??s obligatori!!", colorVermell);
                    return;
                }
                else{
                    boolean compNomZ = bd.mirarNomZonaRepe(nomZona);
                    if(compNomZ == false)
                    {
                        missatgeSnackBar(root, "El nom de la zona ja existeix!!", colorVermell);
                        return;
                    }
                }
                bd.addZonas(nomZona);
                missatgeSnackBar(root, "La zona s'ha creat correctament!!", colorVerd);
                actualitzarZona();

            }
        });
        Zona.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Zona.create();
        dialog.show();

    }
    public void implementacioListView(View root) {

        //Instanciem el listView
        listView = (ListView) root.findViewById(R.id.list1);

        Cursor cursor = bd.mostrarAllZones();

        dataAdapter = new adaptadorZona(getContext(),
                R.layout.row_estructurazones,
                cursor,
                columnesZones,
                toZones,
                1, ZonaFragment.this);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                editarZona(id);
            }
        });
    }
    public void editarZona(long id)
    {
        AlertDialog.Builder Zona = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_zona, null);

        Cursor updateZona = bd.agafarZonesUn(id);
        updateZona.moveToFirst();
        TextView titol = (TextView)v2.findViewById(R.id.idAfegirZona);
        titol.setText("Actualitzar Zona");
        final EditText etNom = v2.findViewById(R.id.etZonaNom);
        etNom.setText(updateZona.getString(updateZona.getColumnIndex(BuidemDataSource.nomZ)));

        Zona.setView(v2).setPositiveButton("Modificar Zona", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    missatgeSnackBar(root, "El nom ??s obligatori!!", colorVermell);
                    return;
                }
                boolean aux = bd.updateZones(id, nom);
                if(aux==true)
                {
                    missatgeSnackBar(root, "El nom de la zona ja existeix!!", colorVermell);
                }
                else{
                    missatgeSnackBar(root, "La zona s'ha actualitzat correctament!!", colorVerd);
                }

                actualitzarZona();
            }
        });
        Zona.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Zona.create();
        dialog.show();

    }
    private static void bdCursorDelete(long idF) {
        Cursor cursor = bd.agafarZonesUn(idF);
        cursor.moveToFirst();
        nom = cursor.getString(cursor.getColumnIndex(BuidemDataSource.nomZ));

    }
    private static void bdEliminarZona(long idF) {
        bd.eliminarZones(idF);
    }
    public void deleteZona(long idF, ViewGroup parent) {

        bdCursorDelete(idF);
        String nomF;
        nomF = nom;

        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.sample, null);
        ImageView segur = (ImageView) view.findViewById(R.id.dialog_imageview);
        Glide.with(getContext()).load(R.drawable.segurgif).into(segur);
        alertadd.setView(view);
        String missatgeAenviar="??Estas segur que vols eliminar la zona amb les seg??ents dades:?" + "\n" + " " + "\n" +

                "-Nom Zona: " + nomF + "\n";
        alertadd.setMessage(missatgeAenviar);
        alertadd.setNeutralButton("Si!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                boolean buscaEiliminar = bd.mirarSiZonaAssignat(idF);

                if(buscaEiliminar==false)
                {
                    ZonaFragment.bdEliminarZona(idF);
                    filtreAplicat = filtratge.FILTRE_TOT;
                    actualitzarZona();
                    missatgeSnackBar(root, "La zona s'ha eliminat correctament!!", colorVerd);
                }
                else{
                    if(buscaEiliminar==true)
                    {
                        missatgeSnackBar(root, "La zona esta assignada, no pots eliminarla!!", colorVermell);
                    }
                }
            }
        });
        alertadd.setNegativeButton("No", null);
        alertadd.show();
    }
}
class adaptadorZona extends android.widget.SimpleCursorAdapter {

    private ZonaFragment aTiconZona;

    public adaptadorZona(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ZonaFragment fragZ) {
        super(context, layout, c, from, to, flags);
        aTiconZona = fragZ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView botoEliminarProducte = (ImageView) view.findViewById(R.id.imgdelete12345);
        ImageView botoMaps = (ImageView) view.findViewById(R.id.imgMapsZona);
        botoEliminarProducte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posici??.
                Cursor linia = (Cursor) getItem(position);
                aTiconZona.deleteZona(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);
            }
        });
        botoMaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posici??.
                Cursor linia = (Cursor) getItem(position);

                Bundle b = new Bundle();
                String nomIcolor = null;
                nomIcolor=String.valueOf(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
                b.putString("nomZ", nomIcolor);

                NavHostFragment.findNavController(aTiconZona.getParentFragment()).navigate(R.id.action_navigation_zona_to_navigation_maps,b);

            }
        });
       /* Bundle result = new Bundle();
        result.putString("bundleKey", "result");
        getParentFragmentManager().setFragmentResult("requestKey", result);*/
        return view;
    }

}