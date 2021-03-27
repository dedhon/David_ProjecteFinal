package com.example.david_projectefinal;

public class ZonaMaquina {

    int elIdZona;
    String nomZona;

    public ZonaMaquina(int iD, String nom)
    {
        this.elIdZona=iD;
        this.nomZona=nom;
    }

    public int getElIdZona() {
        return elIdZona;
    }

    public void getElIdZona(int elId) {
        this.elIdZona = elId;
    }

    public String getNomZona() {
        return nomZona;
    }

    public void setNomZona(String nomz) {
        this.nomZona = nomz;
    }
}

