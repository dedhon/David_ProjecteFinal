package com.example.david_projectefinal;

public class DadaMaps {

    String pob;
    String numS;
    String nomT;
    String color;

    public DadaMaps(String po, String num, String nom, String col)
    {
        this.pob=po;
        this.numS=num;
        this.nomT=nom;
        this.color=col;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getNumS() {
        return numS;
    }

    public void setNumS(String numS) {
        this.numS = numS;
    }

    public String getNomT() {
        return nomT;
    }

    public void setNomT(String nomT) {
        this.nomT = nomT;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
