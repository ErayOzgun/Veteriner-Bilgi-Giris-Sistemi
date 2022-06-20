package com.vuralsoftware.vbgs.models;

public class note {
    String baslik,tarih;

    public note(){}
    public note(String baslik,String tarih){
        this.baslik=baslik;
        this.tarih=tarih;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
