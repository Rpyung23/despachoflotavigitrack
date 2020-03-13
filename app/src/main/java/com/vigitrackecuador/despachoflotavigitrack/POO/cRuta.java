package com.vigitrackecuador.despachoflotavigitrack.POO;

public class cRuta
{
    private int idRuta;
    private String DescRuta;
    private String LetrRuta;
    private String CodiClieRuta;

    public cRuta()
    {
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    @Override
    public String toString()
    {
        return LetrRuta;
    }

    public String getDescRuta() {
        return DescRuta;
    }

    public void setDescRuta(String descRuta) {
        DescRuta = descRuta;
    }

    public String getLetrRuta() {
        return LetrRuta;
    }

    public void setLetrRuta(String letrRuta) {
        LetrRuta = letrRuta;
    }

    public String getCodiClieRuta() {
        return CodiClieRuta;
    }

    public void setCodiClieRuta(String codiClieRuta) {
        CodiClieRuta = codiClieRuta;
    }
}
