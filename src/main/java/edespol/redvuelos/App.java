package edespol.redvuelos;

import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.ResultadoRuta;
import edespol.redvuelos.persistencia.LectorCsv;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private final GrafoAeropuertos grafo = new GrafoAeropuertos();

    @Override
    public void start(Stage stage) {
        // Carga de datos (ajusta rutas si usas otra carpeta)
        LectorCsv.cargarAeropuertos("data/airports.csv", grafo);
        LectorCsv.cargarVuelos("data/flights.csv", grafo);

        TextField txtOrigen = new TextField();
        txtOrigen.setPromptText("Código origen (p.ej., PKX)");

        TextField txtDestino = new TextField();
        txtDestino.setPromptText("Código destino (p.ej., LHR)");

        ChoiceBox<MetricaPeso> cbMetrica = new ChoiceBox<>();
        cbMetrica.getItems().addAll(MetricaPeso.DISTANCIA, MetricaPeso.DURACION, MetricaPeso.COSTO);
        cbMetrica.setValue(MetricaPeso.DISTANCIA);

        Button btnBuscar = new Button("Buscar ruta");

        TextArea salida = new TextArea();
        salida.setPrefRowCount(10);

        btnBuscar.setOnAction(e -> {
            String o = txtOrigen.getText().trim();
            String d = txtDestino.getText().trim();
            MetricaPeso m = cbMetrica.getValue();

            ResultadoRuta r = grafo.dijkstra(o, d, m);

            if (!r.isEncontrada()) {
                salida.setText("No existe ruta entre " + o + " y " + d);
                return;
            }

            salida.setText(
                "Ruta: " + r.getRutaAeropuertos() +
                "\nPeso total (" + m + "): " + r.getPesoTotal()
            );
        });

        HBox fila1 = new HBox(10, txtOrigen, txtDestino, cbMetrica, btnBuscar);
        VBox root = new VBox(10, fila1, salida);
        root.setPadding(new Insets(12));

        stage.setTitle("AeroGraph — Red de Vuelos");
        stage.setScene(new Scene(root, 680, 380));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
