package edespol.redvuelos.grafo;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

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

        // ---------- CRUD de aeropuertos y vuelos ----------
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

    public boolean eliminarAeropuerto(String codAero) {
        VerticeAeropuerto v = buscarVertice(codAero);
        if (v == null) return false;

        // quitar aristas entrantes
        for (VerticeAeropuerto u : vertices) {
            LinkedList<AristaVuelo> out = u.getSalientes();
            for (int j = 0; j < out.size(); j++) {
                if (codAero.equals(out.get(j).getCodDestino())) {
                    out.remove(j);
                    j--; 
                }
            }
        }
        vertices.remove(v);
        mapa.remove(codAero);
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

    
    public boolean eliminarVuelo(String vueloId) {
        boolean eliminado = false;

        for (VerticeAeropuerto v : this.vertices) {
            if (v.eliminarAristaPorId(vueloId)) {
                eliminado = true;
            }
        }

        return eliminado;
    }

     public LinkedList<Vuelo> obtenerSalientes(String codigoAeropuerto) {
        LinkedList<Vuelo> lista = new LinkedList<>();

        VerticeAeropuerto v = buscarVertice(codigoAeropuerto);
        if (v == null) {
            return lista;
        }

        for (AristaVuelo e : v.getSalientes()) {
            if (e.getVuelo() != null) {
                lista.add(e.getVuelo());
            }
        }

        return lista;
    }

    // ------------------- Internos -------------------

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

    //                      -- STATS--
    
    public int gradoSalida(String codigo) {
        VerticeAeropuerto v = buscarVertice(codigo);
        if (v == null) {
            return 0;
        }
        return v.getSalientes().size();
    }

    public int gradoEntrada(String codigo) {
        int count = 0;

        for (VerticeAeropuerto v : this.vertices) {
            for (AristaVuelo e : v.getSalientes()) {
                if (codigo.equals(e.getCodDestino())) {
                    count++;
                }
            }
        }

        return count;
    }

    public Aeropuerto masConectado() {
        Aeropuerto best = null;
        int bestDeg = -1;

        for (VerticeAeropuerto v : this.vertices) {
            String c = v.getDato().getCodigo();
            int deg = gradoSalida(c) + gradoEntrada(c);
            if (deg > bestDeg) {
                bestDeg = deg;
                best = v.getDato();
            }
        }

        return best;
    }

    public Aeropuerto menosConectado() {
        Aeropuerto worst = null;
        int worstDeg = Integer.MAX_VALUE;

        for (VerticeAeropuerto v : this.vertices) {
            String c = v.getDato().getCodigo();
            int deg = gradoSalida(c) + gradoEntrada(c);
            if (deg < worstDeg) {
                worstDeg = deg;
                worst = v.getDato();
            }
        }

        return worst;
    }

     // ------------------- BFS / DFS -------------------

    public LinkedList<String> bfs(String desdeCodigo) {
        LinkedList<String> orden = new LinkedList<>();

        if (!this.mapa.containsKey(desdeCodigo)) {
            return orden;
        }

        Map<String, Boolean> vis = new HashMap<>();
        LinkedList<String> q = new LinkedList<>();

        q.add(desdeCodigo);
        vis.put(desdeCodigo, Boolean.TRUE);

        while (!q.isEmpty()) {
            String u = q.removeFirst();
            orden.add(u);

            VerticeAeropuerto v = buscarVertice(u);
            if (v == null) {
                continue;
            }

            for (AristaVuelo e : v.getSalientes()) {
                String w = e.getCodDestino();
                if (!vis.containsKey(w)) {
                    vis.put(w, Boolean.TRUE);
                    q.add(w);
                }
            }
        }

        return orden;
    }

    public LinkedList<String> dfs(String desdeCodigo) {
        LinkedList<String> orden = new LinkedList<>();
        Map<String, Boolean> vis = new HashMap<>();

        dfsRec(desdeCodigo, vis, orden);
        return orden;
    }

    private void dfsRec(String u, Map<String, Boolean> vis, LinkedList<String> orden) {
        if (u == null || vis.containsKey(u) || !this.mapa.containsKey(u)) {
            return;
        }

        vis.put(u, Boolean.TRUE);
        orden.add(u);

        VerticeAeropuerto v = this.mapa.get(u);
        for (AristaVuelo e : v.getSalientes()) {
            dfsRec(e.getCodDestino(), vis, orden);
        }
    }

        // ------------------- Dijkstra  -------------------

    public ResultadoRuta dijkstra(String desdeCodigo, String haciaCodigo, MetricaPeso metrica) {
        ResultadoRuta res = new ResultadoRuta();

        if (!this.mapa.containsKey(desdeCodigo) || !this.mapa.containsKey(haciaCodigo)) {
            return res;
        }

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> previo = new HashMap<>();
        Map<String, Vuelo> vueloPrevio = new HashMap<>();

        for (VerticeAeropuerto v : this.vertices) {
            String c = v.getDato().getCodigo();
            dist.put(c, Double.POSITIVE_INFINITY);
            previo.put(c, null);
            vueloPrevio.put(c, null);
        }

        dist.put(desdeCodigo, 0.0);

        PriorityQueue<Map.Entry<String, Double>> pq =
            new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));

        pq.offer(new AbstractMap.SimpleEntry<>(desdeCodigo, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<String, Double> uEntry = pq.poll();
            String u = uEntry.getKey();
            double du = uEntry.getValue();

            if (du > dist.get(u)) {
                continue; // entrada obsoleta
            }

            if (u.equals(haciaCodigo)) {
                break; // llegamos con el mejor costo
            }

            VerticeAeropuerto v = this.mapa.get(u);
            if (v == null) {
                continue;
            }

            for (AristaVuelo av : v.getSalientes()) {
                Vuelo vu = av.getVuelo();
                String w = av.getCodDestino();

                double peso = (vu == null)
                              ? av.getPeso()
                              : calcularPeso(vu, metrica);

                double alt = du + peso;

                if (alt < dist.get(w)) {
                    dist.put(w, alt);
                    previo.put(w, u);
                    vueloPrevio.put(w, vu);
                    pq.offer(new AbstractMap.SimpleEntry<>(w, alt)); // decrease-key por reinserción
                }
            }
        }

        if (dist.get(haciaCodigo) == Double.POSITIVE_INFINITY) {
            return res;
        }

        LinkedList<String> ruta = new LinkedList<>();
        LinkedList<Vuelo> rVuelos = new LinkedList<>();

        String cur = haciaCodigo;

        while (cur != null) {
            ruta.addFirst(cur);

            Vuelo pf = vueloPrevio.get(cur);
            if (pf != null) {
                rVuelos.addFirst(pf);
            }

            cur = previo.get(cur);
        }

        res.setEncontrada(true);
        res.setPesoTotal(dist.get(haciaCodigo));
        res.getRutaAeropuertos().addAll(ruta);
        res.getRutaVuelos().addAll(rVuelos);

        return res;
    }

    // ------------------- Accesores -------------------

    public LinkedList<VerticeAeropuerto> getVertices() {
        return this.vertices;
    }

}
