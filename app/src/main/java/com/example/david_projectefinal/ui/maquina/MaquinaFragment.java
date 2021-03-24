package com.example.david_projectefinal.ui.maquina;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;

import java.net.URI;
import java.util.Calendar;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class MaquinaFragment extends Fragment {
    Context context;
    static String nom, numSerie;
    String dataFINAL;
    private filtratge filtreAplicat;
    //Contingut adapter per listview
    String[] columnesMaquina = new String[]{
            BuidemDataSource.iD,
            BuidemDataSource.nomM,
            BuidemDataSource.adreçaM,
            BuidemDataSource.codiPostalM,
            BuidemDataSource.poblacioM,
            BuidemDataSource.tlfM,
            BuidemDataSource.emailM,
            BuidemDataSource.numM,
            BuidemDataSource.dataM,
            BuidemDataSource.tipusForeign,
            BuidemDataSource.zonaForeign
    };
    int[] toMaquina = new int[]{
            R.id.lblId,
            R.id.lblNomCli,
            R.id.lblAdreça,
            R.id.lblCodiPostal,
            R.id.lblPobla,
            R.id.lblTlf,
            R.id.lblEmail,
            R.id.lblNumSer,
            R.id.lblDataUR,
            R.id.lblTipus,
            R.id.lblZona
    };
    ////////////////

    //Image views
    ImageView addMaquina, deleteMaquina, imageTrucada;
    public static BuidemDataSource bd;
    Context mycontext;
    public static adapterTodoIcon dataAdapter;
    MCalendarView calendarView ;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Instanciem la base de dades amb aquest contexte
        bd = new BuidemDataSource(getContext());
        //Definim la vista i la inflem amb el fragment de maquina
        View myview = inflater.inflate(R.layout.fragment_maquina, container, false);

        filtreAplicat = filtratge.FILTRE_TOT;
        deleteMaquina = (ImageView) myview.findViewById(R.id.imgdelete123);
        implementacioListView(myview);
        addMaquinaButon();
        final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CALL_PHONE)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
        }
        return myview;
    }

    public void deleteMaquina(long idF, ViewGroup parent) {
        context = parent.getContext();
        bdCursorDelete(idF);
        String nomF, numF;
        nomF = nom;
        numF = numSerie;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("¿Estas segur que vols eliminar la màquina amb el codi: " + idF + " amb el nom de client " + nomF + " i amb número de serie " + numF + "?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                MaquinaFragment.bdEliminar(idF);
                filtreAplicat = filtratge.FILTRE_TOT;
                actualitzarProductes();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();

    }

    private static void bdCursorDelete(long idF) {
        Cursor cur = bd.agafarMaquinaUna(idF);
        cur.moveToFirst();
        nom = cur.getString(cur.getColumnIndex(BuidemDataSource.nomM));
        numSerie = cur.getString(cur.getColumnIndex(BuidemDataSource.numM));

    }

    private static void bdEliminar(long idF) {
        bd.eliminarMaquina(idF);
    }


    public void addMaquinaButon() {
        addMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddMaquina();
            }
        });
    }

    public void implementacioListView(View myview) {
        //Definim una imatge per aplicarli amb el GLIDE un GIF a la imatge
        addMaquina = (ImageView) myview.findViewById(R.id.imageAddMaquina);
        Glide.with(getContext()).load(R.drawable.addmac).into(addMaquina);
        //Instanciem el listView
        listView = (ListView) myview.findViewById(R.id.list1);

        Cursor cursor = bd.mostrarAllMaquines();

        dataAdapter = new adapterTodoIcon(getContext(),
                R.layout.row_estructuramaquines,
                cursor,
                columnesMaquina,
                toMaquina,
                1, MaquinaFragment.this);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                editarAddMaquina(id);

            }
        });
    }

    public void data(View v)
    {
        Calendar cal = Calendar.getInstance();
        int any = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        final EditText etData = v.findViewById(R.id.etData);
        DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthaux = month + 1;
                dataFINAL = dayOfMonth + "-" + monthaux + "-" + year;
                etData.setEnabled(false);
                etData.setText(dataFINAL);
            }
        }, any, mes, dia);
        dpd.show();
    }
    public void dialogAddMaquina() {

        AlertDialog.Builder Maquina = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v1 = inflater.inflate(R.layout.addmaquina, null);
        final EditText etData = v1.findViewById(R.id.etData);
        etData.setEnabled(false);
        ImageView calendar = (ImageView) v1.findViewById(R.id.imageViewData);
        Glide.with(getContext()).load(R.drawable.cal).into(calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data(v1);
            }
        });

        final EditText etNom = v1.findViewById(R.id.etNom);
        final EditText etDir = v1.findViewById(R.id.etDir);
        final EditText etCodiPos = v1.findViewById(R.id.etCodiPostal);
        final EditText etPobl = v1.findViewById(R.id.etPoblacio);
        final EditText etTlf = v1.findViewById(R.id.etTelefon);
        final EditText etEmail = v1.findViewById(R.id.etEmail);
        final EditText etNumSer = v1.findViewById(R.id.etNumSer);

        final EditText etTipus = v1.findViewById(R.id.etTipus);
        final EditText etZona = v1.findViewById(R.id.etZona);


        Maquina.setView(v1).setPositiveButton("Afegir Màquina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nom = etNom.getText().toString();
                String adreça = etDir.getText().toString();
                String codiPos = etCodiPos.getText().toString();
                int codiConvert = Integer.parseInt(codiPos);
                String pob = etPobl.getText().toString();
                String tlf = etTlf.getText().toString();
                String email = etEmail.getText().toString();
                String numser = etNumSer.getText().toString();
                String data = etData.getText().toString();
                String tips = etTipus.getText().toString();
                String zons = etZona.getText().toString();
                bd.addMaquina(nom, adreça, codiConvert, pob, tlf, email, numser, data, tips, zons);
                actualitzarProductes();
            }
        });
        Maquina.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Maquina.create();
        dialog.show();


    }

    public void editarAddMaquina(long id) {

        AlertDialog.Builder Maquina = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.addmaquina, null);

        ImageView calendar = (ImageView) v2.findViewById(R.id.imageViewData);
        Glide.with(getContext()).load(R.drawable.cal).into(calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data(v2);
            }
        });
        Cursor updateMaquina = bd.agafarMaquinaUna(id);
        updateMaquina.moveToFirst();

        final EditText etNom = v2.findViewById(R.id.etNom);
        etNom.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.nomM)));
        final EditText etDir = v2.findViewById(R.id.etDir);
        etDir.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.adreçaM)));
        final EditText etCodiPos = v2.findViewById(R.id.etCodiPostal);
        etCodiPos.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.codiPostalM)));
        final EditText etPobl = v2.findViewById(R.id.etPoblacio);
        etPobl.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.poblacioM)));
        final EditText etTlf = v2.findViewById(R.id.etTelefon);
        etTlf.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM)));
        final EditText etEmail = v2.findViewById(R.id.etEmail);
        etEmail.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM)));
        final EditText etNumSer = v2.findViewById(R.id.etNumSer);
        etNumSer.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM)));
        final EditText etData = v2.findViewById(R.id.etData);
        etData.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.dataM)));
        etData.setEnabled(false);
        final EditText etTipus = v2.findViewById(R.id.etTipus);
        etTipus.setEnabled(false);
        etTipus.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tipusForeign)));
        final EditText etZona = v2.findViewById(R.id.etZona);
        etZona.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.zonaForeign)));
        etZona.setEnabled(false);
        etData.setEnabled(false);
        etData.setText(dataFINAL);
        Maquina.setView(v2).setPositiveButton("Modificar Màquina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nom = etNom.getText().toString();
                String adreça = etDir.getText().toString();
                String codiPos = etCodiPos.getText().toString();
                int codiConvert = Integer.parseInt(codiPos);
                String pob = etPobl.getText().toString();
                String tlf = etTlf.getText().toString();
                String email = etEmail.getText().toString();
                String numser = etNumSer.getText().toString();
                String data = etData.getText().toString();
                String tips = etTipus.getText().toString();
                String zons = etZona.getText().toString();
                bd.updateMaquina(id, nom, adreça, codiConvert, pob, tlf, email, numser, data, tips, zons);
                actualitzarProductes();
            }
        });
        Maquina.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Maquina.create();
        dialog.show();


    }

    private void actualitzarProductes() {

        // Demanem totes les tasques
        Cursor cursorMaquines = bd.mostrarAllMaquines();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filtreAplicat) {
            case FILTRE_TOT:
                cursorMaquines = bd.mostrarAllMaquines();
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

}

