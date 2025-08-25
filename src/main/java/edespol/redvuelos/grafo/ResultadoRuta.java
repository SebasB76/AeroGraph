package edespol.redvuelos.grafo;

import java.util.LinkedList;

import edespol.redvuelos.dominio.Vuelo;

public class ResultadoRuta {

    private boolean encontrada;
    private double pesoTotal;
    private LinkedList<String> rutaAeropuertos;
    private LinkedList<Vuelo> rutaVuelos;

    public ResultadoRuta() {
        this.encontrada = false;
        this.pesoTotal = 0.0;
        this.rutaAeropuertos = new LinkedList<>();
        this.rutaVuelos = new LinkedList<>();
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public void setEncontrada(boolean encontrada) {
        this.encontrada = encontrada;
    }

    public double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public LinkedList<String> getRutaAeropuertos() {
        return rutaAeropuertos;
    }

    public LinkedList<Vuelo> getRutaVuelos() {
        return rutaVuelos;
    }
}
