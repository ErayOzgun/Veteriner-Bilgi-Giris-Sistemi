package com.vuralsoftware.vbgs.models;

public class asimodel {

    String asiad,durum,tarih,saat;

    public asimodel(){}

    public asimodel(String asiad, String durum, String tarih, String saat) {
        this.asiad = asiad;
        this.durum = durum;
        this.tarih = tarih;
        this.saat = saat;
    }

    public String getAsiad() {
        return asiad;
    }

    public String getDurum() {
        return durum;
    }

    public String getTarih() {
        return tarih;
    }

    public String getSaat() {
        return saat;
    }
}
