package com.example.maceradores.maceracion.RetrofitGsonContainer;

public class SensedValuesContainer {

       //{"id":"5","id_exp":"69","fechayhora":"2018-11-29 10:13:02","temp1":"-1000","temp2":"-1000","temp3":"-1000","temp4":"-1000","temp5":"-1000","tempPh":"-1000","tempAmb":"-10000","humity":"-1","pH":"-2"}

        private int id;
        private int id_exp;
        private String fechayhora; //Ver esto...
        private String Temp1;
        private String Temp2;
        private String Temp3;
        private String Temp4;
        private String Temp5;
        private String TempPh;
        private String Ph;
        private String Humidity;
        private String TempAmb;

        public SensedValuesContainer(int id, int id_exp, String fechayhora, String temp1, String temp2, String temp3, String temp4, String temp5, String tempPh, String ph, String humidity, String tempAmb) {
            this.id = id;
            this.id_exp = id_exp;
            this.fechayhora = fechayhora;
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

        public int getId() {
            return id;
        }

        public int getId_exp() {
            return id_exp;
        }

        public String getFechayhora() {
            return fechayhora;
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


