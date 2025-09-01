package edespol.redvuelos.ui;

import java.util.List;

import edespol.redvuelos.RedVuelosManager;
import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.RutaOptima;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Manager para gestionar la búsqueda de rutas
 */
public class RutaManager {
    
    private Stage ownerStage;
    
    public RutaManager(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }
    
    /**
     * Muestra el formulario para buscar ruta óptima con Dijkstra
     */
    public void mostrarFormularioBuscarRutaOptima() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Buscar Ruta Óptima");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // ComboBox con lista de aeropuertos
        ComboBox<String> cmbOrigen = new ComboBox<>();
        ComboBox<String> cmbDestino = new ComboBox<>();
        
        // Cargar lista de aeropuertos
        cargarAeropuertosEnComboBox(cmbOrigen, cmbDestino);
        
        ComboBox<MetricaPeso> cmbMetrica = new ComboBox<>();
        cmbMetrica.getItems().addAll(MetricaPeso.values());
        cmbMetrica.setValue(MetricaPeso.DISTANCIA);
        
        Button btnBuscar = new Button("Buscar Ruta");
        TextArea txtResultado = new TextArea();
        txtResultado.setEditable(false);
        txtResultado.setPrefSize(400, 200);

        vbox.getChildren().addAll(
                new Label("Aeropuerto Origen:"), cmbOrigen,
                new Label("Aeropuerto Destino:"), cmbDestino,
                new Label("Métrica:"), cmbMetrica,
                btnBuscar, txtResultado
        );

