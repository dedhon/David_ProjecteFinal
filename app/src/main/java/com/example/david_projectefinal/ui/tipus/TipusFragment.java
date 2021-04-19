package com.example.david_projectefinal.ui.tipus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.MaquinaaddClass;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.TipusMaquina;
import com.example.david_projectefinal.filtratge;
import com.google.android.material.snackbar.Snackbar;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TipusFragment extends Fragment {
    Snackbar snackbar;
    final String colorVermell = "#FF0000";
    final String colorVerd = "#4BFE26";
    public void missatgeSnackBar(View v, String missatge, String color) {
        snackbar = Snackbar.make(v, missatge, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();
    }
    int mDefaultColor;
    String colorBDTipus = "";
    TextView tvColor;
    String[] columnesTipus = new String[]{
            BuidemDataSource.nomT,
            BuidemDataSource.colorT
    };
    int[] toTipus = new int[]{
            R.id.lblNomTipu,
            R.id.lblColor
    };
    static String nom;
    ListView listView;
    public static adaptadorTipus dataAdapter;
    public static BuidemDataSource bd;
    ImageView addTipus;
    private filtratge filtreAplicat;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_tipus, container, false);
        addTipus = (ImageView) root.findViewById(R.id.imageAddTipus);
        bd = new BuidemDataSource(getContext());
        filtreAplicat = filtratge.FILTRE_TOT;

        implementacioListView(root);
        setHasOptionsMenu(true);
        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionbar_tipus, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMacT: {
                crearTipus();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void crearTipus() {
        AlertDialog.Builder Tipus = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_tipus, null);

        butonColor(v2);
        final EditText etNomTip = v2.findViewById(R.id.etZonaNom);
        Tipus.setView(v2).setPositiveButton("Afegir Tipus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomTipus = etNomTip.getText().toString();
                if (nomTipus.trim().equals("")) {
                    missatgeSnackBar(root, "El nom és obligatori!!", colorVermell);
                    return;
                } else {
                    boolean compNom = bd.mirarNomTipusRepe(nomTipus);
                    if (compNom == false) {
                        missatgeSnackBar(root, "El nom del tipus ja existeix!!", colorVermell);
                        return;
                    }
                }
                bd.addTipus(nomTipus, colorBDTipus);
                missatgeSnackBar(root, "El tipus s'ha creat correctament!!", colorVerd);
                actualitzarTipus();
            }
        });
        Tipus.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Tipus.create();
        dialog.show();
    }

    public void butonColor(View v2) {
        Button btnSelectColorBg = (Button) v2.findViewById(R.id.btnSelectColorBg);

        mDefaultColor = ContextCompat.getColor(getContext(), R.color.design_default_color_primary);
        btnSelectColorBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(v2);
            }

        });
    }

    public void openColorPicker(View v2) {
        tvColor = (TextView) v2.findViewById(R.id.idColorView);

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                colorBDTipus = String.format("#%06X", (0xFFFFFF & color));
                tvColor.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
    }

    private void actualitzarTipus() {

        // Demanem totes les tasques
        Cursor cursorMaquines = bd.mostrarAllTipus();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filtreAplicat) {
            case FILTRE_TOT:
                cursorMaquines = bd.mostrarAllTipus();
                break;
        }
        // Now create a simple cursor adapter and set it to display
        dataAdapter.changeCursor(cursorMaquines);
        dataAdapter.notifyDataSetChanged();
    }

    public void implementacioListView(View root) {

        //Instanciem el listView
        listView = (ListView) root.findViewById(R.id.list1);

        Cursor cursor = bd.mostrarAllTipus();

        dataAdapter = new adaptadorTipus(getContext(),
                R.layout.row_estructuratipus,
                cursor,
                columnesTipus,
                toTipus,
                1, TipusFragment.this);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor curA = bd.agafarTipusUn(id);

                curA.moveToFirst();
                String colorin = curA.getString(2);
                editarTipus(id,colorin);
            }
        });
    }

    public void editarTipus(long id,String color) {

        AlertDialog.Builder Tipus = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_tipus, null);
        tvColor = (TextView) v2.findViewById(R.id.idColorView);

        Cursor updateTipus = bd.agafarTipusUn(id);
        updateTipus.moveToFirst();
        butonColor(v2);
        TextView titol = (TextView) v2.findViewById(R.id.idBuscar);
        titol.setText("Actualitzar tipus");

        final EditText etNom = v2.findViewById(R.id.etZonaNom);
        etNom.setText(updateTipus.getString(updateTipus.getColumnIndex(BuidemDataSource.nomT)));
        String colorHexa = color;
        if(colorHexa.equalsIgnoreCase(""))
        {

        }
        else{
            int colorInt = Color.parseColor(colorHexa);
            tvColor.setBackgroundColor(colorInt);
        }


        Tipus.setView(v2).setPositiveButton("Modificar Tipus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Comprobem que el nom al ser obligatori, tingui algun contingut
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    missatgeSnackBar(root, "El nom és obligatori!!", colorVermell);
                    return;
                }
                //Comprobem que el nom introduit no estigui repetit
                boolean aux = bd.updateTipus(id, nom, colorBDTipus);
                if (aux == true) {
                    missatgeSnackBar(root, "El nom del tipus ja existeix!!", colorVermell);
                }
                else{
                    missatgeSnackBar(root, "El tipus s'ha actualitzat correctament!!", colorVerd);
                }
                actualitzarTipus();
            }
        });
        Tipus.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Tipus.create();
        dialog.show();
    }

    private static void bdCursorDelete(long idF) {
        Cursor cursor = bd.agafarTipusUn(idF);
        cursor.moveToFirst();
        nom = cursor.getString(cursor.getColumnIndex(BuidemDataSource.nomT));

    }

    private static void bdEliminarTipus(long idF) {
        bd.eliminarTipus(idF);
    }

    public void deleteTipus(long idF, ViewGroup parent) {

        bdCursorDelete(idF);
        String nomF;
        nomF = nom;

        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.sample, null);
        ImageView segur = (ImageView) view.findViewById(R.id.dialog_imageview);
        Glide.with(getContext()).load(R.drawable.segurgif).into(segur);
        alertadd.setView(view);
        String missatgeAenviar = "¿Estas segur que vols eliminar el tipus amb les següents dades:?" + "\n" + " " + "\n" +

                "-Nom Tipus: " + nomF + "\n";
        alertadd.setMessage(missatgeAenviar);
        alertadd.setNeutralButton("Si!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                boolean buscaEiliminar = bd.mirarSiTipusAssignat(idF);

                if (buscaEiliminar == false) {
                    TipusFragment.bdEliminarTipus(idF);
                    filtreAplicat = filtratge.FILTRE_TOT;
                    actualitzarTipus();
                    missatgeSnackBar(root, "El tipus s'ha eliminat correctament!!", colorVerd);
                } else {
                    if (buscaEiliminar == true) {
                        missatgeSnackBar(root, "El tipus de màquina esta assignat, no pots eliminarlo!!", colorVermell);
                    }

                }

            }
        });
        alertadd.setNegativeButton("No", null);
        alertadd.show();

    }
}

