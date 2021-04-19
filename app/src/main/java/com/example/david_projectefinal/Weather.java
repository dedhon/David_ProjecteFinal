package com.example.david_projectefinal;

public class Weather {

    private int id;
    private String main;
    private String descr;
    private String icon;

    public Weather(int iD, String mai, String desc, String ic)
    {
        this.id=iD;
        this.main=mai;
        this.descr=desc;
        this.icon=ic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
