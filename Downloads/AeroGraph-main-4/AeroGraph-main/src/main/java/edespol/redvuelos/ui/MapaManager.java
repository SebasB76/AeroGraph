package edespol.redvuelos.ui;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Locale;

/**
 * Manager para gestionar el mapa y navegador web
 */
public class MapaManager {
    
    private Browser browser;
    private Engine engine;
    private BrowserView view;
    
    public MapaManager() {
        inicializarNavegador();
    }
    
    /**
     * Inicializa el navegador JxBrowser
     */
    private void inicializarNavegador() {
        engine = Engine.newInstance(
                EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                        .licenseKey("OK6AEKNYF41NZFIP4Q91OV3MD2T0I7RRJ7735VPC9H9LARBCD97CGJPIBE5TQNWR6EB6V1EKKUAJXP4UJTC2MFTAMGWFQN2JHOK701JNU4LV5A6SF4KFR0FWXPJIZRKJ34QM6UDKRB3103YFD")
                        .build()
        );
        browser = engine.newBrowser();
        view = BrowserView.newInstance(browser);
        
        // Cargar desde servidor web
        browser.navigation().loadUrl("http://localhost:8080/");
    }
    
    /**
     * Obtiene la vista del navegador
     */
    public BrowserView getView() {
        return view;
    }
    
    /**
     * Carga el mapa con datos de aeropuertos y vuelos
     */
    public void cargarMapa(List<Aeropuerto> aeropuertos, List<Vuelo> vuelos) {
        try {
            String aeropuertosJson = convertirAeropuertosAJson(aeropuertos);
            String vuelosJson = convertirVuelosAJson(vuelos);

            // JS que espera a que el mapa esté listo antes de dibujar
            String js = "if (typeof waitMapReady !== 'undefined') { " +
                        "waitMapReady(function() { " +
                        "updateMapData(JSON.parse('" + escapeJavaScript(aeropuertosJson) + "'), " +
                        "JSON.parse('" + escapeJavaScript(vuelosJson) + "')); }); " +
                        "} else { " +
                        "setTimeout(function() { " +
                        "updateMapData(JSON.parse('" + escapeJavaScript(aeropuertosJson) + "'), " +
                        "JSON.parse('" + escapeJavaScript(vuelosJson) + "')); }, 1000); " +
                        "}";

            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(js));
            System.out.println("Datos enviados al mapa");

        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarError("Error al cargar el mapa: " + ex.getMessage());
        }
    }
    
    /**
     * Recarga el mapa
     */
    public void recargarMapa() {
        browser.navigation().reload();
    }
    
    /**
     * Cierra el navegador
     */
    public void cerrar() {
        if (engine != null) {
            engine.close();
        }
    }
    
    /**
     * Convierte lista de aeropuertos a JSON
     */
    private String convertirAeropuertosAJson(List<Aeropuerto> aeropuertos) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < aeropuertos.size(); i++) {
            Aeropuerto a = aeropuertos.get(i);
            json.append(String.format(
                    Locale.US,
                    "{\"codigo\":\"%s\",\"nombre\":\"%s\",\"ciudad\":\"%s\",\"pais\":\"%s\",\"latitud\":%.6f,\"longitud\":%.6f}",
                    escapeJson(a.getCodigo()),
                    escapeJson(a.getNombre()),
                    escapeJson(a.getCiudad()),
                    escapeJson(a.getPais()),
                    a.getLatitud(),
                    a.getLongitud()
            ));
            if (i < aeropuertos.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    
    /**
     * Convierte lista de vuelos a JSON
     */
    private String convertirVuelosAJson(List<Vuelo> vuelos) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < vuelos.size(); i++) {
            Vuelo v = vuelos.get(i);
            json.append(String.format(
                    Locale.US,
                    "{\"origenCodigo\":\"%s\",\"destinoCodigo\":\"%s\",\"aerolineaCodigo\":\"%s\"," +
                    "\"distanciaKm\":%.2f,\"duracionMin\":%d,\"costoUsd\":%.2f,\"horaSalida\":\"%s\"}",
                    escapeJson(v.getOrigenCodigo()),
                    escapeJson(v.getDestinoCodigo()),
                    escapeJson(v.getAerolineaCodigo()),
                    v.getDistanciaKm(),
                    v.getDuracionMin(),
                    v.getCostoUsd(),
                    escapeJson(v.getHoraSalida())
            ));
            if (i < vuelos.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    /**
     * Escapa caracteres especiales para JavaScript
     */
    private String escapeJavaScript(String input) {
        return input.replace("'", "\\'");
    }
    
    /**
     * Muestra un error
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.show();
    }
}
