package edespol.redvuelos.grafo;

import java.util.LinkedList;

import edespol.redvuelos.dominio.Aeropuerto;

public class VerticeAeropuerto {
    private Aeropuerto dato;
    private LinkedList<AristaVuelo> salientes;

    public VerticeAeropuerto( ) {
        this.dato = null;
        this.salientes = new LinkedList<>();
    }

    public void setDato(Aeropuerto dato) { 
        this.dato = dato; 
    }

    public LinkedList<AristaVuelo> getSalientes() {
        return this.salientes;
    }

    public void agregarArista(AristaVuelo e) {
        this.salientes.add(e);
    }

    public boolean eliminarAristaPorId(String vueloId) {
        for (int i = 0; i < this.salientes.size(); i++) {
            AristaVuelo e = this.salientes.get(i);
            if (e.coincideIdVuelo(vueloId)) {
                this.salientes.remove(i);
                return true;
            }
        }
        return false;
    }
}
