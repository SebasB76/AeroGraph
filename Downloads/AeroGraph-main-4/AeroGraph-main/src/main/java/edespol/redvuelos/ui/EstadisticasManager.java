package edespol.redvuelos.ui;

import edespol.redvuelos.RedVuelosManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Manager para gestionar las estadísticas de la aplicación
 */
public class EstadisticasManager {
    
    private Stage ownerStage;
    
    public EstadisticasManager(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }
    
    /**
     * Muestra las estadísticas de conectividad
     */
    public void mostrarEstadisticas() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Estadísticas de Conectividad");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextArea txtEstadisticas = new TextArea();
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setPrefSize(500, 400);
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> dialog.close());

        vbox.getChildren().addAll(
                new Label("Estadísticas de la Red de Vuelos:"),
                txtEstadisticas,
                btnCerrar
        );

        // Ejecutar en hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                RedVuelosManager manager = new RedVuelosManager();
                manager.cargarDatosIniciales();
                
                // Capturar la salida de consola
                StringBuilder estadisticas = new StringBuilder();
                estadisticas.append("ESTADÍSTICAS DE CONECTIVIDAD\n");
                estadisticas.append("=================================\n\n");
                
                // Obtener estadísticas del grafo
                Map<String, Integer> stats = manager.getGrafo().obtenerEstadisticasConectividad();
                for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                    estadisticas.append("Aeropuerto ").append(entry.getKey())
                               .append(": ").append(entry.getValue())
                               .append(" conexiones salientes\n");
                }
                
                estadisticas.append("\n🏆 Aeropuerto más conectado: ")
                           .append(manager.getGrafo().obtenerAeropuertoMasConectado())
                           .append(" (").append(manager.getGrafo().obtenerNumeroConexiones(
                               manager.getGrafo().obtenerAeropuertoMasConectado())).append(" conexiones)\n");
                
                estadisticas.append("📉 Aeropuerto menos conectado: ")
                           .append(manager.getGrafo().obtenerAeropuertoMenosConectado())
                           .append(" (").append(manager.getGrafo().obtenerNumeroConexiones(
                               manager.getGrafo().obtenerAeropuertoMenosConectado())).append(" conexiones)\n");
                
                estadisticas.append("\nResumen de la red:\n");
                estadisticas.append("   - Total aeropuertos: ").append(manager.getGrafo().obtenerNumeroVertices()).append("\n");
                estadisticas.append("   - Total vuelos: ").append(manager.getGrafo().obtenerNumeroAristas()).append("\n");
                
                // Actualizar UI en el hilo principal
                Platform.runLater(() -> txtEstadisticas.setText(estadisticas.toString()));
                
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> txtEstadisticas.setText("Error al cargar estadísticas: " + ex.getMessage()));
            }
        }).start();

        dialog.setScene(new Scene(vbox, 550, 500));
        dialog.showAndWait();
    }
    
    /**
     * Ejecuta el demo de la aplicación
     */
    public void ejecutarDemo() {
        new Thread(() -> {
            try {
                RedVuelosManager manager = new RedVuelosManager();
                manager.ejecutarDemo();
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, "Error en demo: " + ex.getMessage()).show();
                });
            }
        }).start();
        
        new Alert(Alert.AlertType.INFORMATION, 
                 "Demo iniciado. Revisa la consola para ver los resultados.").show();
    }
}
