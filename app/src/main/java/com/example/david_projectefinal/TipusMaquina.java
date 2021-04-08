package com.example.david_projectefinal;

public class TipusMaquina {

    int elId;
    String nomTips;
    String color;
    public TipusMaquina(int iD, String nom)
    {
        this.elId=iD;
        this.nomTips=nom;
    }
    public TipusMaquina(String Color, int id)
    {
        this.elId=id;
        this.color=Color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
