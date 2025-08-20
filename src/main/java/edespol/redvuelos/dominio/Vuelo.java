package edespol.redvuelos.dominio;

public class Vuelo {
    private String id;  //id del vuelo
    private String origenCodigo;  //codigo del aeropuerto de origen
    private String destinoCodigo; //codigo del aeropuerto de destino
    private String aerolineaCodigo;
    private double distanciaKm;
    private int duracionMin;
    private double costoUsd;
    private String horaSalida;

    public Vuelo() {
        this.id = null;
        this.origenCodigo = null;
        this.destinoCodigo = null;
        this.aerolineaCodigo = null;
        this.distanciaKm = 0.0;
        this.duracionMin = 0;
        this.costoUsd = 0.0;
        this.horaSalida = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigenCodigo() {
        return origenCodigo;
    }

    public void setOrigenCodigo(String origenCodigo) {
        this.origenCodigo = origenCodigo;
    }

    public String getDestinoCodigo() {
        return destinoCodigo;
    }

    public void setDestinoCodigo(String destinoCodigo) {
        this.destinoCodigo = destinoCodigo;
    }

    public String getAerolineaCodigo() {
        return aerolineaCodigo;
    }

    public void setAerolineaCodigo(String aerolineaCodigo) {
        this.aerolineaCodigo = aerolineaCodigo;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public double getCostoUsd() {
        return costoUsd;
    }

    public void setCostoUsd(double costoUsd) {
        this.costoUsd = costoUsd;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }


}
