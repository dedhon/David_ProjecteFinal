package com.example.david_projectefinal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        // Eliminem el producte amb clau primària "id"
        dbW.delete(MaquinaBuidem,iD + " = ?", new String[] { String.valueOf(id) });
    }
    //Actualitzem les dades d'una maquina amb el id
    public void updateMaquina(long id,String nom, String adre, int cod, String pob,String tlf, String emil, String num, String data, String tips, String zons ){
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
    public long addMaquina(String nom, String adre, int cod, String pob,String tlf, String emil, String num, String data, String tips, String zons ) {
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

}

