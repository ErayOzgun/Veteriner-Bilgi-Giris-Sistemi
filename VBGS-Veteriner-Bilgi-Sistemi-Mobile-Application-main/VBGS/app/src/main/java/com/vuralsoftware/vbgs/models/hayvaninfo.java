package com.vuralsoftware.vbgs.models;

public class hayvaninfo {

    String hayvanad,kupeno;

    public hayvaninfo(){

    }

    public hayvaninfo(String hayvanad,String kupeno) {

        this.hayvanad = hayvanad;
        this.kupeno = kupeno;

    }

    public String getHayvanad() {

        return hayvanad;

    }

    public void setHayvanad(String hayvanad) {

        this.hayvanad = hayvanad;

    }

    public String getKupeno() {

        return kupeno;

    }

    public void setKupeno(String kupeno) {

        this.kupeno = kupeno;

    }
}