class adapterTodoIcon extends android.widget.SimpleCursorAdapter {

    private static final String colorTaskPending = "#F04C4C";
    private static final String colorTaskCompleted = "#FFFFFF";

    private MaquinaFragment aTiconProduct;

    public adapterTodoIcon(Context context, int layout, Cursor c, String[] from, int[] to, int flags, MaquinaFragment frag) {
        super(context, layout, c, from, to, flags);
        aTiconProduct = frag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView imageEmail = (ImageView) view.findViewById(R.id.imgEmail);
        ImageView imageTrucada = (ImageView) view.findViewById(R.id.imgTlf);
        ImageView botoEliminarProducte = (ImageView) view.findViewById(R.id.imgdelete123);
        botoEliminarProducte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);
                aTiconProduct.deleteMaquina(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);
            }
        });
        imageTrucada.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                ferTrucada(v, position);
            }
        });
        imageEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                enviarEmail(position);
            }
        });

        return view;
    }
    public void enviarEmail(int position) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconProduct.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String sEmail = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM));
        String sNums = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM));
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{sEmail});
        email.putExtra(Intent.EXTRA_SUBJECT, "“Propera revisió màquina nº " + sNums);
        //email.putExtra(Intent.EXTRA_TEXT, "message");
        email.setType("message/rfc822");
        aTiconProduct.startActivity(Intent.createChooser(email, "Choose an Email client :"));
       // aTiconProduct.startActivity(email);
    }
    public void ferTrucada(View v, int position) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconProduct.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String tlfon = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM));
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + tlfon));
        aTiconProduct.startActivity(i);
    }
}