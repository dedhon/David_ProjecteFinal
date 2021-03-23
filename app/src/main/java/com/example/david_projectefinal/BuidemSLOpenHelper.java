package com.example.david_projectefinal;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuidemSLOpenHelper extends SQLiteOpenHelper {


    private static final int VERSIO_DATABASE = 1;

    // database name
    private static final String NOM_DATABASE = "BuidemCompany";

    public BuidemSLOpenHelper(Context contexte) {
        super(contexte, NOM_DATABASE, null, VERSIO_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase BuidemCompany) {


        String CREATE_TIPUS =
                "CREATE TABLE tipus ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NomTipus TEXT)";

        BuidemCompany.execSQL(CREATE_TIPUS);

        String CREATE_ZONES =
                "CREATE TABLE zones ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NomZona TEXT)";

        BuidemCompany.execSQL(CREATE_ZONES);
        String CREATE_MAQUINES =
                "CREATE TABLE maquines ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Nom TEXT NOT NULL," +
                        "Adreça TEXT NOT NULL," +
                        "CodiPostal INTEGER NOT NULL," +
                        "Poblacio TEXT NOT NULL," +
                        "Telefon TEXT," +
                        "Email TEXT," +
                        "NumMaquina TEXT NOT NULL," +
                        "DataRevisio TEXT NOT NULL," +
                        "Tipus INTEGER NOT NULL," +
                        "Zona INTEGER NOT NULL," +
                        "FOREIGN KEY (Tipus) REFERENCES tipus(_id), "+
                        "FOREIGN KEY (Zona) REFERENCES zones(_id))";

        BuidemCompany.execSQL(CREATE_MAQUINES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase BuidemCompany, int oldVersion, int newVersion) {

    }
}
