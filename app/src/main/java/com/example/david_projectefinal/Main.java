package com.example.david_projectefinal;

public class Main {

    private double temp;
    private double feel;
    private double temp_min;
    private double temp_max;
    private double pressure;
    private double humidity;

    public Main(double tem, double fel, double min, double max, double press, double humi)
    {
        this.temp=tem;
        this.feel=fel;
        this.temp_min=min;
        this.temp_max=max;
        this.pressure=press;
        this.humidity=humi;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeel() {
        return feel;
    }

    public void setFeel(double feel) {
        this.feel = feel;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
