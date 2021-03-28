package com.example.david_projectefinal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BuidemDataSource {

    //Camp id per unic per les 3 taules
    public static final String iD = "_id";

    //Camps de la taula maquines
    public static final String MaquinaBuidem = "maquines";
    public static final String nomM = "Nom";
    public static final String adreçaM = "Adreça";
    public static final String codiPostalM = "CodiPostal";
    public static final String poblacioM = "Poblacio";
    public static final String tlfM = "Telefon";
    public static final String emailM = "Email";
    public static final String numM = "NumMaquina";
    public static final String dataM = "DataRevisio";
    public static final String tipusForeign = "Tipus";
    public static final String zonaForeign = "Zona";

    //Camps de la taula zones
    public static final String zonesBuidem = "zones";
    public static final String nomZ = "NomZona";

    //Camps de la taula tipus

    public static final String tipusBuidem = "tipus";
    public static final String nomT = "NomTipus";



    private BuidemSLOpenHelper dbBuidemSLOpenHelper;
    private SQLiteDatabase dbW, dbR;

    public BuidemDataSource(Context context) {

        dbBuidemSLOpenHelper = new BuidemSLOpenHelper(context);
        open();
    }
    //Obrim l'acces a la base de dades
    public void open() {
        dbW = dbBuidemSLOpenHelper.getWritableDatabase();
        dbR = dbBuidemSLOpenHelper.getReadableDatabase();
    }
    //Tanquem l'acces a la base de dades
    public void finalize () {
        dbW.close();
        dbR.close();
    }
/////////////Consultes MAQUINES//////////////////////////////////////////
    //Secció consultes d'ordenació
    public Cursor ordenarNom() {
    return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
            null,
            null,
            null,
            null,
            nomM);
    }
    public Cursor ordenarZona() {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                null,
                null,
                null,
                null,
                zonaForeign);
    }
    public Cursor ordenarPoblacio() {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                null,
                null,
                null,
                null,
                poblacioM);
    }
    public Cursor ordenarAdreça() {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                null,
                null,
                null,
                null,
                adreçaM);
    }
    public Cursor ordenarData() {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                null,
                null,
                null,
                null,
                dataM);
    }
    /////////Seccio filtratge màquines
    public Cursor filtrarNumSerie(String nums) {
        final String MY_QUERY = "SELECT * FROM maquines WHERE NumMaquina LIKE '%" + nums + "%'";

        return dbR.rawQuery(MY_QUERY, null);

    }



    //Agafem tota la info d'una maquina amb un id
    public Cursor agafarMaquinaUna(long id) {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                iD+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }
    //Agafem totes les maquines
    public Cursor mostrarAllMaquines() {
        return dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                null,
                null,
                null,
                null,
                iD);
    }
    //Eliminem maquina amb un id
    public void eliminarMaquina(long id) {

        dbW.delete(MaquinaBuidem,iD + " = ?", new String[] { String.valueOf(id) });
    }
    //Actualitzem les dades d'una maquina amb el id
    public void updateMaquina(long id,String nom, String adre, int cod, String pob,String tlf, String emil, String num, String data, int tips, int zons ){
        ContentValues values = new ContentValues();
        values.put(nomM, nom);
        values.put(adreçaM, adre);
        values.put(codiPostalM, cod);
        values.put(poblacioM, pob);
        values.put(tlfM, tlf);
        values.put(emailM, emil);
        values.put(numM, num);
        values.put(dataM, data);
        values.put(tipusForeign, tips);
        values.put(zonaForeign, zons);
        dbW.update(MaquinaBuidem, values, iD + " = ?", new String[] {String.valueOf(id)});
    }
    public long addMaquina(String nom, String adre, int cod, String pob,String tlf, String emil, String num, String data, int tips, int zons ) {
        ContentValues values = new ContentValues();
        values.put(nomM, nom);
        values.put(adreçaM, adre);
        values.put(codiPostalM, cod);
        values.put(poblacioM, pob);
        values.put(tlfM, tlf);
        values.put(emailM, emil);
        values.put(numM, num);
        values.put(dataM, data);
        values.put(tipusForeign, tips);
        values.put(zonaForeign, zons);
        return dbW.insert(MaquinaBuidem, null, values);
    }
    //////////////////////////////////////////////////////Tipus
    public boolean mirarSiTipusAssignat(long idAbuscar)
    {
        boolean contingut=false;
        Cursor curmirar = dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                tipusForeign+ "=?", new String[]{String.valueOf(idAbuscar)},
                null, null, null);

        if(!curmirar.moveToFirst())
        {
            contingut = false;
        }
        else{
            contingut=true;
        }
        return contingut;
    }
    public Cursor agafarTipusUn(long id) {
        return dbR.query(tipusBuidem, new String[]{iD,nomT},
                iD+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }
    public long addTipus(String nom) {
        ContentValues values = new ContentValues();
        values.put(nomT, nom);

        return dbW.insert(tipusBuidem, null, values);
    }
    public Cursor mostrarAllTipus() {
        return dbR.query(tipusBuidem, new String[]{iD,nomT},
                null,
                null,
                null,
                null,
                iD);
    }

    public void eliminarTipus(long id) {
        dbW.delete(tipusBuidem,iD + " = ?", new String[] { String.valueOf(id) });
    }
    public void updateTipus(long id,String nom ) {
        ContentValues values = new ContentValues();
        values.put(nomT, nom);

        dbW.update(tipusBuidem, values, iD + " = ?", new String[]{String.valueOf(id)});
    }
    //////////////////////////////////////////////////////Zones
    public boolean mirarSiZonaAssignat(long idAbuscar)
    {
        boolean contingut=false;
        Cursor curmirar = dbR.query(MaquinaBuidem, new String[]{iD,nomM,adreçaM,codiPostalM,poblacioM,tlfM,emailM,numM,dataM,tipusForeign,zonaForeign},
                zonaForeign+ "=?", new String[]{String.valueOf(idAbuscar)},
                null, null, null);

        if(!curmirar.moveToFirst())
        {
            contingut = false;
        }
        else{
            contingut=true;
        }
        return contingut;
    }
    public Cursor agafarZonesUn(long id) {
        return dbR.query(zonesBuidem, new String[]{iD,nomZ},
                iD+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }
    public long addZonas(String nom) {
        ContentValues values = new ContentValues();
        values.put(nomZ, nom);

        return dbW.insert(zonesBuidem, null, values);
    }
    public Cursor mostrarAllZones() {
        return dbR.query(zonesBuidem, new String[]{iD,nomZ},
                null,
                null,
                null,
                null,
                iD);
    }

    public void eliminarZones(long id) {
        dbW.delete(zonesBuidem,iD + " = ?", new String[] { String.valueOf(id) });
    }
    public void updateZones(long id,String nom ) {
        ContentValues values = new ContentValues();
        values.put(nomZ, nom);

        dbW.update(zonesBuidem, values, iD + " = ?", new String[]{String.valueOf(id)});
    }
}

