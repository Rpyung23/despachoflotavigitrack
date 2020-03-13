package com.vigitrackecuador.despachoflotavigitrack.POO;

public class cFrecuencia
{
    private String DescFrec;
    private String LetrFrec;
    private String idRutaFrec;

    @Override
    public String toString() {
        return DescFrec;
    }

    public cFrecuencia() {
    }

    public String getDescFrec() {
        return DescFrec;
    }

    public void setDescFrec(String descFrec) {
        DescFrec = descFrec;
    }

    public String getLetrFrec() {
        return LetrFrec;
    }

    public void setLetrFrec(String letrFrec) {
        LetrFrec = letrFrec;
    }

    public String getIdRutaFrec() {
        return idRutaFrec;
    }

    public void setIdRutaFrec(String idRutaFrec) {
        this.idRutaFrec = idRutaFrec;
    }
}
