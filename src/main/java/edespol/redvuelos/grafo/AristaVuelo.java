package edespol.redvuelos.grafo;

import edespol.redvuelos.dominio.Vuelo;

public class AristaVuelo {
    private String codDestino;
    private double peso; // Puede representar distancia, duración o costo según la métrica seleccionada
    private Vuelo vuelo; // Identificador único del vuelo

    public AristaVuelo() {
        this.codDestino = null;
        this.peso = 0.0;
        this.vuelo = null;
    }

    public String getCodDestino() {
        return codDestino;
    }
    public void setCodDestino(String codDestino) {
        this.codDestino = codDestino;
    }
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
    public boolean coincideIdVuelo(String vueloId) {
         return this.vuelo != null  && this.vuelo.getId() != null  && this.vuelo.getId().equals(vueloId);
          
    }

}
