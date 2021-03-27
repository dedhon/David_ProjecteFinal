package com.example.david_projectefinal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.ui.maquina.MaquinaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MaquinaaddClass extends AppCompatActivity {
    String dataFINAL="";
    public BuidemDataSource bd;
    EditText etData;
    EditText etNom;
    EditText etDir ;
    EditText etCodiPos;
    EditText etPobl;
    EditText etTlf;
    EditText etEmail;
    EditText etNumSer;
    EditText etZona;
    Spinner tipus;
    ImageView aceptar,cancel,calendar;
    ///////////////////////////////Secció spinner per Tipus
    ArrayList<TipusMaquina> llistaTipus;
    ArrayList<String> auxListTipus;
    ArrayAdapter<String> arrayAdapterTipus;
    ///////////////////////////////Secció spinner per Zona
    ArrayList<TipusMaquina> llistaZona;
    ArrayList<String> auxListZonas;
    ArrayAdapter<String> arrayAdapterZonas;
    int idTipusFinal,idZonaFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmaquina);
        tipus=findViewById(R.id.TipusSpinner);
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
        etZona = findViewById(R.id.etZona);
        long[] filtre= new long[2];
        filtre=this.getIntent().getExtras().getLongArray("FILTRE");;
        gestionarSpinnerTipus();
        if(filtre[0] == 1)
        {
            crearAddMaquina();
        }
        else{
            if(filtre[0] == 2){
                editarAddMaquina(filtre[1]);
            }
        }
    }
    public void gestionarSpinnerZones()
    {
        Cursor cur = bd.mostrarAllTipus();
        llistaTipus = new ArrayList<>();
        TipusMaquina lstTipus = null;
        while(cur.moveToNext())
        {
            int id = cur.getInt(cur.getColumnIndex(BuidemDataSource.iD));
            String nom=cur.getString(cur.getColumnIndex(BuidemDataSource.nomT));
            lstTipus = new TipusMaquina(id,nom);
            llistaTipus.add(lstTipus);
        }
        cur.close();
        auxListTipus = new ArrayList<String>();
        for(int i = 0; i < llistaTipus.size();i++)
        {
            auxListTipus.add(String.valueOf(llistaTipus.get(i).getElId()) + "- " + llistaTipus.get(i).getNomTips());
        }
        arrayAdapterTipus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListTipus);
        tipus.setAdapter(arrayAdapterTipus);
        tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcioTriada=tipus.getSelectedItem().toString();
                String auxOption="";
                int cont=0;
                while(opcioTriada.charAt(cont)!='-')
                {
                    auxOption = auxOption + "" + opcioTriada.charAt(cont);
                    cont++;
                }

                idTipusFinal = Integer.parseInt(auxOption);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void gestionarSpinnerTipus()
    {
        Cursor cur = bd.mostrarAllTipus();
        llistaTipus = new ArrayList<>();
        TipusMaquina lstTipus = null;
        while(cur.moveToNext())
        {
            int id = cur.getInt(cur.getColumnIndex(BuidemDataSource.iD));
            String nom=cur.getString(cur.getColumnIndex(BuidemDataSource.nomT));
            lstTipus = new TipusMaquina(id,nom);
            llistaTipus.add(lstTipus);
        }
        cur.close();
        auxListTipus = new ArrayList<String>();
        for(int i = 0; i < llistaTipus.size();i++)
        {
            auxListTipus.add(String.valueOf(llistaTipus.get(i).getElId()) + "- " + llistaTipus.get(i).getNomTips());
        }
        arrayAdapterTipus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auxListTipus);
        tipus.setAdapter(arrayAdapterTipus);
        tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcioTriada=tipus.getSelectedItem().toString();
                String auxOption="";
                int cont=0;
                while(opcioTriada.charAt(cont)!='-')
                {
                    auxOption = auxOption + "" + opcioTriada.charAt(cont);
                    cont++;
                }

                idTipusFinal = Integer.parseInt(auxOption);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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

        TextView titol = (TextView)findViewById(R.id.idAfegirTipus);
        titol.setText("Actualitzar màquina");

        etNom.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.nomM)));

        etDir.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.adreçaM)));

        etCodiPos.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.codiPostalM)));

        etPobl.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.poblacioM)));

        etTlf.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM)));

        etEmail.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM)));

        etNumSer.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM)));

        etData.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.dataM)));
        etData.setEnabled(false);


        etZona.setText(updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.zonaForeign)));
        etData.setEnabled(false);
        etData.setText(dataFINAL);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codiConvert=0;
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String adreça = etDir.getText().toString();
                if (adreça.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"L'adreça és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String codiPos = etCodiPos.getText().toString();

                if (codiPos.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El codi postal és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    codiConvert = Integer.parseInt(codiPos);
                }
                String pob = etPobl.getText().toString();
                if (pob.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"La població és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tlf = etTlf.getText().toString();
                String email = etEmail.getText().toString();
                String numser = etNumSer.getText().toString();
                if (numser.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El número de serie és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String data = etData.getText().toString();
                String probaId = String.valueOf(idTipusFinal);
                if (probaId.equals("0")) {
                    Toast.makeText(MaquinaaddClass.this,"El tipus és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String zons = etZona.getText().toString();
                if (zons.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"La zona és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bd.updateMaquina(id, nom, adreça, codiConvert, pob, tlf, email, numser, data, idTipusFinal, 0);
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
                int codiConvert=0;
                String nom = etNom.getText().toString();
                if (nom.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El nom és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String adreça = etDir.getText().toString();
                if (adreça.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"L'adreça és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String codiPos = etCodiPos.getText().toString();

                if (codiPos.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El codi postal és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    codiConvert = Integer.parseInt(codiPos);
                }
                String pob = etPobl.getText().toString();
                if (pob.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"La població és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tlf = etTlf.getText().toString();
                String email = etEmail.getText().toString();
                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
                {
                    Toast.makeText(MaquinaaddClass.this, "No has introduit un email valid, torna a probar ", Toast.LENGTH_LONG).show();
                    return;
                }
                String numser = etNumSer.getText().toString();
                if (numser.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El número de serie és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String data = etData.getText().toString();
                String probaId = String.valueOf(idTipusFinal);
                if (probaId.equals("0")) {
                    Toast.makeText(MaquinaaddClass.this,"El tipus és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String zons = etZona.getText().toString();
                if (zons.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"La zona és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bd.addMaquina(nom, adreça, codiConvert, pob, tlf, email, numser, data, idTipusFinal, 1);

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
    public void data()
    {
        Calendar cal = Calendar.getInstance();
        int any = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        final EditText etData = findViewById(R.id.etData);
        DatePickerDialog dpd = new DatePickerDialog(MaquinaaddClass.this, new DatePickerDialog.OnDateSetListener() {
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
}
