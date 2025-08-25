package edespol.redvuelos;
import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.MetricaPeso;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.ResultadoRuta;
import edespol.redvuelos.persistencia.EscritorCsv;
import edespol.redvuelos.persistencia.LectorCsv;
import edespol.redvuelos.ui.GrafoView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    // 1) Cargar datos
    LectorCsv.cargarAeropuertos("data/airports.csv", grafo);
    LectorCsv.cargarVuelos("data/flights.csv", grafo);
    grafo.getVertices().forEach(v -> aeropuertosData.add(v.getDato()));

    // 2) Vista del grafo
    GrafoView grafoView = new GrafoView();
    grafoView.setPrefSize(600, 400);
    grafoView.setGraph(grafo);
    grafoView.setLayoutMode(GrafoView.LayoutMode.CIRCULAR);
    grafoView.setOnAirportClick(code -> {
        vuelosData.clear();
        vuelosData.addAll(grafo.obtenerSalientes(code));
    });

    // 3) Tablas
    TableView<Aeropuerto> tablaAeropuertos = new TableView<>(aeropuertosData);
    TableColumn<Aeropuerto, String> colCodigo = new TableColumn<>("Código");
    colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCodigo()));
    TableColumn<Aeropuerto, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
    tablaAeropuertos.getColumns().addAll(colCodigo, colNombre);

    TableView<Vuelo> tablaVuelos = new TableView<>(vuelosData);
    TableColumn<Vuelo, String> colId = new TableColumn<>("Vuelo");
    colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
    TableColumn<Vuelo, String> colDestino = new TableColumn<>("Destino");
    colDestino.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDestinoCodigo()));
    TableColumn<Vuelo, String> colDist = new TableColumn<>("DistanciaKm");
    colDist.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getDistanciaKm())));
    tablaVuelos.getColumns().addAll(colId, colDestino, colDist);

    tablaAeropuertos.setOnMouseClicked(e -> {
        Aeropuerto a = tablaAeropuertos.getSelectionModel().getSelectedItem();
        if (a != null) {
            vuelosData.clear();
            vuelosData.addAll(grafo.obtenerSalientes(a.getCodigo()));
        }
    });

    // 4) Buscador de rutas
    TextField txtOrigen = new TextField("PKX");
    TextField txtDestino = new TextField("LHR");
    ChoiceBox<MetricaPeso> cbMetrica = new ChoiceBox<>(FXCollections.observableArrayList(MetricaPeso.values()));
    cbMetrica.setValue(MetricaPeso.DISTANCIA);
    Button btnBuscar = new Button("Ruta más óptima");
    TextArea salidaRuta = new TextArea();
    HBox buscador = new HBox(10, txtOrigen, txtDestino, cbMetrica, btnBuscar);

    // 5) Formulario agregar vuelo
    TextField fId = new TextField();     fId.setPromptText("Id");
    TextField fOrig = new TextField();   fOrig.setPromptText("Origen");
    TextField fDest = new TextField();   fDest.setPromptText("Destino");
    TextField fDist = new TextField();   fDist.setPromptText("Distancia");
    Button btnAdd = new Button("Agregar vuelo");
    HBox formVuelo = new HBox(10, fId, fOrig, fDest, fDist, btnAdd);

    // 6) Top bar (layout del grafo)
    ToggleGroup tg = new ToggleGroup();
    RadioButton rbCirc = new RadioButton("Circular"); rbCirc.setToggleGroup(tg); rbCirc.setSelected(true);
    RadioButton rbGeo  = new RadioButton("Geo (lat/lng)"); rbGeo.setToggleGroup(tg);
    rbCirc.setOnAction(e -> grafoView.setLayoutMode(GrafoView.LayoutMode.CIRCULAR));
    rbGeo.setOnAction(e -> grafoView.setLayoutMode(GrafoView.LayoutMode.GEO));
    HBox topBar = new HBox(10, rbCirc, rbGeo);
    topBar.setPadding(new Insets(8));

    // 7) Acciones
    btnBuscar.setOnAction(e -> {
        String o = norm(txtOrigen.getText());
        String d = norm(txtDestino.getText());
        MetricaPeso m = cbMetrica.getValue();

        if (o.isEmpty() || d.isEmpty()) { alert("Error", "Debes ingresar códigos de origen y destino."); return; }
        boolean origenExiste  = !(grafo.obtenerSalientes(o).isEmpty() && grafo.gradoEntrada(o) == 0);
        boolean destinoExiste = !(grafo.obtenerSalientes(d).isEmpty() && grafo.gradoEntrada(d) == 0);
        if (!origenExiste)  { alert("Error", "El aeropuerto de ORIGEN (" + o + ") no existe.");  return; }
        if (!destinoExiste) { alert("Error", "El aeropuerto de DESTINO (" + d + ") no existe."); return; }

        ResultadoRuta r = grafo.dijkstra(o, d, m);
        if (!r.isEncontrada()) {
            salidaRuta.setText("No existe ruta entre " + o + " y " + d);
            grafoView.highlightPath(null);
            return;
        }
        salidaRuta.setText("Ruta: " + r.getRutaAeropuertos() + "\nPeso total (" + m + "): " + r.getPesoTotal());
        grafoView.highlightPath(r.getRutaAeropuertos());
    });

    btnAdd.setOnAction(e -> {
        String id = fId.getText();
        String o  = norm(fOrig.getText());
        String d  = norm(fDest.getText());
        String sDist = fDist.getText();
        if (id == null || id.trim().isEmpty()) { alert("Error","Ingresa un ID de vuelo."); return; }
        if (o.isEmpty() || d.isEmpty()) { alert("Error","Ingresa códigos de ORIGEN y DESTINO."); return; }
        double dist;
        try {
            dist = Double.parseDouble(sDist);
            if (dist <= 0) { alert("Error","La distancia debe ser positiva."); return; }
        } catch (Exception exNum) { alert("Error","Distancia inválida."); return; }

        boolean origenExiste  = !(grafo.obtenerSalientes(o).isEmpty() && grafo.gradoEntrada(o) == 0);
        boolean destinoExiste = !(grafo.obtenerSalientes(d).isEmpty() && grafo.gradoEntrada(d) == 0);
        if (!origenExiste)  { alert("Error","El ORIGEN ("+o+") no existe.");  return; }
        if (!destinoExiste) { alert("Error","El DESTINO ("+d+") no existe."); return; }

        Vuelo v = new Vuelo();
        v.setId(id.trim()); v.setOrigenCodigo(o); v.setDestinoCodigo(d); v.setDistanciaKm(dist);
        boolean ok = grafo.agregarVuelo(v);
        if (!ok) { alert("Error","No se pudo agregar el vuelo. ID repetido o códigos inválidos."); return; }

        try { EscritorCsv.guardarVuelos("data/flights.csv", grafo); }
        catch (Exception ex) { alert("Error al guardar","No se pudo guardar: " + ex.getMessage()); }

        vuelosData.clear();
        vuelosData.addAll(grafo.obtenerSalientes(o));
        alert("OK","Vuelo agregado y guardado.");
    });

    // 8) Armar layout (¡un solo root!)
    BorderPane root = new BorderPane();
    root.setTop(topBar);                         // arriba: botones de layout
    root.setLeft(tablaAeropuertos);              // izquierda: aeropuertos
    root.setCenter(grafoView);                   // centro: grafo
    root.setRight(tablaVuelos);                  // derecha: vuelos del seleccionado
    VBox bottomBox = new VBox(10, buscador, salidaRuta, formVuelo);
    bottomBox.setPadding(new Insets(10));
    root.setBottom(bottomBox);                   // abajo: buscador + resultado + alta de vuelos

    // 9) Mostrar
    stage.setTitle("AeroGraph — Red de Vuelos");
    stage.setScene(new Scene(root, 1000, 650));
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