        btnBuscar.setOnAction(e -> {
            try {
                String origen = extraerCodigoAeropuerto(cmbOrigen.getValue());
                String destino = extraerCodigoAeropuerto(cmbDestino.getValue());
                
                if (origen == null || destino == null) {
                    new Alert(Alert.AlertType.WARNING, "Seleccione origen y destino").show();
                    return;
                }
                
                if (origen.equals(destino)) {
                    new Alert(Alert.AlertType.WARNING, "El origen y destino no pueden ser iguales").show();
                    return;
                }

                // Crear manager y buscar ruta
                RedVuelosManager manager = new RedVuelosManager();
                manager.cargarDatosIniciales();
                
                RutaOptima ruta = manager.encontrarRutaOptima(origen, destino, cmbMetrica.getValue());
                
                if (ruta != null) {
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("RUTA ÓPTIMA ENCONTRADA\n");
                    resultado.append("========================\n");
                    resultado.append("Origen: ").append(origen).append("\n");
                    resultado.append("Destino: ").append(destino).append("\n");
                    resultado.append("Métrica: ").append(cmbMetrica.getValue()).append("\n");
                    resultado.append("Peso total: ").append(String.format("%.2f", ruta.getPesoTotal())).append("\n\n");
                    resultado.append("RUTA:\n");
                    resultado.append(String.join(" → ", ruta.getAeropuertos())).append("\n\n");
                    resultado.append("VUELOS:\n");
                    for (Vuelo vuelo : ruta.getVuelos()) {
                        resultado.append(vuelo.getOrigenCodigo())
                                .append(" → ").append(vuelo.getDestinoCodigo())
                                .append(" (").append(vuelo.getAerolineaCodigo()).append(")\n");
                    }
                    txtResultado.setText(resultado.toString());
                } else {
                    txtResultado.setText("No se encontró ruta entre " + origen + " y " + destino);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        dialog.setScene(new Scene(vbox, 450, 500));
        dialog.showAndWait();
    }
    
    /**
     * Muestra el formulario para buscar ruta con BFS
     */
    public void mostrarFormularioBuscarRutaBFS() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Buscar Ruta BFS");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // ComboBox con lista de aeropuertos
        ComboBox<String> cmbOrigen = new ComboBox<>();
        ComboBox<String> cmbDestino = new ComboBox<>();
        
        // Cargar lista de aeropuertos
        cargarAeropuertosEnComboBox(cmbOrigen, cmbDestino);
        Button btnBuscar = new Button("Buscar Ruta");
        TextArea txtResultado = new TextArea();
        txtResultado.setEditable(false);
        txtResultado.setPrefSize(400, 200);

        vbox.getChildren().addAll(
                new Label("Aeropuerto Origen:"), cmbOrigen,
                new Label("Aeropuerto Destino:"), cmbDestino,
                btnBuscar, txtResultado
        );

        btnBuscar.setOnAction(e -> {
            try {
                String origen = extraerCodigoAeropuerto(cmbOrigen.getValue());
                String destino = extraerCodigoAeropuerto(cmbDestino.getValue());
                
                if (origen == null || destino == null) {
                    new Alert(Alert.AlertType.WARNING, "Seleccione origen y destino").show();
                    return;
                }
                
                if (origen.equals(destino)) {
                    new Alert(Alert.AlertType.WARNING, "El origen y destino no pueden ser iguales").show();
                    return;
                }

                RedVuelosManager manager = new RedVuelosManager();
                manager.cargarDatosIniciales();
                
                List<String> ruta = manager.buscarRutaBFS(origen, destino);
                
                if (ruta != null) {
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("RUTA BFS ENCONTRADA\n");
                    resultado.append("=====================\n");
                    resultado.append("Origen: ").append(origen).append("\n");
                    resultado.append("Destino: ").append(destino).append("\n");
                    resultado.append("Número de escalas: ").append(ruta.size() - 2).append("\n\n");
                    resultado.append("RUTA:\n");
                    resultado.append(String.join(" → ", ruta));
                    txtResultado.setText(resultado.toString());
                } else {
                    txtResultado.setText("No se encontró ruta entre " + origen + " y " + destino);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        dialog.setScene(new Scene(vbox, 450, 500));
        dialog.showAndWait();
    }
    
    /**
     * Muestra el formulario para buscar ruta con DFS
     */
    public void mostrarFormularioBuscarRutaDFS() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Buscar Ruta DFS");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // ComboBox con lista de aeropuertos
        ComboBox<String> cmbOrigen = new ComboBox<>();
        ComboBox<String> cmbDestino = new ComboBox<>();
        
        // Cargar lista de aeropuertos
        cargarAeropuertosEnComboBox(cmbOrigen, cmbDestino);
        
        Button btnBuscar = new Button("Buscar Ruta");
        TextArea txtResultado = new TextArea();
        txtResultado.setEditable(false);
        txtResultado.setPrefSize(400, 200);

        vbox.getChildren().addAll(
                new Label("Aeropuerto Origen:"), cmbOrigen,
                new Label("Aeropuerto Destino:"), cmbDestino,
                btnBuscar, txtResultado
        );

        btnBuscar.setOnAction(e -> {
            try {
                String origen = extraerCodigoAeropuerto(cmbOrigen.getValue());
                String destino = extraerCodigoAeropuerto(cmbDestino.getValue());
                
                if (origen == null || destino == null) {
                    new Alert(Alert.AlertType.WARNING, "Seleccione origen y destino").show();
                    return;
                }
                
                if (origen.equals(destino)) {
                    new Alert(Alert.AlertType.WARNING, "El origen y destino no pueden ser iguales").show();
                    return;
                }

                RedVuelosManager manager = new RedVuelosManager();
                manager.cargarDatosIniciales();
                
                List<String> ruta = manager.buscarRutaDFS(origen, destino);
                
                if (ruta != null) {
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("RUTA DFS ENCONTRADA\n");
                    resultado.append("=====================\n");
                    resultado.append("Origen: ").append(origen).append("\n");
                    resultado.append("Destino: ").append(destino).append("\n");
                    resultado.append("Número de escalas: ").append(ruta.size() - 2).append("\n\n");
                    resultado.append("RUTA:\n");
                    resultado.append(String.join(" → ", ruta));
                    txtResultado.setText(resultado.toString());
                } else {
                    txtResultado.setText("No se encontró ruta entre " + origen + " y " + destino);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        dialog.setScene(new Scene(vbox, 450, 500));
        dialog.showAndWait();
    }
    
    /**
     * Carga aeropuertos en ComboBox
     */
    private void cargarAeropuertosEnComboBox(ComboBox<String> cmbOrigen, ComboBox<String> cmbDestino) {
        try {
            List<Aeropuerto> aeropuertos = edespol.redvuelos.Persistencia.cargarAeropuertos();
            
            // Limpiar items existentes
            cmbOrigen.getItems().clear();
            cmbDestino.getItems().clear();
            
            // Agregar aeropuertos con formato descriptivo
            for (Aeropuerto a : aeropuertos) {
                String descripcion = a.getCodigo() + " - " + a.getNombre() + " (" + a.getCiudad() + ", " + a.getPais() + ")";
                cmbOrigen.getItems().add(descripcion);
                cmbDestino.getItems().add(descripcion);
            }
            
            // Seleccionar el primer aeropuerto por defecto
            if (!cmbOrigen.getItems().isEmpty()) {
                cmbOrigen.getSelectionModel().selectFirst();
            }
            if (!cmbDestino.getItems().isEmpty()) {
                cmbDestino.getSelectionModel().selectFirst();
            }
            
            System.out.println(aeropuertos.size() + " aeropuertos cargados en ComboBox");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar aeropuertos: " + ex.getMessage());
            alert.show();
        }
    }
    
    /**
     * Extrae código de aeropuerto del texto descriptivo
     */
    private String extraerCodigoAeropuerto(String textoDescriptivo) {
        if (textoDescriptivo == null || textoDescriptivo.isEmpty()) {
            return null;
        }
        // El formato es: "CODIGO - Nombre (Ciudad, País)"
        // Extraemos solo el código (primeros 3-4 caracteres antes del guión)
        int indiceGuion = textoDescriptivo.indexOf(" - ");
        if (indiceGuion > 0) {
            return textoDescriptivo.substring(0, indiceGuion);
        }
        return textoDescriptivo; // Si no hay guión, devolvemos el texto completo
    }
}
