package edespol.redvuelos.grafo;

import java.util.*;

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


    public static class RutaOptima {
        private List<String> aeropuertos;
        private double pesoTotal;
        private List<Vuelo> vuelos;
        
        public RutaOptima(List<String> aeropuertos, double pesoTotal, List<Vuelo> vuelos) {
            this.aeropuertos = aeropuertos;
            this.pesoTotal = pesoTotal;
            this.vuelos = vuelos;
        }
        
        public List<String> getAeropuertos() { return aeropuertos; }
        public double getPesoTotal() { return pesoTotal; }
        public List<Vuelo> getVuelos() { return vuelos; }
        
        @Override
        public String toString() {
            return "Ruta: " + String.join(" → ", aeropuertos) + 
                   " | Peso: " + String.format("%.2f", pesoTotal);
        }
    }
    
    public RutaOptima encontrarRutaOptima(String origen, String destino, MetricaPeso metrica) {
        if (origen == null || destino == null || buscarVertice(origen) == null || buscarVertice(destino) == null) {
            return null;
        }
        
        // Estructuras para Dijkstra
        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();
        Map<String, Vuelo> vuelosUsados = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> cola = new PriorityQueue<>(
            (a, b) -> Double.compare(a.getValue(), b.getValue())
        );
        
        // Inicialización
        for (VerticeAeropuerto v : vertices) {
            String codigo = v.getDato().getCodigo();
            distancias.put(codigo, Double.MAX_VALUE);
            predecesores.put(codigo, null);
        }
        distancias.put(origen, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(origen, 0.0));
        
        // Algoritmo de Dijkstra
        while (!cola.isEmpty()) {
            String u = cola.poll().getKey();
            double distU = distancias.get(u);
            
            if (u.equals(destino)) {
                break; // Llegamos al destino
            }
            
            VerticeAeropuerto verticeU = buscarVertice(u);
            if (verticeU == null) continue;
            
            for (AristaVuelo arista : verticeU.getSalientes()) {
                String v = arista.getCodDestino();
                double peso = calcularPeso(arista.getVuelo(), metrica);
                
                if (distU + peso < distancias.get(v)) {
                    distancias.put(v, distU + peso);
                    predecesores.put(v, u);
                    vuelosUsados.put(v, arista.getVuelo());
                    cola.offer(new AbstractMap.SimpleEntry<>(v, distU + peso));
                }
            }
        }
        
        // Reconstruir la ruta
        if (distancias.get(destino) == Double.MAX_VALUE) {
            return null; // No hay ruta
        }
        
        List<String> ruta = new ArrayList<>();
        List<Vuelo> vuelosRuta = new ArrayList<>();
        String actual = destino;
        
        while (actual != null) {
            ruta.add(0, actual);
            if (vuelosUsados.get(actual) != null) {
                vuelosRuta.add(0, vuelosUsados.get(actual));
            }
            actual = predecesores.get(actual);
        }
        
        return new RutaOptima(ruta, distancias.get(destino), vuelosRuta);
    }
    
    public List<String> buscarRutaBFS(String origen, String destino) {
        if (origen == null || destino == null || buscarVertice(origen) == null || buscarVertice(destino) == null) {
            return null;
        }
        
        Queue<String> cola = new LinkedList<>();
        Map<String, String> predecesores = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        
        cola.offer(origen);
        visitados.add(origen);
        predecesores.put(origen, null);
        
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            
            if (actual.equals(destino)) {
                // Reconstruir ruta
                List<String> ruta = new ArrayList<>();
                String nodo = destino;
                while (nodo != null) {
                    ruta.add(0, nodo);
                    nodo = predecesores.get(nodo);
                }
                return ruta;
            }
            
            VerticeAeropuerto vertice = buscarVertice(actual);
            for (AristaVuelo arista : vertice.getSalientes()) {
                String vecino = arista.getCodDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.offer(vecino);
                }
            }
        }
        
        return null; // No hay ruta
    }
    

    public List<String> buscarRutaDFS(String origen, String destino) {
        if (origen == null || destino == null || buscarVertice(origen) == null || buscarVertice(destino) == null) {
            return null;
        }
        
        Set<String> visitados = new HashSet<>();
        Map<String, String> predecesores = new HashMap<>();
        
        if (dfsRecursivo(origen, destino, visitados, predecesores)) {
            // Reconstruir ruta
            List<String> ruta = new ArrayList<>();
            String nodo = destino;
            while (nodo != null) {
                ruta.add(0, nodo);
                nodo = predecesores.get(nodo);
            }
            return ruta;
        }
        
        return null;
    }
    
    public List<String> buscarRutaDFSPila(String origen, String destino) {
        if (origen == null || destino == null || buscarVertice(origen) == null || buscarVertice(destino) == null) {
            return null;
        }
        
        Set<String> visitados = new HashSet<>();
        Map<String, String> predecesores = new HashMap<>();
        Stack<String> pila = new Stack<>();
        
        // Inicializar con el nodo origen
        pila.push(origen);
        visitados.add(origen);
        predecesores.put(origen, null);
        
        while (!pila.isEmpty()) {
            String nodoActual = pila.peek(); // Solo mirar, no sacar aún
            
            // Si encontramos el destino, reconstruir ruta
            if (nodoActual.equals(destino)) {
                List<String> ruta = new ArrayList<>();
                String nodo = destino;
                while (nodo != null) {
                    ruta.add(0, nodo);
                    nodo = predecesores.get(nodo);
                }
                return ruta;
            }
            
            // Explorar vecinos del nodo actual
            VerticeAeropuerto vertice = buscarVertice(nodoActual);
            if (vertice != null) {
                // Buscar el primer vecino no visitado
                String proximoVecino = null;
                for (AristaVuelo arista : vertice.getSalientes()) {
                    String vecino = arista.getCodDestino();
                    if (!visitados.contains(vecino)) {
                        proximoVecino = vecino;
                        break; // Solo tomar el primer vecino no visitado
                    }
                }
                
                if (proximoVecino != null) {
                    // Explorar este vecino (DFS: ir hacia adelante)
                    visitados.add(proximoVecino);
                    predecesores.put(proximoVecino, nodoActual);
                    pila.push(proximoVecino);
                } else {
                    // No hay más vecinos no visitados, retroceder
                    pila.pop();
                }
            } else {
                // Nodo no válido, retroceder
                pila.pop();
            }
        }
        
        return null; // No se encontró ruta
    }
    
    private boolean dfsRecursivo(String actual, String destino, Set<String> visitados, Map<String, String> predecesores) {
        visitados.add(actual);
        
        if (actual.equals(destino)) {
            return true;
        }
        
        VerticeAeropuerto vertice = buscarVertice(actual);
        for (AristaVuelo arista : vertice.getSalientes()) {
            String vecino = arista.getCodDestino();
            if (!visitados.contains(vecino)) {
                predecesores.put(vecino, actual);
                if (dfsRecursivo(vecino, destino, visitados, predecesores)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // ======================================================
    // 🔹 ESTADÍSTICAS DE CONECTIVIDAD
    // ======================================================
    
    public int obtenerNumeroConexiones(String codigoAeropuerto) {
        VerticeAeropuerto vertice = buscarVertice(codigoAeropuerto);
        return vertice != null ? vertice.getSalientes().size() : 0;
    }
    
    public String obtenerAeropuertoMasConectado() {
        String masConectado = null;
        int maxConexiones = -1;
        
        for (VerticeAeropuerto vertice : vertices) {
            int conexiones = vertice.getSalientes().size();
            if (conexiones > maxConexiones) {
                maxConexiones = conexiones;
                masConectado = vertice.getDato().getCodigo();
            }
        }
        
        return masConectado;
    }
    
    public String obtenerAeropuertoMenosConectado() {
        String menosConectado = null;
        int minConexiones = Integer.MAX_VALUE;
        
        for (VerticeAeropuerto vertice : vertices) {
            int conexiones = vertice.getSalientes().size();
            if (conexiones < minConexiones) {
                minConexiones = conexiones;
                menosConectado = vertice.getDato().getCodigo();
            }
        }
        
        return menosConectado;
    }
    
    public Map<String, Integer> obtenerEstadisticasConectividad() {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        for (VerticeAeropuerto vertice : vertices) {
            String codigo = vertice.getDato().getCodigo();
            int conexiones = vertice.getSalientes().size();
            estadisticas.put(codigo, conexiones);
        }
        
        return estadisticas;
    }
    
    // ======================================================
    // 🔹 MÉTODOS ADICIONALES ÚTILES
    // ======================================================
    
    public List<Aeropuerto> obtenerAeropuertos() {
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        for (VerticeAeropuerto vertice : vertices) {
            aeropuertos.add(vertice.getDato());
        }
        return aeropuertos;
    }
    
    public List<Vuelo> obtenerVuelosDesde(String codigoAeropuerto) {
        VerticeAeropuerto vertice = buscarVertice(codigoAeropuerto);
        if (vertice == null) return new ArrayList<>();
        
        List<Vuelo> vuelos = new ArrayList<>();
        for (AristaVuelo arista : vertice.getSalientes()) {
            if (arista.getVuelo() != null) {
                vuelos.add(arista.getVuelo());
            }
        }
        return vuelos;
    }
    
    public boolean existeRuta(String origen, String destino) {
        return buscarRutaBFS(origen, destino) != null;
    }
    
    public int obtenerNumeroVertices() {
        return vertices.size();
    }
    
    public int obtenerNumeroAristas() {
        int total = 0;
        for (VerticeAeropuerto vertice : vertices) {
            total += vertice.getSalientes().size();
        }
        return total;
    }
}
