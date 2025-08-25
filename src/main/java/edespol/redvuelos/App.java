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
import javafx.scene.control.Alert;
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
        Button btnBuscar = new Button("Ruta más optima");
        TextArea salidaRuta = new TextArea();
btnBuscar.setOnAction(e -> {
    String o = norm(txtOrigen.getText());
    String d = norm(txtDestino.getText());
    MetricaPeso m = cbMetrica.getValue();

    // Validación rápida
    if (o.isEmpty() || d.isEmpty()) {
        alert("Error", "Debes ingresar códigos de origen y destino.");
        return;
    }
    if (grafo.obtenerSalientes(o).isEmpty() && grafo.gradoEntrada(o) == 0) {
        alert("Error", "El aeropuerto de ORIGEN (" + o + ") no existe en el grafo.");
        return;
    }
    if (grafo.obtenerSalientes(d).isEmpty() && grafo.gradoEntrada(d) == 0) {
        alert("Error", "El aeropuerto de DESTINO (" + d + ") no existe en el grafo.");
        return;
    }

    ResultadoRuta r = grafo.dijkstra(o, d, m);

    if (!r.isEncontrada()) {
        salidaRuta.setText("No existe ruta entre " + o + " y " + d);
        return;
    }

    salidaRuta.setText(
        "Ruta: " + r.getRutaAeropuertos() +
        "\nPeso total (" + m + "): " + r.getPesoTotal()
    );
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
    String id = fId.getText();
    String o  = norm(fOrig.getText());
    String d  = norm(fDest.getText());
    String sDist = fDist.getText();

    // Validaciones básicas
    if (id == null || id.trim().isEmpty()) {
        alert("Error", "Ingresa un ID de vuelo (ej. F11).");
        return;
    }
    if (o.isEmpty() || d.isEmpty()) {
        alert("Error", "Ingresa códigos de ORIGEN y DESTINO.");
        return;
    }
    double dist;
    try {
        dist = Double.parseDouble(sDist);
        if (dist <= 0) {
            alert("Error", "La distancia debe ser positiva.");
            return;
        }
    } catch (Exception exNum) {
        alert("Error", "Distancia inválida. Ej: 9000");
        return;
    }

    // Verificar que los aeropuertos existan
    boolean origenExiste  = !(grafo.obtenerSalientes(o).isEmpty() && grafo.gradoEntrada(o) == 0);
    boolean destinoExiste = !(grafo.obtenerSalientes(d).isEmpty() && grafo.gradoEntrada(d) == 0);

    if (!origenExiste) {
        alert("Error", "El aeropuerto de ORIGEN (" + o + ") no existe en el grafo.");
        return;
    }
    if (!destinoExiste) {
        alert("Error", "El aeropuerto de DESTINO (" + d + ") no existe en el grafo.");
        return;
    }

    // Construir vuelo
    Vuelo v = new Vuelo();
    v.setId(id.trim());
    v.setOrigenCodigo(o);
    v.setDestinoCodigo(d);
    v.setDistanciaKm(dist);
    // Si quieres, setea también costo/duración por defecto:
    // v.setDuracionMin((int)(dist / 10)); // ejemplo tonto
    // v.setCostoUsd(dist / 20.0);

    boolean ok = grafo.agregarVuelo(v);

    if (!ok) {
        alert("Error", "No se pudo agregar el vuelo. Revisa que el ID no esté repetido y que los códigos existan.");
        return;
    }

    // Persistir
    try {
        EscritorCsv.guardarVuelos("data/flights.csv", grafo);
    } catch (Exception ex) {
        alert("Error al guardar", "No se pudo guardar en flights.csv: " + ex.getMessage());
        // opcional: revertir?
    }

    // Refrescar tabla de vuelos del ORIGEN actual
    vuelosData.clear();
    vuelosData.addAll(grafo.obtenerSalientes(o));

    alert("OK", "Vuelo agregado y guardado.\nReinicia la app para verificar la persistencia.");
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
    private String norm(String s) {
    return s == null ? "" : s.trim().toUpperCase();
}

private void alert(String titulo, String mensaje) {
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    a.setHeaderText(titulo);
    a.setContentText(mensaje);
    a.showAndWait();
}

}
