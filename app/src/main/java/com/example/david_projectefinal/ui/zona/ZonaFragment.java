package com.example.david_projectefinal.ui.zona;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;
import com.example.david_projectefinal.ui.maps.maps;
import com.example.david_projectefinal.ui.tipus.TipusFragment;


public class ZonaFragment extends Fragment {
    public static BuidemDataSource bd;
    static String nom;
    String[] columnesZones = new String[]{
            BuidemDataSource.iD,
            BuidemDataSource.nomZ
    };
    int[] toZones = new int[]{
            R.id.lblIdzona,
            R.id.lblNomZona
    };
    ListView listView;
    ImageView addZona;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_zona, container, false);
        addZona = (ImageView) root.findViewById(R.id.imageAddZona);
        bd = new BuidemDataSource(getContext());
        filtreAplicat = filtratge.FILTRE_TOT;
        implementacioListView(root);
        addZona(root);

        return root;
    }
    public void addZona(View v) {
        addZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearZona();
            }
        });
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
                    Toast.makeText(getContext(),"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    boolean compNomZ = bd.mirarNomZonaRepe(nomZona);
                    if(compNomZ == false)
                    {
                        Toast.makeText(getContext(), "El nom de la zona ja existeix!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                bd.addZonas(nomZona);
                actualitzarZona();

            }
        });
        Zona.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Zona.create();
        dialog.show();

    }
    public void implementacioListView(View root) {
        //Definim una imatge per aplicarli amb el GLIDE un GIF a la imatge
        ImageView adzona = (ImageView) root.findViewById(R.id.imageAddZona);
        Glide.with(getContext()).load(R.drawable.addmac).into(adzona);
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
                    Toast.makeText(getContext(),"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean aux = bd.updateZones(id, nom);
                if(aux==true)
                {
                    Toast.makeText(getContext(), "El nom de la zona ja existeix!!", Toast.LENGTH_SHORT).show();
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
        String missatgeAenviar="¿Estas segur que vols eliminar la zona amb les següents dades:?" + "\n" +
                "-ID Zona: " + idF + "\n" +
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
                }
                else{
                    if(buscaEiliminar==true)
                    {
                        Toast.makeText(getContext(),"La zona esta assignada, no pots eliminarla!!", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        alertadd.setNegativeButton("No", null);
        alertadd.show();

    }
}
class adaptadorZona extends android.widget.SimpleCursorAdapter {

    private static final String colorTaskPending = "#F04C4C";
    private static final String colorTaskCompleted = "#FFFFFF";

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

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);
                aTiconZona.deleteZona(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);
            }
        });
        botoMaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);
                Fragment nuevoFragmento = new maps();
                Bundle b = new Bundle();
                b.putString("nom", "granada");

                nuevoFragmento.setArguments(b);
                FragmentTransaction fragmentTransaction = aTiconZona.getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, nuevoFragmento);
                fragmentTransaction.addToBackStack(null);

                // Commit a la transacción
                fragmentTransaction.commit();
            }
        });
       /* Bundle result = new Bundle();
        result.putString("bundleKey", "result");
        getParentFragmentManager().setFragmentResult("requestKey", result);*/
        return view;
    }

}