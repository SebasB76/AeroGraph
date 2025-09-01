/*
 * AeroGraph - Sistema de Gestión de Vuelos Inteligente
 * Clase principal optimizada con separación de responsabilidades
 */
package edespol.redvuelos;

import java.io.IOException;
import java.util.List;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.ui.EstadisticasManager;
import edespol.redvuelos.ui.FormularioManager;
import edespol.redvuelos.ui.MapaManager;
import edespol.redvuelos.ui.MenuManager;
import edespol.redvuelos.ui.RutaManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación AeroGraph
 * Optimizada con separación de responsabilidades en managers especializados
 */
public class Mapa extends Application {

    // Managers especializados
    private MapaManager mapaManager;
    private MenuManager menuManager;
    private FormularioManager formularioManager;
    private RutaManager rutaManager;
    private EstadisticasManager estadisticasManager;

    @Override
    public void start(Stage stage) {
        try {
            // ✅ INICIAR SERVIDOR WEB PRIMERO
            WebServer.start();
            
            // ✅ INICIALIZAR FIREBASE
            FirebaseConfig.initFirebase();
            System.out.println("Firebase inicializado correctamente");

        } catch (IOException e) {
            System.err.println("Error al inicializar Firebase: " + e.getMessage());
            mostrarErrorFirebase(stage);
            return;
        }

        // ✅ INICIALIZAR MANAGERS ESPECIALIZADOS
        inicializarManagers(stage);

        // ✅ CONFIGURAR INTERFAZ PRINCIPAL
        configurarInterfazPrincipal(stage);

        // ✅ CONFIGURAR EVENTOS DE BOTONES
        configurarEventosBotones();

        // ✅ MOSTRAR VENTANA
        stage.show();

        // ✅ CARGAR MAPA INICIAL
        cargarMapaInicial();
    }

    /**
     * Inicializa todos los managers especializados
     */
    private void inicializarManagers(Stage stage) {
        mapaManager = new MapaManager();
        menuManager = new MenuManager(stage);
        formularioManager = new FormularioManager(stage, this::cargarMapaInicial);
        rutaManager = new RutaManager(stage);
        estadisticasManager = new EstadisticasManager(stage);
    }

    /**
     * Configura la interfaz principal de la aplicación
     */
    private void configurarInterfazPrincipal(Stage stage) {
        // Layout principal
        HBox root = new HBox();
        root.getChildren().addAll(
            menuManager.getMenuContainer(), 
            mapaManager.getView()
        );
        HBox.setHgrow(mapaManager.getView(), Priority.ALWAYS);

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        stage.setTitle("AeroGraph - Sistema de Gestión de Vuelos Inteligente");
        stage.setScene(scene);
        
        // ✅ CERRAR ELEGANTEMENTE AL SALIR
        stage.setOnCloseRequest(event -> {
            WebServer.stop();
            if (mapaManager != null) {
                mapaManager.cerrar();
            }
        });
    }

    /**
     * Configura todos los eventos de los botones del menú
     */
    private void configurarEventosBotones() {
        // Eventos de botones - Aeropuertos
        menuManager.getBtnAgregarAeropuerto().setOnAction(e -> 
            formularioManager.mostrarFormularioAgregarAeropuerto());
        menuManager.getBtnEditarAeropuerto().setOnAction(e -> 
            formularioManager.mostrarFormularioEditarAeropuerto());
        menuManager.getBtnEliminarAeropuerto().setOnAction(e -> 
            formularioManager.mostrarFormularioEliminarAeropuerto());

        // Eventos de botones - Vuelos
        menuManager.getBtnAgregarVuelo().setOnAction(e -> 
            formularioManager.mostrarFormularioAgregarVuelo());
        menuManager.getBtnEditarVuelo().setOnAction(e -> 
            formularioManager.mostrarFormularioEditarVueloid2());
        menuManager.getBtnEliminarVuelo().setOnAction(e -> 
            formularioManager.mostrarFormularioEliminarVuelo());
        menuManager.getBtnListarVuelos().setOnAction(e -> 
            formularioManager.mostrarListaVuelos());

        // Eventos de botones - Búsqueda de rutas
        menuManager.getBtnBuscarRuta().setOnAction(e -> 
            rutaManager.mostrarFormularioBuscarRutaOptima());
        menuManager.getBtnBuscarRutaBFS().setOnAction(e -> 
            rutaManager.mostrarFormularioBuscarRutaBFS());
        menuManager.getBtnBuscarRutaDFS().setOnAction(e -> 
            rutaManager.mostrarFormularioBuscarRutaDFS());
        
        // Eventos de botones - Estadísticas
        menuManager.getBtnEstadisticas().setOnAction(e -> 
            estadisticasManager.mostrarEstadisticas());
        menuManager.getBtnDemo().setOnAction(e -> 
            estadisticasManager.ejecutarDemo());

        // Botón de recarga del mapa
        menuManager.getBtnRecargarMapa().setOnAction(e -> 
            mapaManager.recargarMapa());
    }

