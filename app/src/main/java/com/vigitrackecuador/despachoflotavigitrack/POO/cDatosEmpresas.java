package com.vigitrackecuador.despachoflotavigitrack.POO;

public class cDatosEmpresas
{
    private String name_db;
    private String emrpesa_name;
    private String ip_server;
    private String user;
    private String pass;

    public cDatosEmpresas()
    {
    }

    public String getName_db() {
        return name_db;
    }

    public void setName_db(String name_db) {
        this.name_db = name_db;
    }

    public String getEmrpesa_name() {
        return emrpesa_name;
    }

    public void setEmrpesa_name(String emrpesa_name) {
        this.emrpesa_name = emrpesa_name;
    }

    public String getIp_server() {
        return ip_server;
    }

    public void setIp_server(String ip_server) {
        this.ip_server = ip_server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString()
    {
        return emrpesa_name;
    }
}
