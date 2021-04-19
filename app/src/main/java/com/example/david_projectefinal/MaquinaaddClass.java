package com.example.david_projectefinal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.ui.tipus.TipusFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MaquinaaddClass extends AppCompatActivity {
    final String colorVermell = "#FF0000";
    String dataFINAL = "";
    int contSemafor;
    public BuidemDataSource bd;
    TextView etData;
    EditText etNom;
    EditText etDir;
    EditText etCodiPos;
    EditText etPobl;
    EditText etTlf;
    EditText etEmail;
    EditText etNumSer;
    Spinner tipus, zones;
    long[] filtre;
    ImageView aceptar, cancel, calendar;
    boolean auxSemaforTipus = false;
    boolean auxSemaforZona = false;
    ///////////////////////////////Secció spinner per Tipus
    ArrayList<TipusMaquina> llistaTipus;
    ArrayList<String> auxListTipus;
    ArrayList<String> auxListTipusFinal;
    ArrayAdapter<String> arrayAdapterTipus;
    ///////////////////////////////Secció spinner per Zona
    ArrayList<ZonaMaquina> llistaZona;
    ArrayList<String> auxListZonas;
    ArrayList<String> auxListZonasFinal;
    ArrayAdapter<String> arrayAdapterZonas;
    int idTipusFinal, idZonaFinal;
    int mDefaultColor;
    String colorBDTipus = "";
    TextView tvColor;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmaquina);
        tipus = findViewById(R.id.TipusSpinner);
        zones = findViewById(R.id.ZonaSpinner);
        bd = new BuidemDataSource(this);
        aceptar = findViewById(R.id.btnAceptar);
        cancel = findViewById(R.id.btnCancelar);
        calendar = findViewById(R.id.imageViewData);
        Glide.with(MaquinaaddClass.this).load(R.drawable.validaric).into(aceptar);
        Glide.with(MaquinaaddClass.this).load(R.drawable.iccancel).into(cancel);
        Glide.with(MaquinaaddClass.this).load(R.drawable.cal).into(calendar);
        etNom = findViewById(R.id.etNom);
        etDir = findViewById(R.id.etDir);
        etCodiPos = findViewById(R.id.etCodiPostal);
        etPobl = findViewById(R.id.etPoblacio);
        etTlf = findViewById(R.id.etTelefon);
        etEmail = findViewById(R.id.etEmail);
        etNumSer = findViewById(R.id.etNumSer);
        etData = findViewById(R.id.etData);
        contSemafor = 0;
        filtre = new long[2];
        filtre = this.getIntent().getExtras().getLongArray("FILTRE");

        gestionarSpinners();
        if (filtre[0] == 1) {
            crearAddMaquina();
        } else {
            if (filtre[0] == 2) {
                editarAddMaquina(filtre[1]);
            }
        }
    }

    public void gestionarSpinners() {
        //Gestionem els dos cursors per omplir els spinners
        Cursor auxCursor = bd.agafarMaquinaUna(filtre[1]);
        auxCursor.moveToFirst();
        Cursor curTipus = bd.mostrarAllTipus();
        Cursor curZona = bd.mostrarAllZones();
        //Instanciem les llistes
        llistaTipus = new ArrayList<>();
        llistaZona = new ArrayList<>();
        //Creem els objectes de les clases
        TipusMaquina lstTipus = null;
        ZonaMaquina lstZona = null;
        //Fem els 2 whiles, 1 per cada cursor per saber agafar les dades per els spinners
        while (curTipus.moveToNext()) {
            int id = curTipus.getInt(curTipus.getColumnIndex(BuidemDataSource.iD));
            String nom = curTipus.getString(curTipus.getColumnIndex(BuidemDataSource.nomT));
            lstTipus = new TipusMaquina(id, nom);
            llistaTipus.add(lstTipus);
        }
        while (curZona.moveToNext()) {
            int id = curZona.getInt(curZona.getColumnIndex(BuidemDataSource.iD));
            String nom = curZona.getString(curZona.getColumnIndex(BuidemDataSource.nomZ));
            lstZona = new ZonaMaquina(id, nom);
            llistaZona.add(lstZona);
        }
        //Tanquem els cursors
        curTipus.close();
        curZona.close();
        //Instanciem els arraysList de strings
        auxListTipus = new ArrayList<String>();
        auxListZonas = new ArrayList<String>();
        auxListTipusFinal = new ArrayList<String>();
        auxListZonasFinal = new ArrayList<String>();
        if (filtre[0] == 1) {
            //Fiquem per defecte les dos opcions per triar al spinner
            auxListTipus.add(String.valueOf("--" + " Selecciona un tipus"));
            auxListZonas.add(String.valueOf("--" + " Selecciona una zona"));
            for (int i = 0; i < llistaTipus.size(); i++) {
                auxListTipus.add(String.valueOf(llistaTipus.get(i).getElId()) + "- " + llistaTipus.get(i).getNomTips());
                contSemafor++;
            }
            for (int j = 0; j < llistaZona.size(); j++) {
                auxListZonas.add(String.valueOf(llistaZona.get(j).getElIdZona()) + "- " + llistaZona.get(j).getNomZona());
            }
            //Instanciem el adaptador que contindra les dades de la BD segons la crida
            arrayAdapterTipus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListTipus);
            arrayAdapterZonas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListZonas);
        } else {
            if (filtre[0] == 2) {
                //Fiquem el contingut del array list d'0bjectes en format String a dintre del arrayList de strings
                for (int i = 0; i < llistaTipus.size(); i++) {
                    if (llistaTipus.get(i).getElId() == auxCursor.getInt(auxCursor.getColumnIndex(BuidemDataSource.tipusForeign))) {
                        auxListTipusFinal.add(String.valueOf(llistaTipus.get(i).getElId()) + "- " + llistaTipus.get(i).getNomTips());
                    } else {
                        auxListTipus.add(String.valueOf(llistaTipus.get(i).getElId()) + "- " + llistaTipus.get(i).getNomTips());
                    }
                }
                for (int j = 0; j < llistaZona.size(); j++) {
                    if (llistaZona.get(j).getElIdZona() == auxCursor.getInt(auxCursor.getColumnIndex(BuidemDataSource.zonaForeign))) {
                        auxListZonasFinal.add(String.valueOf(llistaZona.get(j).getElIdZona()) + "- " + llistaZona.get(j).getNomZona());
                    } else {
                        auxListZonas.add(String.valueOf(llistaZona.get(j).getElIdZona()) + "- " + llistaZona.get(j).getNomZona());
                    }
                }
                for (int p = 0; p < auxListTipus.size(); p++) {
                    auxListTipusFinal.add(auxListTipus.get(p));
                }
                for (int l = 0; l < auxListZonas.size(); l++) {
                    auxListZonasFinal.add(auxListZonas.get(l));
                }
                //Instanciem el adaptador que contindra les dades de la BD segons la crida
                arrayAdapterTipus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListTipusFinal);
                arrayAdapterZonas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListZonasFinal);
            }
        }
        //Inflem els spinners amb els adaptadors
        tipus.setAdapter(arrayAdapterTipus);
        zones.setAdapter(arrayAdapterZonas);

        //Definim els set on click dels spinners
        tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcioTriada = tipus.getSelectedItem().toString();
                String auxOption = "";
                int cont = 0;
                while (opcioTriada.charAt(cont) != '-') {
                    auxOption = auxOption + "" + opcioTriada.charAt(cont);
                    cont++;
                }
                if (auxOption != "") {
                    idTipusFinal = Integer.parseInt(auxOption);
                    auxSemaforTipus = false;
                } else {
                    auxSemaforTipus = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        zones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcioTriada = zones.getSelectedItem().toString();
                String auxOption = "";
                int cont = 0;
                while (opcioTriada.charAt(cont) != '-') {
                    auxOption = auxOption + "" + opcioTriada.charAt(cont);
                    cont++;
                }
                if (auxOption != "") {
                    idZonaFinal = Integer.parseInt(auxOption);
                    auxSemaforZona = false;
                } else {
                    auxSemaforZona = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ImageView auxT = (ImageView) findViewById(R.id.iVaddTipsAux);
        Glide.with(MaquinaaddClass.this).load(R.drawable.addtipaux).into(auxT);

        auxT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTipus();
            }
        });
        ImageView auxZ = (ImageView) findViewById(R.id.iVaddZonaAux);
        Glide.with(MaquinaaddClass.this).load(R.drawable.addtipaux).into(auxZ);
        auxZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearZona();
            }
        });
    }

    public void crearZona() {
        AlertDialog.Builder Zona = new AlertDialog.Builder(MaquinaaddClass.this);
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_zona, null);


        final EditText etNomZona = v2.findViewById(R.id.etZonaNom);
        Zona.setView(v2).setPositiveButton("Afegir Zona", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomZona = etNomZona.getText().toString();
                if (nomZona.trim().equals("")) {
                    missatgeSnackBar(v2, "El nom es obligarori!!", colorVermell);
                    return;
                } else {
                    boolean compNomZ = bd.mirarNomZonaRepe(nomZona);
                    if (compNomZ == false) {
                        missatgeSnackBar(v2, "El nom de la zona ja existeix!!", colorVermell);
                        return;
                    }
                }
                bd.addZonas(nomZona);
                gestionarSpinners();
            }
        });
        Zona.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Zona.create();
        dialog.show();

    }

    public void missatgeSnackBar(View v, String missatge, String color) {
        snackbar = Snackbar.make(v, missatge, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();
    }

    public void editarAddMaquina(long id) {

        ImageView calendar = (ImageView) findViewById(R.id.imageViewData);
        Glide.with(MaquinaaddClass.this).load(R.drawable.cal).into(calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data();
            }
        });

        Cursor updateMaquina = bd.agafarMaquinaUna(id);
        updateMaquina.moveToFirst();

        TextView titol = (TextView) findViewById(R.id.idBuscar);
        titol.setText("Actualitzar màquina");

        etNom.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.nomM)));
        etDir.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.adreçaM)));
        etCodiPos.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.codiPostalM)));
        etPobl.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.poblacioM)));
        etTlf.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM)));
        etEmail.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM)));
        etNumSer.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM)));
        etData.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.dataM)));


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codiConvert = 0;
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    missatgeSnackBar(v, "El nom es obligarori!!", colorVermell);
                    return;
                }
                String adreça = etDir.getText().toString();
                if (adreça.trim().equals("")) {
                    missatgeSnackBar(v, "L'adreça és obligatoria!!", colorVermell);
                    return;
                }
                String codiPos = etCodiPos.getText().toString();

                if (codiPos.trim().equals("")) {
                    missatgeSnackBar(v, "El codi postal és obligatori!!", colorVermell);
                    return;
                } else {
                    codiConvert = Integer.parseInt(codiPos);
                }
                String pob = etPobl.getText().toString();
                if (pob.trim().equals("")) {
                    missatgeSnackBar(v, "La població és obligatoria!!", colorVermell);
                    return;
                }
                String tlf = etTlf.getText().toString();
                if (tlf.length() != 9) {
                    missatgeSnackBar(v, "La longitud del telefon no es correcte, 9 digits en total!!", colorVermell);
                    return;
                }
                String email = etEmail.getText().toString();
                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    missatgeSnackBar(v, "No has introduit un email valid, torna a probar !!", colorVermell);
                    return;
                }
                String numser = etNumSer.getText().toString();
                if (numser.trim().equals("")) {
                    missatgeSnackBar(v, "El número de serie és obligatori!!", colorVermell);
                    return;
                }
                String data = etData.getText().toString();
                String probaId = String.valueOf(idTipusFinal);
                if (probaId.equals("0")) {
                    missatgeSnackBar(v, "El tipus és obligatori!!", colorVermell);
                    return;
                }
                String probaIdZona = String.valueOf(idZonaFinal);
                if (probaIdZona.equals("0")) {
                    missatgeSnackBar(v, "La zona és obligatoria!!", colorVermell);
                    return;
                }
                boolean aux = bd.updateMaquina(id, nom, adreça, codiConvert, pob, tlf, email, numser, data, idTipusFinal, idZonaFinal);
                if (aux == true) {
                    missatgeSnackBar(v, "El número de serie ja existeix!!", colorVermell);
                }
                Intent mIntent = new Intent();
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                setResult(RESULT_CANCELED, mIntent);
                finish();
            }
        });
    }

    public void butonColor(View v2) {
        Button btnSelectColorBg = (Button) v2.findViewById(R.id.btnSelectColorBg);

        mDefaultColor = ContextCompat.getColor(MaquinaaddClass.this, R.color.design_default_color_primary);
        btnSelectColorBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(v2);
            }
        });
    }

    public void openColorPicker(View v2) {
        tvColor = (TextView) v2.findViewById(R.id.idColorView);
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(MaquinaaddClass.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
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

    public void crearTipus() {
        AlertDialog.Builder Tipus = new AlertDialog.Builder(MaquinaaddClass.this);
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.add_tipus, null);

        butonColor(v2);
        final EditText etNomTip = v2.findViewById(R.id.etZonaNom);
        Tipus.setView(v2).setPositiveButton("Afegir Tipus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomTipus = etNomTip.getText().toString();
                if (nomTipus.trim().equals("")) {
                    missatgeSnackBar(v2, "El nom es obligarori!!", colorVermell);
                    return;
                } else {
                    boolean compNom = bd.mirarNomTipusRepe(nomTipus);
                    if (compNom == false) {
                        missatgeSnackBar(v2, "El nom del tipus ja existeix!!", colorVermell);
                        return;
                    }
                }
                bd.addTipus(nomTipus, colorBDTipus);
                gestionarSpinners();
                //actualitzarTipus();
            }
        });
        Tipus.setNegativeButton("Cancelar", null);
        AlertDialog dialog = Tipus.create();
        dialog.show();

    }

    public void crearAddMaquina() {

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codiConvert = 0;
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    missatgeSnackBar(v, "El nom es obligarori!!", colorVermell);
                    return;
                }
                String adreça = etDir.getText().toString();
                if (adreça.trim().equals("")) {
                    missatgeSnackBar(v, "L'adreça és obligatoria!!", colorVermell);
                    return;
                }
                String codiPos = etCodiPos.getText().toString();

                if (codiPos.trim().equals("")) {
                    missatgeSnackBar(v, "El codi postal és obligatori!!", colorVermell);
                    return;
                } else {
                    codiConvert = Integer.parseInt(codiPos);
                }
                String pob = etPobl.getText().toString();
                if (pob.trim().equals("")) {
                    missatgeSnackBar(v, "La població és obligatoria!!", colorVermell);
                    return;
                }
                String tlf = etTlf.getText().toString();
                if (tlf.length() != 9) {
                    missatgeSnackBar(v, "La longitud del telefon no es correcte, 9 digits en total!!", colorVermell);
                    return;
                }
                String email = etEmail.getText().toString();
                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    missatgeSnackBar(v, "No has introduit un email valid, torna a probar !!", colorVermell);
                    return;
                }
                String numser = etNumSer.getText().toString();
                if (numser.trim().equals("")) {
                    missatgeSnackBar(v, "El número de serie és obligatori!!", colorVermell);
                    return;
                } else {
                    boolean compNum = bd.mirarNumSerieRepe(numser);
                    if (compNum == false) {
                        missatgeSnackBar(v, "El número de serie ja existeix!!", colorVermell);
                        return;
                    }
                }
                String data = etData.getText().toString();
                String probaId = String.valueOf(idTipusFinal);
                if (probaId.equals("0") || auxSemaforTipus == true) {
                    missatgeSnackBar(v, "El tipus és obligatori!!", colorVermell);
                    return;
                }
                String probaIdZona = String.valueOf(idZonaFinal);
                if (probaIdZona.equals("0") || auxSemaforZona == true) {
                    missatgeSnackBar(v, "La zona és obligatoria!!", colorVermell);
                    return;
                }

                bd.addMaquina(nom, adreça, codiConvert, pob, tlf, email, numser, data, idTipusFinal, idZonaFinal);

                Intent mIntent = new Intent();
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                setResult(RESULT_CANCELED, mIntent);
                finish();
            }
        });
    }

    public void data() {
        Calendar cal = Calendar.getInstance();
        int any = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        final TextView etData = findViewById(R.id.etData);
        DatePickerDialog dpd = new DatePickerDialog(MaquinaaddClass.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthaux = month + 1;
                dataFINAL = dayOfMonth + "-" + monthaux + "-" + year;

                etData.setText(dataFINAL);
            }
        }, any, mes, dia);
        dpd.show();
    }
}
