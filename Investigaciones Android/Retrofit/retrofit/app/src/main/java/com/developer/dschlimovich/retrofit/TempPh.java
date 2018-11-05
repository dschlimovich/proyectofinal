package com.developer.dschlimovich.retrofit;

public class TempPh {
    private String Temp; //Vienen como Strings, ya que voy a mostarlos (String) declararlos es al pedo.
    private String pH;

    public TempPh(String temp, String pH) {
        Temp = temp;
        this.pH = pH;
    }

    public String getTemp() {
        return Temp;
    }

    public String getpH() {
        return pH;
    }
}
