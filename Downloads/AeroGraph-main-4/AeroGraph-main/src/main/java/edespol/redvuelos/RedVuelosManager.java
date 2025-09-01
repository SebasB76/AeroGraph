package edespol.redvuelos;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.GrafoAeropuertos.RutaOptima;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Clase principal para gestionar la red de vuelos y proporcionar
 * funcionalidades de búsqueda de rutas y estadísticas
 */
public class RedVuelosManager {
    private GrafoAeropuertos grafo;
    
    public RedVuelosManager() {
        this.grafo = new GrafoAeropuertos();
    }
    
    /**
     * Carga los datos iniciales desde Firebase y construye el grafo
     */
    public void cargarDatosIniciales() throws IOException, Exception {
        // Cargar aeropuertos y vuelos desde Firebase
        List<Aeropuerto> aeropuertos = Persistencia.cargarAeropuertos();
        List<Vuelo> vuelos = Persistencia.cargarVuelos();
        
        // Construir el grafo
        for (Aeropuerto aeropuerto : aeropuertos) {
            grafo.agregarAeropuerto(aeropuerto);
        }
        
        for (Vuelo vuelo : vuelos) {
            grafo.agregarVuelo(vuelo);
        }
        
        System.out.println("✅ Grafo construido con " + aeropuertos.size() + " aeropuertos y " + vuelos.size() + " vuelos");
    }
    
    /**
     * Encuentra la ruta óptima entre dos aeropuertos usando Dijkstra
     */
    public RutaOptima encontrarRutaOptima(String origen, String destino, MetricaPeso metrica) {
        if (origen == null || destino == null) {
            return null;
        }
        
        System.out.println("🔍 Buscando ruta óptima de " + origen + " a " + destino + " usando " + metrica);
        
        RutaOptima ruta = grafo.encontrarRutaOptima(origen, destino, metrica);
        
        if (ruta != null) {
            System.out.println("✅ Ruta encontrada: " + ruta);
        } else {
            System.out.println("❌ No se encontró ruta entre " + origen + " y " + destino);
        }
        
        return ruta;
    }
    
    /**
     * Busca una ruta usando BFS (menor número de escalas)
     */
    public List<String> buscarRutaBFS(String origen, String destino) {
        if (origen == null || destino == null) {
            return null;
        }
        
        System.out.println("🔍 Buscando ruta BFS de " + origen + " a " + destino);
        
        List<String> ruta = grafo.buscarRutaBFS(origen, destino);
        
        if (ruta != null) {
            System.out.println("✅ Ruta BFS encontrada: " + String.join(" → ", ruta));
        } else {
            System.out.println("❌ No se encontró ruta BFS entre " + origen + " y " + destino);
        }
        
        return ruta;
    }
    
    /**
     * Busca una ruta usando DFS
     */
    public List<String> buscarRutaDFS(String origen, String destino) {
        if (origen == null || destino == null) {
            return null;
        }
        
        System.out.println("🔍 Buscando ruta DFS de " + origen + " a " + destino);
        
        List<String> ruta = grafo.buscarRutaDFS(origen, destino);
        
        if (ruta != null) {
            System.out.println("✅ Ruta DFS encontrada: " + String.join(" → ", ruta));
        } else {
            System.out.println("❌ No se encontró ruta DFS entre " + origen + " y " + destino);
        }
        
        return ruta;
    }
    
    /**
     * Obtiene estadísticas de conectividad de la red
     */
    public void mostrarEstadisticasConectividad() {
        System.out.println("\n📊 ESTADÍSTICAS DE CONECTIVIDAD");
        System.out.println("=================================");
        
        Map<String, Integer> estadisticas = grafo.obtenerEstadisticasConectividad();
        
        for (Map.Entry<String, Integer> entry : estadisticas.entrySet()) {
            System.out.println("Aeropuerto " + entry.getKey() + ": " + entry.getValue() + " conexiones salientes");
        }
        
        String masConectado = grafo.obtenerAeropuertoMasConectado();
        String menosConectado = grafo.obtenerAeropuertoMenosConectado();
        
        if (masConectado != null) {
            System.out.println("\n🏆 Aeropuerto más conectado: " + masConectado + 
                             " (" + grafo.obtenerNumeroConexiones(masConectado) + " conexiones)");
        }
        
        if (menosConectado != null) {
            System.out.println("📉 Aeropuerto menos conectado: " + menosConectado + 
                             " (" + grafo.obtenerNumeroConexiones(menosConectado) + " conexiones)");
        }
        
        System.out.println("\n📈 Resumen de la red:");
        System.out.println("   - Total aeropuertos: " + grafo.obtenerNumeroVertices());
        System.out.println("   - Total vuelos: " + grafo.obtenerNumeroAristas());
    }
    
    /**
     * Verifica si existe una ruta entre dos aeropuertos
     */
    public boolean existeRuta(String origen, String destino) {
        boolean existe = grafo.existeRuta(origen, destino);
        System.out.println("🔍 ¿Existe ruta de " + origen + " a " + destino + "? " + (existe ? "✅ Sí" : "❌ No"));
        return existe;
    }
    
    /**
     * Obtiene todos los vuelos desde un aeropuerto específico
     */
    public List<Vuelo> obtenerVuelosDesde(String codigoAeropuerto) {
        List<Vuelo> vuelos = grafo.obtenerVuelosDesde(codigoAeropuerto);
        System.out.println("✈️ Vuelos desde " + codigoAeropuerto + ": " + vuelos.size());
        return vuelos;
    }
    
    /**
     * Obtiene el grafo subyacente
     */
    public GrafoAeropuertos getGrafo() {
        return grafo;
    }
    
    /**
     * Ejecuta una demostración de todas las funcionalidades
     */
    public void ejecutarDemo() {
        try {
            System.out.println("🚀 INICIANDO DEMO DE LA RED DE VUELOS");
            System.out.println("=====================================");
            
            // Cargar datos
            cargarDatosIniciales();
            
            // Mostrar estadísticas
            mostrarEstadisticasConectividad();
            
            // Buscar algunas rutas de ejemplo (si hay datos)
            if (grafo.obtenerNumeroVertices() > 0) {
                List<Aeropuerto> aeropuertos = grafo.obtenerAeropuertos();
                if (aeropuertos.size() >= 2) {
                    String origen = aeropuertos.get(0).getCodigo();
                    String destino = aeropuertos.get(1).getCodigo();
                    
                    System.out.println("\n🔍 DEMO DE BÚSQUEDA DE RUTAS");
                    System.out.println("=============================");
                    
                    // Búsqueda BFS
                    buscarRutaBFS(origen, destino);
                    
                    // Búsqueda DFS
                    buscarRutaDFS(origen, destino);
                    
                    // Ruta óptima por distancia
                    encontrarRutaOptima(origen, destino, MetricaPeso.DISTANCIA);
                    
                    // Verificar existencia de ruta
                    existeRuta(origen, destino);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en la demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
