package com.example.david_projectefinal;

public class Temps {

    String poblacio;
    String temp;
    String tempMax;
    String tempMin;
    String icon;
    String color;

    public Temps(String pob,String tm, String tmMa, String tmMi,String ic,String col)
    {
        this.poblacio=pob;
        this.temp=tm;
        this.tempMax=tmMa;
        this.tempMin=tmMi;
        this.icon=ic;
        this.color=col;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
