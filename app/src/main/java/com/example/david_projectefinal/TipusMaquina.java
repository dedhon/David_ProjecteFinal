package com.example.david_projectefinal;

public class TipusMaquina {

    int elId;
    String nomTips;

    public TipusMaquina(int iD, String nom)
    {
        this.elId=iD;
        this.nomTips=nom;
    }

    public int getElId() {
        return elId;
    }

    public void setElId(int elId) {
        this.elId = elId;
    }

    public String getNomTips() {
        return nomTips;
    }

    public void setNomTips(String nomTips) {
        this.nomTips = nomTips;
    }
}
