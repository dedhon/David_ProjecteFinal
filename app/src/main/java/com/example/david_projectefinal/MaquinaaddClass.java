package com.example.david_projectefinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.ui.maquina.MaquinaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MaquinaaddClass extends AppCompatActivity {
    public BuidemDataSource bd;
     EditText etData;
    EditText etNom;
    EditText etDir ;
    EditText etCodiPos;
    EditText etPobl;
    EditText etTlf;
    EditText etEmail;
    EditText etNumSer;
    EditText etTipus;
    EditText etZona;
    ImageView aceptar,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmaquina);
        aceptar = findViewById(R.id.btnAceptar);
        cancel = findViewById(R.id.btnCancelar);
        Glide.with(MaquinaaddClass.this).load(R.drawable.iccancel).into(cancel);
        Glide.with(MaquinaaddClass.this).load(R.drawable.iccancel).into(cancel);
        etNom = findViewById(R.id.etNom);
        etDir = findViewById(R.id.etDir);
        etCodiPos = findViewById(R.id.etCodiPostal);
        etPobl = findViewById(R.id.etPoblacio);
        etTlf = findViewById(R.id.etTelefon);
        etEmail = findViewById(R.id.etEmail);
        etNumSer = findViewById(R.id.etNumSer);
        etData = findViewById(R.id.etData);
        etTipus = findViewById(R.id.etTipus);
        etZona = findViewById(R.id.etZona);
        dialogAddMaquina();
    }
    public void dialogAddMaquina() {

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
                String tips = etTipus.getText().toString();
                if (tips.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"El tipus és obligatori!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String zons = etZona.getText().toString();
                if (zons.trim().equals("")) {
                    Toast.makeText(MaquinaaddClass.this,"La zona és obligatoria!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bd.addMaquina(nom, adreça, codiConvert, pob, tlf, email, numser, data, tips, zons);

                Intent mIntent = new Intent();
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
    }
}
