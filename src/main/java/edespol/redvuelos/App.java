package edespol.redvuelos;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.ResultadoRuta;
import edespol.redvuelos.persistencia.EscritorCsv;
import edespol.redvuelos.persistencia.LectorCsv;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private final GrafoAeropuertos grafo = new GrafoAeropuertos();
    private final ObservableList<Aeropuerto> aeropuertosData = FXCollections.observableArrayList();
    private final ObservableList<Vuelo> vuelosData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // --- cargar datos desde CSV
        LectorCsv.cargarAeropuertos("data/airports.csv", grafo);
        LectorCsv.cargarVuelos("data/flights.csv", grafo);
        grafo.getVertices().forEach(v -> aeropuertosData.add(v.getDato()));

        // --- tabla de aeropuertos
        TableView<Aeropuerto> tablaAeropuertos = new TableView<>(aeropuertosData);
        TableColumn<Aeropuerto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCodigo()));
        TableColumn<Aeropuerto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        tablaAeropuertos.getColumns().addAll(colCodigo, colNombre);

        // --- tabla de vuelos
        TableView<Vuelo> tablaVuelos = new TableView<>(vuelosData);
        TableColumn<Vuelo, String> colId = new TableColumn<>("Vuelo");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        TableColumn<Vuelo, String> colDestino = new TableColumn<>("Destino");
        colDestino.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDestinoCodigo()));
        TableColumn<Vuelo, String> colDist = new TableColumn<>("DistanciaKm");
        colDist.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDistanciaKm())));
        tablaVuelos.getColumns().addAll(colId, colDestino, colDist);

        // --- evento: click en aeropuerto -> listar vuelos
        tablaAeropuertos.setOnMouseClicked(e -> {
            Aeropuerto a = tablaAeropuertos.getSelectionModel().getSelectedItem();
            if (a != null) {
                vuelosData.clear();
                vuelosData.addAll(grafo.obtenerSalientes(a.getCodigo()));
            }
        });

        // --- buscador de rutas
        TextField txtOrigen = new TextField("PKX");
        TextField txtDestino = new TextField("LHR");
        ChoiceBox<MetricaPeso> cbMetrica = new ChoiceBox<>(FXCollections.observableArrayList(MetricaPeso.values()));
        cbMetrica.setValue(MetricaPeso.DISTANCIA);
        Button btnBuscar = new Button("Ruta más corta");
        TextArea salidaRuta = new TextArea();

        btnBuscar.setOnAction(e -> {
            ResultadoRuta r = grafo.dijkstra(txtOrigen.getText().trim(), txtDestino.getText().trim(), cbMetrica.getValue());
            if (!r.isEncontrada()) {
                salidaRuta.setText("No existe ruta");
            } else {
                salidaRuta.setText("Ruta: " + r.getRutaAeropuertos() + "\nPeso: " + r.getPesoTotal());
            }
        });

        HBox buscador = new HBox(10, txtOrigen, txtDestino, cbMetrica, btnBuscar);

        // --- formulario para agregar vuelo
        TextField fId = new TextField();
        fId.setPromptText("Id");
        TextField fOrig = new TextField();
        fOrig.setPromptText("Origen");
        TextField fDest = new TextField();
        fDest.setPromptText("Destino");
        TextField fDist = new TextField();
        fDist.setPromptText("Distancia");
        Button btnAdd = new Button("Agregar vuelo");

        btnAdd.setOnAction(e -> {
            Vuelo v = new Vuelo();
            v.setId(fId.getText());
            v.setOrigenCodigo(fOrig.getText());
            v.setDestinoCodigo(fDest.getText());
            v.setDistanciaKm(Double.parseDouble(fDist.getText()));
            grafo.agregarVuelo(v);
            EscritorCsv.guardarVuelos("data/flights.csv", grafo);
            vuelosData.clear();
            vuelosData.addAll(grafo.obtenerSalientes(fOrig.getText()));
        });

        HBox formVuelo = new HBox(10, fId, fOrig, fDest, fDist, btnAdd);

        // --- layout principal
        BorderPane root = new BorderPane();
        root.setLeft(tablaAeropuertos);
        root.setCenter(tablaVuelos);

        VBox bottomBox = new VBox(10, buscador, salidaRuta, formVuelo);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        stage.setTitle("AeroGraph — Red de Vuelos");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