class adaptadorTipus extends android.widget.SimpleCursorAdapter {

    private TipusFragment aTiconTipus;

    public adaptadorTipus(Context context, int layout, Cursor c, String[] from, int[] to, int flags, TipusFragment frag) {
        super(context, layout, c, from, to, flags);
        aTiconTipus = frag;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Cursor curAux = aTiconTipus.bd.mostrarAllTipus();
        TextView filaColor = (TextView) view.findViewById(R.id.lblColor);
        TextView filaTipus = (TextView) view.findViewById(R.id.lblNomTipu);
        Cursor linia =(Cursor) getItem(position);
        String proba1 = linia.getString(linia.getColumnIndex(BuidemDataSource.colorT));
        while(curAux.moveToNext())
        {
            if(proba1!=null)
            {
                if(proba1.equalsIgnoreCase(curAux.getString(curAux.getColumnIndex(BuidemDataSource.colorT))))
                {
                    String contingutFinal="";
                    contingutFinal = filaColor.getText().toString();
                    if(contingutFinal.equalsIgnoreCase(""))
                    {

                    }else{
                        filaColor.setBackgroundColor(Color.parseColor(curAux.getString(curAux.getColumnIndex(BuidemDataSource.colorT))));
                        filaTipus.setBackgroundColor(Color.parseColor(curAux.getString(curAux.getColumnIndex(BuidemDataSource.colorT))));
                    }
                }
            }



        }
        ImageView botoEliminarProducte = (ImageView) view.findViewById(R.id.imgdelete1234);
        botoEliminarProducte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);
                aTiconTipus.deleteTipus(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);
            }
        });
        return view;
    }

}