package edespol.redvuelos.grafo;

import java.util.List;

import edespol.redvuelos.dominio.Vuelo;

public class RutaOptima {
    private List<String> aeropuertos;
    private double pesoTotal;
    private List<Vuelo> vuelos;

    public RutaOptima(List<String> aeropuertos, double pesoTotal, List<Vuelo> vuelos) {
        this.aeropuertos = aeropuertos;
        this.pesoTotal = pesoTotal;
        this.vuelos = vuelos;
    }
    
    public List<String> getAeropuertos() { 
        return aeropuertos; 
    }
    
    public double getPesoTotal() { 
        return pesoTotal; 
    }
    
    public List<Vuelo> getVuelos() { 
        return vuelos; 
    }
    
    @Override
    public String toString() {
        return "Ruta: " + String.join(" → ", aeropuertos) + 
               " | Peso: " + String.format("%.2f", pesoTotal);
    }
}
