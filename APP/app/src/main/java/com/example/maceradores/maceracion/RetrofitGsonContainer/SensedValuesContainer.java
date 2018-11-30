package com.example.maceradores.maceracion.RetrofitGsonContainer;

public class SensedValuesContainer {

       //{"id":"5","id_exp":"69","fechayhora":"2018-11-29 10:13:02","temp1":"-1000","temp2":"-1000","temp3":"-1000","temp4":"-1000","temp5":"-1000","tempPh":"-1000","tempAmb":"-10000","humity":"-1","pH":"-2"}

        private String id;
        private String id_exp;
        private String fechayhora;
        private String temp1;
        private String temp2;
        private String temp3;
        private String temp4;
        private String temp5;
        private String tempPh;
        private String tempAmb;
        private String humity;
        private String pH;

    public SensedValuesContainer(String id, String id_exp, String fechayhora, String temp1, String temp2, String temp3, String temp4, String temp5, String tempPh, String tempAmb, String humity, String pH) {
        this.id = id;
        this.id_exp = id_exp;
        this.fechayhora = fechayhora;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
        this.tempPh = tempPh;
        this.tempAmb = tempAmb;
        this.humity = humity;
        this.pH = pH;
    }

    public String getId() {
        return id;
    }

    public String getId_exp() {
        return id_exp;
    }

    public String getFechayhora() {
        return fechayhora;
    }

    public String getTemp1() {
        return temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public String getTemp3() {
        return temp3;
    }

    public String getTemp4() {
        return temp4;
    }

    public String getTemp5() {
        return temp5;
    }

    public String getTempPh() {
        return tempPh;
    }

    public String getTempAmb() {
        return tempAmb;
    }

    public String getHumity() {
        return humity;
    }

    public String getpH() {
        return pH;
    }
}


