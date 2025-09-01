package edespol.redvuelos;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App - Sistema de Gestión de Vuelos AeroGraph
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Usar la interfaz ya implementada en la clase Mapa
        Mapa mapa = new Mapa();
        mapa.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}