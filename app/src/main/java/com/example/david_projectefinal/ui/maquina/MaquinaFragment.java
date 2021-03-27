package com.example.david_projectefinal.ui.maquina;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.MaquinaaddClass;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


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
    private static int ADD_MAQUINA = 1;
    private static int UPDATA_MAQUINA = 2;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_MAQUINA) {
            if(resultCode==RESULT_OK)
            {
                actualitzarMaquines();
            }
        }
        if (requestCode == UPDATA_MAQUINA) {
            if(resultCode==RESULT_OK)
            {
                actualitzarMaquines();
            }
        }
    }
    ////////////////

    //Image views
    ImageView addMaquina, deleteMaquina, imageTrucada;
    public static BuidemDataSource bd;
    Context mycontext;
    public static adapterTodoIcon dataAdapter;

    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Instanciem la base de dades amb aquest contexte
        bd = new BuidemDataSource(getContext());
        //Definim la vista i la inflem amb el fragment de maquina
        View myview = inflater.inflate(R.layout.fragment_maquina, container, false);
        demanarPermisos();
        filtreAplicat = filtratge.FILTRE_TOT;
        deleteMaquina = (ImageView) myview.findViewById(R.id.imgdelete123);
        implementacioListView(myview);
        addMaquinaButon(myview);

        return myview;
    }
    public void demanarPermisos()
    {
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
    }
    public void deleteMaquina(long idF, ViewGroup parent) {

        context = parent.getContext();
        bdCursorDelete(idF);
        String nomF, numF;
        nomF = nom;
        numF = numSerie;

        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.sample, null);
        ImageView segur = (ImageView) view.findViewById(R.id.dialog_imageview);
        Glide.with(getContext()).load(R.drawable.segurgif).into(segur);
        alertadd.setView(view);
        String missatgeAenviar="¿Estas segur que vols eliminar la màquina amb les següents dades:?" + "\n" +
                "-ID Màquina: " + idF + "\n" +
                "-Nom client: " + nomF + "\n" +
                "-Número serie: " + numF;
        alertadd.setMessage(missatgeAenviar);
        alertadd.setNeutralButton("Si!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                MaquinaFragment.bdEliminar(idF);
                filtreAplicat = filtratge.FILTRE_TOT;
                actualitzarMaquines();
            }
        });
        alertadd.setNegativeButton("No", null);
        alertadd.show();

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


    public void addMaquinaButon(View v) {
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
    public void dialogAddMaquina()
    {

        Bundle bundle = new Bundle();
        long[] idActual = new long[2];
        idActual[0]=1;
        idActual[1]=0;
        bundle.putLongArray("FILTRE",idActual);
        Intent intent = new Intent(getActivity(), MaquinaaddClass.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,ADD_MAQUINA);
    }

    public void editarAddMaquina(long id) {

        Bundle bundle = new Bundle();
        long[] idActual = new long[2];
        idActual[0]=2;
        idActual[1]=id;
        bundle.putLongArray("FILTRE",idActual);
        Intent intent = new Intent(getActivity(), MaquinaaddClass.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,UPDATA_MAQUINA);

        /*AlertDialog.Builder Maquina = new AlertDialog.Builder(getContext());
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
        TextView titol = (TextView)v2.findViewById(R.id.idAfegirTipus);
        titol.setText("Actualitzar màquina");
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
        etTipus.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tipusForeign)));
        final EditText etZona = v2.findViewById(R.id.etZona);
        etZona.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.zonaForeign)));
        etData.setEnabled(false);
        etData.setText(dataFINAL);
        Maquina.setView(v2).setPositiveButton("Modificar Màquina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int codiConvert=0;
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    Toast.makeText(getContext(),"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String adreça = etDir.getText().toString();
                if (adreça.trim().equals("")) {
                    Toast.makeText(getContext(),"L'adreça és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String codiPos = etCodiPos.getText().toString();

                if (codiPos.trim().equals("")) {
                    Toast.makeText(getContext(),"El codi postal és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    codiConvert = Integer.parseInt(codiPos);
                }
                String pob = etPobl.getText().toString();
                if (pob.trim().equals("")) {
                    Toast.makeText(getContext(),"La població és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tlf = etTlf.getText().toString();
                String email = etEmail.getText().toString();
                String numser = etNumSer.getText().toString();
                if (numser.trim().equals("")) {
                    Toast.makeText(getContext(),"El número de serie és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String data = etData.getText().toString();
                String tips = etTipus.getText().toString();
                if (tips.trim().equals("")) {
                    Toast.makeText(getContext(),"El tipus és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String zons = etZona.getText().toString();
                if (zons.trim().equals("")) {
                    Toast.makeText(getContext(),"La zona és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bd.updateMaquina(id, nom, adreça, codiConvert, pob, tlf, email, numser, data, tips, zons);
                actualitzarMaquines();
            }
        });
        Maquina.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Maquina.create();
        dialog.show();*/


    }

    private void actualitzarMaquines() {

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

    private MaquinaFragment aTiconMaquina;

    public adapterTodoIcon(Context context, int layout, Cursor c, String[] from, int[] to, int flags, MaquinaFragment frag) {
        super(context, layout, c, from, to, flags);
        aTiconMaquina = frag;
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
                aTiconMaquina.deleteMaquina(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);
            }
        });
        imageTrucada.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                ferTrucada(view, position, aTiconMaquina.getContext());
            }
        });
        imageEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                enviarEmail(view,position,aTiconMaquina.getContext());
            }
        });

        return view;
    }
    public void enviarEmail(View v,int position,Context context) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconMaquina.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String sEmail = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM));
        if(!sEmail.trim().equals(""))
        {
            String sNums = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM));
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{sEmail});
            email.putExtra(Intent.EXTRA_SUBJECT, "“Propera revisió màquina nº " + sNums);
            email.setType("message/rfc822");
            aTiconMaquina.startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        else{
            Toast.makeText(context,"No hi ha e-mail al que enviar un correu!!", Toast.LENGTH_SHORT).show();

        }
    }
    public void ferTrucada(View v, int position, Context context) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconMaquina.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String tlfon = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM));
        if(!tlfon.trim().equals(""))
        {
            if(tlfon.length()==9)
            {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + tlfon));
                aTiconMaquina.startActivity(i);
            }
            else{
                Toast.makeText(context,"La longitud del telefon ha de ser de 9 digits!!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context,"No hi ha telèfon al que trucar!!", Toast.LENGTH_SHORT).show();

        }

    }
}