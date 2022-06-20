package com.vuralsoftware.vbgs.models;

public class ilacmodel {

    String ilacad,doz,tarih,saat;

    public ilacmodel(){

    }

    public ilacmodel(String ilacad,String doz,String tarih,String saat){

        this.ilacad=ilacad;
        this.doz=doz;
        this.tarih=tarih;
        this.saat=saat;
    }

    public String getIlacad() {
        return ilacad;
    }

    public void setIlacad(String ilacad) {
        this.ilacad = ilacad;
    }

    public String getDoz() {
        return doz;
    }

    public void setDoz(String doz) {
        this.doz = doz;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }


}
