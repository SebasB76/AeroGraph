package edespol.redvuelos.grafo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.dominio.Vuelo;

public class GrafoAeropuertos {
    private LinkedList<VerticeAeropuerto> vertices;
    private Map<String, VerticeAeropuerto> mapa ;// acceso rapido por codigo de aeropuerto
    private boolean dirigido ;
    public GrafoAeropuertos() {
        this.vertices = new LinkedList<>();
        this.mapa = new HashMap<String, VerticeAeropuerto>();
        this.dirigido = true;
    }

    public boolean agregarAeropuerto(Aeropuerto a ){
        if (a == null || a.getCodigo() == null){return false;}
        if(buscarVertice(a.getCodigo()) != null) {
            return false; 
        }
        VerticeAeropuerto v= new VerticeAeropuerto();
        v.setDato(a);
        this.vertices.add(v);
        this.mapa.put(a.getCodigo(), v);
        return true;
    }

    public boolean eliminarAeropuerto (String codAero) {
        VerticeAeropuerto v = buscarVertice(codAero);
        if (v == null) {
            return false;
        }
        // quitar aristas entrantes
        for (int i = 0; i < this.vertices.size(); i++) {
            VerticeAeropuerto u = vertices.get(i);
            LinkedList<AristaVuelo> out = u.getSalientes();
            for (int j = 0; j < out.size(); j++) {
                AristaVuelo e = out.get(j);
                if (codAero.equals(e.getCodDestino())) {
                    out.remove(j);
                    j--;
                }
            }
        }
        this.vertices.remove(v);
        this.mapa.remove(codAero);
        return true;
    }

    public boolean agregarVuelo (Vuelo v){
        if (v == null || v.getOrigenCodigo() == null || v.getDestinoCodigo() == null) {
            return false;
        }
        VerticeAeropuerto origen = buscarVertice(v.getOrigenCodigo());
        VerticeAeropuerto destino = buscarVertice(v.getDestinoCodigo());
        if (origen == null || destino == null) {
            return false;
        }
        AristaVuelo a= new AristaVuelo();
        a.setCodDestino(v.getDestinoCodigo());
        a.setPeso(v.getDistanciaKm());
        a.setVuelo(v);
        origen.agregarArista(a);
        return true;
    }

    private VerticeAeropuerto buscarVertice(String codAero) {
        return mapa.get(codAero);
    }

    private double calcularPeso(Vuelo v, MetricaPeso metrica) {
        if (metrica == MetricaPeso.DURACION) {
            return (double) v.getDuracionMin();
        }
        if (metrica == MetricaPeso.COSTO) {
            return v.getCostoUsd();
        }
        return v.getDistanciaKm();
    }


}