    /**
     * Carga el mapa inicial con datos de aeropuertos y vuelos
     */
    private void cargarMapaInicial() {
        try {
            List<Aeropuerto> aeropuertos = Persistencia.cargarAeropuertos();
            List<Vuelo> vuelos = Persistencia.cargarVuelos();
            
            mapaManager.cargarMapa(aeropuertos, vuelos);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "Error al cargar el mapa: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Muestra error de Firebase en una ventana separada
     */
    private void mostrarErrorFirebase(Stage stage) {
        VBox errorBox = new VBox(10);
        errorBox.setPadding(new javafx.geometry.Insets(20));
        errorBox.setAlignment(Pos.CENTER);
        
        Label errorLabel = new Label("Error al conectar con la base de datos Firebase");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        
        TextArea errorDetails = new TextArea();
        errorDetails.setEditable(false);
        errorDetails.setPrefHeight(100);
        
        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> stage.close());
        
        errorBox.getChildren().addAll(errorLabel, errorDetails, btnSalir);
        
        Scene errorScene = new Scene(errorBox, 400, 200);
        stage.setScene(errorScene);
        stage.show();
    }

    /**
     * Muestra una alerta moderna
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        // Aplicar estilos modernos al diálogo
        javafx.scene.control.DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif;");
        
        alert.showAndWait();
    }

    /**
     * Método principal de la aplicación
     */
    public static void main(String[] args) {
        // Primero probar la conexión a Firebase
        try {
            System.out.println("🔹 Intentando conectar con Firebase...");
            FirebaseConfig.initFirebase();
            System.out.println("✅ Firebase conectado exitosamente");
            
            // Ahora lanzar la aplicación JavaFX
            launch(args);
            
        } catch (IOException e) {
            System.err.println("❌ Error crítico: No se pudo conectar con Firebase");
            System.err.println("Mensaje: " + e.getMessage());
            
            // Mostrar ventana de error
            Platform.runLater(() -> {
                Stage errorStage = new Stage();
                VBox errorBox = new VBox(10);
                errorBox.setPadding(new javafx.geometry.Insets(20));
                errorBox.setAlignment(Pos.CENTER);
                
                Label title = new Label("Error de Conexión");
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
                
                TextArea errorDetails = new TextArea();
                errorDetails.setText("No se pudo conectar con la base de datos Firebase.\n\n" +
                                   "Causa: " + e.getMessage() + "\n\n" +
                                   "Verifique:\n" +
                                   "1. El archivo serviceAccountKey.json existe\n" +
                                   "2. La configuración de Firebase es correcta\n" +
                                   "3. Tiene conexión a internet");
                errorDetails.setEditable(false);
                errorDetails.setPrefSize(400, 200);
                
                Button btnExit = new Button("Salir");
                btnExit.setOnAction(event -> System.exit(1));
                
                errorBox.getChildren().addAll(title, errorDetails, btnExit);
                errorStage.setScene(new Scene(errorBox));
                errorStage.setTitle("Error de Conexión");
                errorStage.show();
            });
        }
    }
}