package com.example.david_projectefinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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
    public static final String colorT = "ColorTipus";


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
    public void finalize() {
        dbW.close();
        dbR.close();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////Consultes MAQUINES//////////////////////////////////////////
    //Secció consultes d'ordenació
    public Cursor ordenarNom() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY maq.Nom";

        return dbR.rawQuery(MY_QUERY, null);

    }
    /*public Cursor ordenarNom() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                nomM);
    }*/

    public Cursor ordenarMix() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY zo.NomZona,maq.Poblacio,maq.Adreça";

        return dbR.rawQuery(MY_QUERY, null);

    }
    /*public Cursor ordenarMix() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                zonaForeign + "," + poblacioM + "," + adreçaM);
    }*/

    public Cursor ordenarZona() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY zo.NomZona";

        return dbR.rawQuery(MY_QUERY, null);

    }
    /*public Cursor ordenarZona() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                zonaForeign);
    }*/

    public Cursor ordenarPoblacio() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY maq.Poblacio";

        return dbR.rawQuery(MY_QUERY, null);

    }
    /*public Cursor ordenarPoblacio() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                poblacioM);
    }*/

    public Cursor ordenarAdreça() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY maq.Adreça";

        return dbR.rawQuery(MY_QUERY, null);

    }
    /*
    public Cursor ordenarAdreça() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                adreçaM);
    }*/

    public Cursor ordenarData() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY maq.DataRevisio";

        return dbR.rawQuery(MY_QUERY, null);

    }
   /* public Cursor ordenarData() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                dataM);
    }*/

    /////////Seccio filtratge màquines
    public Cursor filtrarNumSerie(String nums) {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id WHERE NumMaquina LIKE '%" + nums + "%'" + " ORDER BY maq.Nom";

        return dbR.rawQuery(MY_QUERY, null);

    }

    /*public Cursor filtrarNumSerie2(String nums) {
        final String MY_QUERY = "SELECT * FROM maquines WHERE NumMaquina LIKE '%" + nums + "%'";

        return dbR.rawQuery(MY_QUERY, null);
    }*/

    //Mirem si la maquina te un número de serie repetit
    public boolean mirarNumSerieRepe(String numS) {
        boolean contingut = false;
        Cursor cuirAux = dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                numM + "=?", new String[]{String.valueOf(numS)},
                null, null, null);
        if (!cuirAux.moveToFirst()) {
            contingut = true;
        } else {
            contingut = false;
        }
        return contingut;
    }

    //Agafem tota la info d'una maquina amb un id
    public Cursor agafarMaquinaUna(long id) {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                iD + "=?", new String[]{String.valueOf(id)},
                null, null, nomM);

    }

    //Agafem totes les maquines
    public Cursor mostrarAllMaquines() {
        final String MY_QUERY = "SELECT maq._id,Nom,Adreça,CodiPostal,Poblacio,Telefon,Email,NumMaquina,DataRevisio,ti.NomTipus,zo.NomZona FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id ORDER BY maq.Nom";

        return dbR.rawQuery(MY_QUERY, null);

    }
   /* public Cursor mostrarAllMaquines() {
        return dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                null,
                null,
                null,
                null,
                nomM);
    }*/



    //Eliminem maquina amb un id
    public void eliminarMaquina(long id) {

        dbW.delete(MaquinaBuidem, iD + " = ?", new String[]{String.valueOf(id)});
    }

    //Actualitzem les dades d'una maquina amb el id
    public boolean updateMaquina(long id, String nom, String adre, int cod, String pob, String tlf, String emil, String num, String data, int tips, int zons) {
        boolean estat = false;
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
        try {
            dbW.update(MaquinaBuidem, values, iD + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLiteConstraintException e) {
            estat = true;
        }
        return estat;
    }

    public long addMaquina(String nom, String adre, int cod, String pob, String tlf, String emil, String num, String data, int tips, int zons) {
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////Tipus
    //Mirem si la maquina te un número de serie repetit
    public boolean mirarNomTipusRepe(String nom) {
        boolean contingut = false;
        Cursor cuirAux = dbR.query(tipusBuidem, new String[]{iD, nomT, colorT},
                nomT + "=?", new String[]{String.valueOf(nom)},
                null, null, null);
        if (!cuirAux.moveToFirst()) {
            contingut = true;
        } else {
            contingut = false;
        }
        return contingut;
    }

    public boolean mirarSiTipusAssignat(long idAbuscar) {
        boolean contingut = false;
        Cursor curmirar = dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                tipusForeign + "=?", new String[]{String.valueOf(idAbuscar)},
                null, null, null);

        if (!curmirar.moveToFirst()) {
            contingut = false;
        } else {
            contingut = true;
        }
        return contingut;
    }

    public Cursor agafarTipusUn(long id) {
        return dbR.query(tipusBuidem, new String[]{iD, nomT,colorT},
                iD + "=?", new String[]{String.valueOf(id)},
                null, null, nomT);

    }

    public long addTipus(String nom, String color) {
        ContentValues values = new ContentValues();
        values.put(nomT, nom);
        values.put(colorT, color);

        return dbW.insert(tipusBuidem, null, values);
    }

    public Cursor mostrarAllTipus() {
        return dbR.query(tipusBuidem, new String[]{iD, nomT, colorT},
                null,
                null,
                null,
                null,
                nomT);
    }

    public void eliminarTipus(long id) {
        dbW.delete(tipusBuidem, iD + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateTipus(long id, String nom, String color) {
        boolean estat = false;
        ContentValues values = new ContentValues();
        values.put(nomT, nom);
        values.put(colorT, color);
        try {
            dbW.update(tipusBuidem, values, iD + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLiteConstraintException e) {
            estat = true;
        }
        return estat;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////Zones

    public Cursor agafarPoblacionsZones(long idAbusc) {
        final String MY_QUERY = "SELECT Poblacio,NumMaquina,ti.NomTipus,ti.ColorTipus FROM maquines AS maq INNER JOIN zones AS zo ON maq.Zona = zo._id INNER JOIN tipus AS ti ON maq.Tipus = ti._id WHERE maq.Zona =" + idAbusc + " ORDER BY maq.Nom";

        return dbR.rawQuery(MY_QUERY, null);

    }


    public boolean mirarNomZonaRepe(String nom) {
        boolean contingut = false;
        Cursor cuirAux = dbR.query(zonesBuidem, new String[]{iD, nomZ},
                nomZ + "=?", new String[]{String.valueOf(nom)},
                null, null, null);
        if (!cuirAux.moveToFirst()) {
            contingut = true;
        } else {
            contingut = false;
        }
        return contingut;
    }

    public boolean mirarSiZonaAssignat(long idAbuscar) {
        boolean contingut = false;
        Cursor curmirar = dbR.query(MaquinaBuidem, new String[]{iD, nomM, adreçaM, codiPostalM, poblacioM, tlfM, emailM, numM, dataM, tipusForeign, zonaForeign},
                zonaForeign + "=?", new String[]{String.valueOf(idAbuscar)},
                null, null, null);

        if (!curmirar.moveToFirst()) {
            contingut = false;
        } else {
            contingut = true;
        }
        return contingut;
    }

    public Cursor agafarZonesUn(long id) {
        return dbR.query(zonesBuidem, new String[]{iD, nomZ},
                iD + "=?", new String[]{String.valueOf(id)},
                null, null, nomZ);

    }

    public long addZonas(String nom) {
        ContentValues values = new ContentValues();
        values.put(nomZ, nom);

        return dbW.insert(zonesBuidem, null, values);
    }

    public Cursor mostrarAllZones() {
        return dbR.query(zonesBuidem, new String[]{iD, nomZ},
                null,
                null,
                null,
                null,
                nomZ);
    }

    public void eliminarZones(long id) {
        dbW.delete(zonesBuidem, iD + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateZones(long id, String nom) {
        boolean cont = false;
        ContentValues values = new ContentValues();
        values.put(nomZ, nom);
        try {
            dbW.update(zonesBuidem, values, iD + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLiteConstraintException e) {
            cont = true;
        }
        return cont;
    }
}

