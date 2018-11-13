package com.developer.dschlimovich.retrofit;


public class TempPh {
    private String Temp1;
    private String Temp2;
    private String Temp3;
    private String Temp4;
    private String Temp5;
    private String TempPh;
    private String Ph;
    private String Humidity;
    private String TempAmb;

    public TempPh(String temp1, String temp2, String temp3, String temp4, String temp5, String tempPh, String ph, String humidity, String tempAmb) {
        Temp1 = temp1;
        Temp2 = temp2;
        Temp3 = temp3;
        Temp4 = temp4;
        Temp5 = temp5;
        TempPh = tempPh;
        Ph = ph;
        Humidity = humidity;
        TempAmb = tempAmb;
    }

    public String getTemp1() {
        return Temp1;
    }

    public String getTemp2() {
        return Temp2;
    }

    public String getTemp3() {
        return Temp3;
    }

    public String getTemp4() {
        return Temp4;
    }

    public String getTemp5() {
        return Temp5;
    }

    public String getTempPh() {
        return TempPh;
    }

    public String getPh() {
        return Ph;
    }

    public String getHumidity() {
        return Humidity;
    }

    public String getTempAmb() {
        return TempAmb;
    }
}
