package com.example.david_projectefinal.ui.tipus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;


import java.util.List;

public class TipusFragment extends Fragment {
    String[] columnesTipus = new String[]{
            BuidemDataSource.iD,
            BuidemDataSource.nomT

    };
    int[] toTipus = new int[]{
            R.id.lblId,
            R.id.lblNomTipu
    };
    ListView listView;
    public static adaptadorTipus dataAdapter;
    public static BuidemDataSource bd;
    ImageView addTipus;
    private filtratge filtreAplicat;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tipus, container, false);
        addTipus = (ImageView) root.findViewById(R.id.imageAddTipus);
        bd = new BuidemDataSource(getContext());
        filtreAplicat = filtratge.FILTRE_TOT;
        addTipus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTipus(root);
            }
        });
        implementacioListView(root);
        addTipus(root);
        return root;
    }
    public void addTipus(View v) {
        addTipus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTipus();
            }
        });
    }
    public void crearTipus()
    {
        AlertDialog.Builder Tipus = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_tipus, null);


        final EditText etNomTip = v2.findViewById(R.id.etTipusMaq);
        Tipus.setView(v2).setPositiveButton("Afegir Tipus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomTipus = etNomTip.getText().toString();
                if (nomTipus.trim().equals("")) {
                    Toast.makeText(getContext(),"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                bd.addTipus(nomTipus);
                actualitzarTipus();
            }
        });
        Tipus.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Tipus.create();
        dialog.show();

    }
    private void actualitzarTipus() {

        // Demanem totes les tasques
        Cursor cursorMaquines = bd.mostrarAllMaquines();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filtreAplicat) {
            case FILTRE_TOT:
                cursorMaquines = bd.mostrarAllTipus();
                break;
            case FILTRE_NOM:
                // cursorMaquines = bd.filtreNomesDescripcio();
                break;
            case FILTRE_ADREÇA:
                //cursorMaquines = bd.filtreStockMesPetitZeroOigual();
                break;
            case FILTRE_CODI:
                //cursorMaquines = bd.filtreStockMesPetitZeroOigual();
                break;
            case FILTRE_POBLACIO:
                //cursorMaquines = bd.filtreStockMesPetitZeroOigual();
                break;
            case FILTRE_DATA:
                //cursorMaquines = bd.filtreStockMesPetitZeroOigual();
                break;
        }
        // Now create a simple cursor adapter and set it to display
        dataAdapter.changeCursor(cursorMaquines);
        dataAdapter.notifyDataSetChanged();
    }
    public void implementacioListView(View root) {
        //Definim una imatge per aplicarli amb el GLIDE un GIF a la imatge
        ImageView adtipus = (ImageView) root.findViewById(R.id.imageAddTipus);
        Glide.with(getContext()).load(R.drawable.addmac).into(adtipus);
        //Instanciem el listView
        listView = (ListView) root.findViewById(R.id.list1);

        Cursor cursor = bd.mostrarAllTipus();

        dataAdapter = new adaptadorTipus(getContext(),
                R.layout.row_estructuratipus,
                cursor,
                columnesTipus,
                toTipus,
                1,TipusFragment.this);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                //editarAddMaquina(id);
            }
        });
    }

}
class adaptadorTipus extends android.widget.SimpleCursorAdapter {

    private static final String colorTaskPending = "#F04C4C";
    private static final String colorTaskCompleted = "#FFFFFF";

    private TipusFragment aTiconTipus;

    public adaptadorTipus(Context context, int layout, Cursor c, String[] from, int[] to, int flags, TipusFragment frag) {
        super(context, layout, c, from, to, flags);
        aTiconTipus = frag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        return view;
    }

}