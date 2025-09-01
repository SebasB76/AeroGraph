package edespol.redvuelos.ui;

import java.util.List;

import edespol.redvuelos.Persistencia;
import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Manager para gestionar todos los formularios de la aplicación
 */
public class FormularioManager {
    
    private Stage ownerStage;
    private Runnable onMapaUpdate;
    
    public FormularioManager(Stage ownerStage, Runnable onMapaUpdate) {
        this.ownerStage = ownerStage;
        this.onMapaUpdate = onMapaUpdate;
    }
    
    /**
     * Muestra el formulario para agregar aeropuerto
     */
public void mostrarFormularioAgregarAeropuerto() {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(ownerStage);
    dialog.setTitle("Agregar Nuevo Aeropuerto");
    dialog.initStyle(StageStyle.UTILITY);

    VBox vbox = new VBox(20);
    vbox.setPadding(new Insets(24));
    vbox.setStyle("-fx-background-color: #ffffff;");

    // Header del formulario
    Label headerLabel = new Label("Información del Aeropuerto");
    headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #1976d2; -fx-padding: 0 0 16 0;");

    // Campos del formulario
    VBox camposContainer = new VBox(16);

    TextField txtCodigo = UIComponentFactory.crearCampoTexto("Código IATA*", "Ej: GYE, UIO, MAD", true);
    TextField txtNombre = UIComponentFactory.crearCampoTexto("Nombre del Aeropuerto*", "Ej: Aeropuerto Internacional...", true);
    TextField txtCiudad = UIComponentFactory.crearCampoTexto("Ciudad", "Ej: Guayaquil", false);
    TextField txtPais   = UIComponentFactory.crearCampoTexto("País", "Ej: Ecuador", false);
    TextField txtLat    = UIComponentFactory.crearCampoTexto("Latitud*", "Ej: -2.1574", true);
    TextField txtLon    = UIComponentFactory.crearCampoTexto("Longitud*", "Ej: -79.8835", true);

    camposContainer.getChildren().addAll(
        new VBox(4, new Label("Código IATA*"), txtCodigo),
        new VBox(4, new Label("Nombre del Aeropuerto*"), txtNombre),
        new VBox(4, new Label("Ciudad"), txtCiudad),
        new VBox(4, new Label("País"), txtPais),
        new VBox(4, new Label("Latitud*"), txtLat),
        new VBox(4, new Label("Longitud*"), txtLon)
    );

    // Botones de acción
    HBox botonesContainer = new HBox(12);
    botonesContainer.setAlignment(Pos.CENTER_RIGHT);

    Button btnCancelar = UIComponentFactory.crearBotonSecundario("Cancelar");
    Button btnGuardar = UIComponentFactory.crearBotonPrimario("Guardar Aeropuerto");

    btnCancelar.setOnAction(e -> dialog.close());

    botonesContainer.getChildren().addAll(btnCancelar, btnGuardar);

    // Agregar todos los elementos al formulario
    vbox.getChildren().addAll(headerLabel, camposContainer, botonesContainer);

    btnGuardar.setOnAction(e -> {
        try {
            if(txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() ||
               txtLat.getText().isEmpty() || txtLon.getText().isEmpty()) {
                mostrarAlerta("Advertencia", "Complete los campos obligatorios (*)", Alert.AlertType.WARNING);
                return;
            }

            // Verificar si el código ya existe
            if (edespol.redvuelos.Persistencia.aeropuertoExiste(txtCodigo.getText().trim())) {
                mostrarAlerta("Advertencia", "Ya existe un aeropuerto con este código", Alert.AlertType.WARNING);
                return;
            }

            Aeropuerto a = new Aeropuerto();
            a.setCodigo(txtCodigo.getText().trim().toUpperCase());
            a.setNombre(txtNombre.getText().trim());
            a.setCiudad(txtCiudad.getText().trim());
            a.setPais(txtPais.getText().trim());
            a.setLatitud(Double.parseDouble(txtLat.getText().trim()));
            a.setLongitud(Double.parseDouble(txtLon.getText().trim()));

            edespol.redvuelos.Persistencia.guardarAeropuerto(a);
            dialog.close();
            onMapaUpdate.run(); // ACTUALIZAR MAPA
            mostrarAlerta("Éxito", "Aeropuerto agregado correctamente al sistema", Alert.AlertType.INFORMATION);

        } catch(NumberFormatException ex) {
            mostrarAlerta("Error", "Latitud y Longitud deben ser números válidos", Alert.AlertType.ERROR);
        } catch(Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    });

    dialog.setScene(new Scene(vbox, 500, 650));
    dialog.showAndWait();
}

    /**
     * Muestra el formulario para editar aeropuerto
     */
    public void mostrarFormularioEditarAeropuerto() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Editar Aeropuerto");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField txtCodigo = new TextField();
        Button btnBuscar = new Button("Buscar");

        TextField txtNombre = new TextField();
        TextField txtCiudad = new TextField();
        TextField txtPais = new TextField();
        TextField txtLat = new TextField();
        TextField txtLon = new TextField();
        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setDisable(true);

        vbox.getChildren().addAll(
                new Label("Código del aeropuerto a editar*:"), txtCodigo,
                btnBuscar,
                new Label("Nombre*:"), txtNombre,
                new Label("Ciudad:"), txtCiudad,
                new Label("País:"), txtPais,
                new Label("Latitud*:"), txtLat,
                new Label("Longitud*:"), txtLon,
                btnGuardar
        );

        btnBuscar.setOnAction(e -> {
            try {
                String codigo = txtCodigo.getText().trim().toUpperCase();
                if(codigo.isEmpty()) { 
                    new Alert(Alert.AlertType.WARNING, "Ingrese un código").show(); 
                    return; 
                }

                Aeropuerto a = edespol.redvuelos.Persistencia.buscarAeropuertoPorCodigo(codigo);

                if(a == null) { 
                    new Alert(Alert.AlertType.ERROR, "Aeropuerto no encontrado").show(); 
                    return; 
                }

                txtNombre.setText(a.getNombre());
                txtCiudad.setText(a.getCiudad());
                txtPais.setText(a.getPais());
                txtLat.setText(String.valueOf(a.getLatitud()));
                txtLon.setText(String.valueOf(a.getLongitud()));
                btnGuardar.setDisable(false);

            } catch(Exception ex) { 
                ex.printStackTrace(); 
                new Alert(Alert.AlertType.ERROR, "Error: "+ex.getMessage()).show(); 
            }
        });

        btnGuardar.setOnAction(e -> {
            try {
                if(txtNombre.getText().isEmpty() || txtLat.getText().isEmpty() || txtLon.getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Complete los campos obligatorios").show();
                    return;
                }
                
                Aeropuerto a = new Aeropuerto();
                a.setCodigo(txtCodigo.getText().trim().toUpperCase());
                a.setNombre(txtNombre.getText().trim());
                a.setCiudad(txtCiudad.getText().trim());
                a.setPais(txtPais.getText().trim());
                a.setLatitud(Double.parseDouble(txtLat.getText().trim()));
                a.setLongitud(Double.parseDouble(txtLon.getText().trim()));

                edespol.redvuelos.Persistencia.guardarAeropuerto(a);
                dialog.close();
                onMapaUpdate.run(); // ACTUALIZAR MAPA
                new Alert(Alert.AlertType.INFORMATION,"Aeropuerto actualizado correctamente").show();

            } catch(NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Latitud y Longitud deben ser números válidos").show();
            } catch(Exception ex) { 
                ex.printStackTrace(); 
                new Alert(Alert.AlertType.ERROR, "Error: "+ex.getMessage()).show(); 
            }
        });

        dialog.setScene(new Scene(vbox, 350, 450));
        dialog.showAndWait();
    }
    
    /**
     * Muestra el formulario para eliminar aeropuerto
     */
    public void mostrarFormularioEliminarAeropuerto() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Eliminar Aeropuerto");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField txtCodigo = new TextField();
        Button btnBuscar = new Button("Buscar");
        Label lblInfo = new Label();
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setDisable(true);

        vbox.getChildren().addAll(
                new Label("Código del aeropuerto:"), txtCodigo,
                btnBuscar, lblInfo, btnEliminar
        );

        btnBuscar.setOnAction(e -> {
            try {
                String codigo = txtCodigo.getText().trim().toUpperCase();
                if(codigo.isEmpty()) { 
                    new Alert(Alert.AlertType.WARNING, "Ingrese código").show(); 
                    return; 
                }
                
                Aeropuerto a = edespol.redvuelos.Persistencia.buscarAeropuertoPorCodigo(codigo);
                if(a == null) {
                    lblInfo.setText("Aeropuerto no encontrado");
                    lblInfo.setStyle("-fx-text-fill: red;");
                    btnEliminar.setDisable(true);
                } else {
                    lblInfo.setText("Encontrado: " + a.getNombre() + " - " + a.getCiudad() + ", " + a.getPais());
                    lblInfo.setStyle("-fx-text-fill: green;");
                    btnEliminar.setDisable(false);
                }
            } catch(Exception ex){ 
                ex.printStackTrace(); 
                new Alert(Alert.AlertType.ERROR,"Error: "+ex.getMessage()).show(); 
            }
        });

        btnEliminar.setOnAction(e -> {
            try {
                String codigo = txtCodigo.getText().trim().toUpperCase();
                
                // Confirmación antes de eliminar
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, 
                    "¿Está seguro de eliminar el aeropuerto " + codigo + "?");
                confirmacion.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            edespol.redvuelos.Persistencia.eliminarAeropuerto(codigo);
                            dialog.close();
                            onMapaUpdate.run(); // ACTUALIZAR MAPA
                            new Alert(Alert.AlertType.INFORMATION, "Aeropuerto eliminado correctamente").show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR,"Error al eliminar: "+ex.getMessage()).show();
                        }
                    }
                });
            } catch(Exception ex){ 
                ex.printStackTrace(); 
                new Alert(Alert.AlertType.ERROR,"Error: "+ex.getMessage()).show(); 
            }
        });

        dialog.setScene(new Scene(vbox, 350, 200));
        dialog.showAndWait();
    }
    
    /**
     * Muestra el formulario para agregar vuelo
     */
        public void mostrarFormularioAgregarVuelo() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Agregar Vuelo");
        dialog.initStyle(StageStyle.UTILITY);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(24));
        vbox.setStyle("-fx-background-color: #ffffff;");

        // Header
        Label headerLabel = new Label("Información del Vuelo");
        headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
                            "-fx-font-size: 18px; -fx-font-weight: 600; " +
                            "-fx-text-fill: #1976d2; -fx-padding: 0 0 16 0;");

        // Contenedor de campos con scroll
        VBox camposContainer = new VBox(16);
    camposContainer.setPadding(new Insets(24)); // <- aquí agregamos padding dentro del scroll
    camposContainer.setStyle("-fx-background-color: #ffffff;");

        TextField txtOrigen = UIComponentFactory.crearCampoTexto("Código Origen*", "Ej: UIO, GYE", true);
        TextField txtDestino = UIComponentFactory.crearCampoTexto("Código Destino*", "Ej: MAD, LAX", true);
        TextField txtAerolinea = UIComponentFactory.crearCampoTexto("Código Aerolínea", "Ej: AV, AA", false);
        TextField txtDistancia = UIComponentFactory.crearCampoTexto("Distancia (km)", "Ej: 1200", false);
        TextField txtDuracion = UIComponentFactory.crearCampoTexto("Duración (min)", "Ej: 180", false);
        TextField txtCosto = UIComponentFactory.crearCampoTexto("Costo (USD)", "Ej: 250", false);
        TextField txtHoraSalida = UIComponentFactory.crearCampoTexto("Hora Salida (HH:MM)", "Ej: 14:30", false);

        camposContainer.getChildren().addAll(
            new VBox(4, new Label("Código Origen*"), txtOrigen),
            new VBox(4, new Label("Código Destino*"), txtDestino),
            new VBox(4, new Label("Código Aerolínea"), txtAerolinea),
            new VBox(4, new Label("Distancia (km)"), txtDistancia),
            new VBox(4, new Label("Duración (min)"), txtDuracion),
            new VBox(4, new Label("Costo (USD)"), txtCosto),
            new VBox(4, new Label("Hora Salida (HH:MM)"), txtHoraSalida)
        );

        ScrollPane scrollPane = new ScrollPane(camposContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400); // Altura visible del scroll

        // Botones de acción
        HBox botonesContainer = new HBox(12);
        botonesContainer.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancelar = UIComponentFactory.crearBotonSecundario("Cancelar");
        Button btnGuardar = UIComponentFactory.crearBotonPrimario("Agregar Vuelo");

        btnCancelar.setOnAction(e -> dialog.close());
        botonesContainer.getChildren().addAll(btnCancelar, btnGuardar);

        vbox.getChildren().addAll(headerLabel, scrollPane, botonesContainer);

        btnGuardar.setOnAction(e -> {
            try {
                if(txtOrigen.getText().isEmpty() || txtDestino.getText().isEmpty()) {
                    mostrarAlerta("Advertencia", "Complete códigos de origen y destino", Alert.AlertType.WARNING);
                    return;
                }

                if (!edespol.redvuelos.Persistencia.aeropuertoExiste(txtOrigen.getText().trim().toUpperCase())) {
                    mostrarAlerta("Error", "El aeropuerto de origen no existe", Alert.AlertType.ERROR);
                    return;
                }
                if (!edespol.redvuelos.Persistencia.aeropuertoExiste(txtDestino.getText().trim().toUpperCase())) {
                    mostrarAlerta("Error", "El aeropuerto de destino no existe", Alert.AlertType.ERROR);
                    return;
                }

                Vuelo v = new Vuelo();
                v.setOrigenCodigo(txtOrigen.getText().trim().toUpperCase());
                v.setDestinoCodigo(txtDestino.getText().trim().toUpperCase());
                v.setAerolineaCodigo(txtAerolinea.getText().trim());
                v.setDistanciaKm(txtDistancia.getText().isEmpty() ? 0 : Double.parseDouble(txtDistancia.getText().trim()));
                v.setDuracionMin(txtDuracion.getText().isEmpty() ? 0 : Integer.parseInt(txtDuracion.getText().trim()));
                v.setCostoUsd(txtCosto.getText().isEmpty() ? 0 : Double.parseDouble(txtCosto.getText().trim()));
                v.setHoraSalida(txtHoraSalida.getText().trim());

                edespol.redvuelos.Persistencia.guardarVuelo(v);
                dialog.close();
                onMapaUpdate.run(); // Actualizar mapa
                mostrarAlerta("Éxito", "Vuelo agregado correctamente", Alert.AlertType.INFORMATION);

            } catch(NumberFormatException ex) {
                mostrarAlerta("Error", "Distancia, duración y costo deben ser números válidos", Alert.AlertType.ERROR);
            } catch(Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        dialog.setScene(new Scene(vbox, 500, 600));
        dialog.showAndWait();
    }


    /**
     * Muestra el formulario para eliminar vuelo
     */
    public void mostrarFormularioEliminarVuelo() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Eliminar Vuelo");
        dialog.initStyle(StageStyle.UTILITY);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(54));
        vbox.setStyle("-fx-background-color: #ffffff;");

        // Header del formulario
        Label headerLabel = new Label("Eliminar Vuelo del Sistema");
        headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #d32f2f; -fx-padding: 0 0 16 0;");

        // Campo para buscar vuelo por ID
        VBox campoContainer = new VBox(8);
        campoContainer.setPadding(new Insets(24));
        Label labelCampo = new Label("ID del Vuelo a eliminar:");
        labelCampo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #424242;");
        
        TextField txtIdVuelo = new TextField();
        txtIdVuelo.setPromptText("Ingrese el ID del vuelo");
        txtIdVuelo.setPadding(new Insets(12.0, 16.0, 12.0, 16.0));
        txtIdVuelo.setStyle(
            "-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #424242; " +
            "-fx-background-color: #fafafa; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-prompt-text-fill: #9e9e9e;"
        );

        campoContainer.getChildren().addAll(labelCampo, txtIdVuelo);

        // Botones de acción
        HBox botonesContainer = new HBox(12);
        botonesContainer.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnBuscar = UIComponentFactory.crearBotonPrimario("Buscar Vuelo");
        Button btnEliminar = UIComponentFactory.crearBotonPrimario("Eliminar Vuelo");
        btnEliminar.setStyle(btnEliminar.getStyle().replace("#1976d2", "#d32f2f").replace("#1565c0", "#c62828").replace("#0d47a1", "#b71c1c"));
        btnEliminar.setDisable(true);
        
        botonesContainer.getChildren().addAll(btnBuscar, btnEliminar);

        // Información del vuelo
        Label lblInfo = new Label();
        lblInfo.setWrapText(true);
        lblInfo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-padding: 16 0;");

        vbox.getChildren().addAll(headerLabel, campoContainer, lblInfo, botonesContainer);

        // Eventos
        btnBuscar.setOnAction(e -> {
            try {
                String idVuelo = txtIdVuelo.getText().trim();
                if(idVuelo.isEmpty()) { 
                    mostrarAlerta("Advertencia", "Por favor ingrese el ID del vuelo", Alert.AlertType.WARNING);
                    return; 
                }

                Vuelo v = edespol.redvuelos.Persistencia.buscarVueloPorId(idVuelo);
                if(v == null) {
                    lblInfo.setText("Vuelo no encontrado");
                    lblInfo.setStyle(lblInfo.getStyle() + " -fx-text-fill: #d32f2f;");
                    btnEliminar.setDisable(true);
                } else {
                    lblInfo.setText("Vuelo encontrado:\n" + 
                                                   "Origen: " + v.getOrigenCodigo() + "\n" +
                "Destino: " + v.getDestinoCodigo() + "\n" +
                "Aerolínea: " + (v.getAerolineaCodigo() != null ? v.getAerolineaCodigo() : "N/A") + "\n" +
                "Hora: " + (v.getHoraSalida() != null ? v.getHoraSalida() : "N/A"));
                    lblInfo.setStyle(lblInfo.getStyle() + " -fx-text-fill: #388e3c;");
                    btnEliminar.setDisable(false);
                }
            } catch(Exception ex){ 
                ex.printStackTrace(); 
                mostrarAlerta("Error", "Error al buscar vuelo: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnEliminar.setOnAction(e -> {
            try {
                String idVuelo = txtIdVuelo.getText().trim();
                
                // Confirmación antes de eliminar
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, 
                    "¿Está seguro de eliminar este vuelo? Esta acción no se puede deshacer.");
                confirmacion.setTitle("Confirmar Eliminación");
                confirmacion.setHeaderText("Eliminar Vuelo");
                confirmacion.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            edespol.redvuelos.Persistencia.eliminarVuelo(idVuelo);
                            dialog.close();
                            onMapaUpdate.run(); // ACTUALIZAR MAPA
                            mostrarAlerta("Éxito", "Vuelo eliminado correctamente del sistema", Alert.AlertType.INFORMATION);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            mostrarAlerta("Error", "Error al eliminar vuelo: " + ex.getMessage(), Alert.AlertType.ERROR);
                        }
                    }
                });
            } catch(Exception ex){ 
                ex.printStackTrace(); 
                mostrarAlerta("Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        dialog.setScene(new Scene(vbox, 450, 350));
        dialog.showAndWait();
    }
    
    /**
     * Muestra la lista de vuelos disponibles
     */
    public void mostrarListaVuelos() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Lista de Vuelos Disponibles");
        dialog.initStyle(StageStyle.UTILITY);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(24));
        vbox.setStyle("-fx-background-color: #ffffff;");

        // Header
        Label headerLabel = new Label("Vuelos Registrados en el Sistema");
        headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #1976d2; -fx-padding: 0 0 16 0;");

        // Contenedor de vuelos con scroll
        ScrollPane scrollPane = new ScrollPane();
        VBox vuelosContainer = new VBox(12);
        vuelosContainer.setPadding(new Insets(16));

        try {
            List<Vuelo> vuelos = edespol.redvuelos.Persistencia.cargarVuelos();
            
            if (vuelos.isEmpty()) {
                Label lblNoVuelos = new Label("No hay vuelos registrados en el sistema");
                lblNoVuelos.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 16px; -fx-text-fill: #9e9e9e; -fx-padding: 32; -fx-alignment: center;");
                vuelosContainer.getChildren().add(lblNoVuelos);
            } else {
                Label lblTitulo = new Label("Total de vuelos: " + vuelos.size());
                lblTitulo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #666666; -fx-padding: 0 0 16 0;");
                vuelosContainer.getChildren().add(lblTitulo);

                for (Vuelo vuelo : vuelos) {
                    VBox vueloCard = crearTarjetaVuelo(vuelo, dialog);
                    vuelosContainer.getChildren().add(vueloCard);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
                            Label lblError = new Label("Error al cargar vuelos: " + ex.getMessage());
            lblError.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-text-fill: #d32f2f; -fx-padding: 16;");
            vuelosContainer.getChildren().add(lblError);
        }

        scrollPane.setContent(vuelosContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        // Botón cerrar
        Button btnCerrar = UIComponentFactory.crearBotonSecundario("Cerrar");
        btnCerrar.setOnAction(e -> dialog.close());

        vbox.getChildren().addAll(headerLabel, scrollPane, btnCerrar);

        dialog.setScene(new Scene(vbox, 700, 600));
        dialog.showAndWait();
    }
    
    /**
     * Crea una tarjeta de vuelo moderna
     */
    private VBox crearTarjetaVuelo(Vuelo vuelo, Stage dialog) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: #fafafa; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-background-radius: 12px;"
        );

        // Información del vuelo
                    Label lblVuelo = new Label(String.format("Vuelo %s: %s → %s", 
            vuelo.getId(), 
            vuelo.getOrigenCodigo(), 
            vuelo.getDestinoCodigo()));
        lblVuelo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #1976d2;");

                    Label lblDetalles = new Label(String.format("Aerolínea: %s | Hora: %s", 
            vuelo.getAerolineaCodigo() != null ? vuelo.getAerolineaCodigo() : "N/A",
            vuelo.getHoraSalida() != null ? vuelo.getHoraSalida() : "N/A"));
        lblDetalles.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-text-fill: #666666;");

        // Botones de acción
        HBox botonesContainer = new HBox(8);
        botonesContainer.setAlignment(Pos.CENTER_RIGHT);
        
                            Button btnEditar = UIComponentFactory.crearBotonSecundario("Editar");
                    Button btnEliminar = UIComponentFactory.crearBotonSecundario("Eliminar");
        btnEliminar.setStyle(btnEliminar.getStyle().replace("#1976d2", "#d32f2f").replace("#e3f2fd", "#ffebee"));
        
        btnEditar.setOnAction(e -> {
            dialog.close();
            mostrarFormularioEditarVueloConId(vuelo.getId());
        });
        
        btnEliminar.setOnAction(e -> {
            dialog.close();
            mostrarFormularioEliminarVueloConId(vuelo.getId());
        });
        
        botonesContainer.getChildren().addAll(btnEditar, btnEliminar);

        card.getChildren().addAll(lblVuelo, lblDetalles, botonesContainer);
        return card;
    }
    /**
     * Muestra el formulario para editar vuelo con ID predefinido
     */
// ------------------------
// Mostrar formulario de edición de un vuelo por ID
// ------------------------
public void mostrarFormularioEditarVueloConId(String idVuelo) {
    Task<Vuelo> task = new Task<>() {
        @Override
        protected Vuelo call() throws Exception {
            return Persistencia.buscarVueloPorId(idVuelo);
        }
    };

    task.setOnSucceeded(e -> {
        Vuelo vuelo = task.getValue();
        if (vuelo == null) {
            mostrarAlerta("Error", "No se encontró el vuelo con ID: " + idVuelo, Alert.AlertType.ERROR);
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Editar Vuelo");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Campos del vuelo
        TextField txtId = new TextField(vuelo.getId());
        txtId.setDisable(true);

        TextField txtOrigen = new TextField(vuelo.getOrigenCodigo());
        TextField txtDestino = new TextField(vuelo.getDestinoCodigo());
        TextField txtHora = new TextField(vuelo.getHoraSalida());
        TextField txtDuracion = new TextField(String.valueOf(vuelo.getDuracionMin()));
        TextField txtPrecio = new TextField(String.valueOf(vuelo.getCostoUsd()));

        Button btnGuardar = new Button("Guardar Cambios");

        vbox.getChildren().addAll(
                new Label("ID (no editable):"), txtId,
                new Label("Código Origen:"), txtOrigen,
                new Label("Código Destino:"), txtDestino,
                new Label("Hora de salida:"), txtHora,
                new Label("Duración (min):"), txtDuracion,
                new Label("Precio:"), txtPrecio,
                btnGuardar
        );

        btnGuardar.setOnAction(ev -> {
            try {
                if (txtOrigen.getText().isEmpty() || txtDestino.getText().isEmpty() || txtHora.getText().isEmpty()) {
                    mostrarAlerta("Advertencia", "Complete los campos obligatorios", Alert.AlertType.WARNING);
                    return;
                }

                vuelo.setOrigenCodigo(txtOrigen.getText().trim().toUpperCase());
                vuelo.setDestinoCodigo(txtDestino.getText().trim().toUpperCase());
                vuelo.setHoraSalida(txtHora.getText().trim());
                vuelo.setDuracionMin(Integer.parseInt(txtDuracion.getText().trim()));
                vuelo.setCostoUsd(Double.parseDouble(txtPrecio.getText().trim()));

                Persistencia.guardarVuelo(vuelo);

                dialog.close();
                onMapaUpdate.run();
                mostrarAlerta("Éxito", "Vuelo actualizado correctamente", Alert.AlertType.INFORMATION);

            } catch (NumberFormatException exNum) {
                mostrarAlerta("Error", "Duración y Precio deben ser numéricos", Alert.AlertType.ERROR);
            } catch (Exception exGen) {
                exGen.printStackTrace();
                mostrarAlerta("Error", "No se pudo guardar el vuelo: " + exGen.getMessage(), Alert.AlertType.ERROR);
            }
        });

        dialog.setScene(new Scene(vbox, 400, 450));
        dialog.showAndWait();
    });

    task.setOnFailed(e -> {
        mostrarAlerta("Error", "No se pudo cargar el vuelo: " + task.getException().getMessage(), Alert.AlertType.ERROR);
    });

    new Thread(task).start();
}


// ------------------------
// Mostrar formulario para buscar vuelos por aeropuerto
// ------------------------
public void mostrarFormularioEditarVueloid2() {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(ownerStage);
    dialog.setTitle("Buscar Vuelos por Aeropuerto");

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(10));

    TextField txtCodigoAeropuerto = new TextField();
    Button btnBuscar = new Button("Buscar vuelos");

    vbox.getChildren().addAll(new Label("Código del aeropuerto*:"), txtCodigoAeropuerto, btnBuscar);

    btnBuscar.setOnAction(e -> {
        String codigo = txtCodigoAeropuerto.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAlerta("Advertencia", "Ingrese un código de aeropuerto", Alert.AlertType.WARNING);
            return;
        }

        Task<List<Vuelo>> task = new Task<>() {
            @Override
            protected List<Vuelo> call() throws Exception {
                List<Vuelo> vuelos = Persistencia.cargarVuelos();
                return vuelos.stream()
                        .filter(v -> v.getOrigenCodigo().equalsIgnoreCase(codigo) ||
                                     v.getDestinoCodigo().equalsIgnoreCase(codigo))
                        .toList();
            }
        };

        task.setOnSucceeded(ev -> {
            List<Vuelo> vuelosFiltrados = task.getValue();
            if (vuelosFiltrados.isEmpty()) {
                mostrarAlerta("Sin resultados", "No se encontraron vuelos para el aeropuerto: " + codigo, Alert.AlertType.INFORMATION);
                return;
            }

            dialog.close();
            mostrarListaVuelosParaEditar(vuelosFiltrados);
        });

        task.setOnFailed(ev -> {
            mostrarAlerta("Error", "No se pudo cargar la lista de vuelos: " + task.getException().getMessage(), Alert.AlertType.ERROR);
        });

        new Thread(task).start();
    });

    dialog.setScene(new Scene(vbox, 350, 150));
    dialog.showAndWait();
}


// ------------------------
// Mostrar lista de vuelos para seleccionar y editar
// ------------------------
private void mostrarListaVuelosParaEditar(List<Vuelo> vuelos) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(ownerStage);
    dialog.setTitle("Seleccione un Vuelo para Editar");
    dialog.initStyle(StageStyle.UTILITY);

    VBox vbox = new VBox(20);
    vbox.setPadding(new Insets(24));
    vbox.setStyle("-fx-background-color: #ffffff;");

    // Header
    Label headerLabel = new Label("Editar Vuelos Registrados");
    headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
                         "-fx-font-size: 18px; -fx-font-weight: 600; " +
                         "-fx-text-fill: #1976d2; -fx-padding: 0 0 16 0;");

    // Contenedor de vuelos con scroll
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(400);

    VBox vuelosContainer = new VBox(12);
    vuelosContainer.setPadding(new Insets(16));
    vuelosContainer.setFillWidth(true); // Muy importante para que las tarjetas tomen todo el ancho

    if (vuelos.isEmpty()) {
        Label lblNoVuelos = new Label("No hay vuelos registrados para editar");
        lblNoVuelos.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
                             "-fx-font-size: 16px; -fx-text-fill: #9e9e9e; " +
                             "-fx-padding: 32; -fx-alignment: center;");
        vuelosContainer.getChildren().add(lblNoVuelos);
    } else {
        Label lblTitulo = new Label("Total de vuelos: " + vuelos.size());
        lblTitulo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
                           "-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #666666; " +
                           "-fx-padding: 0 0 16 0;");
        vuelosContainer.getChildren().add(lblTitulo);

        // Construye todas las tarjetas ANTES de mostrar el diálogo
        for (Vuelo vuelo : vuelos) {
            VBox vueloCard = crearTarjetaVuelo(vuelo, dialog);
            vueloCard.setMaxWidth(Double.MAX_VALUE); // importante para que se ajuste al scroll
            vuelosContainer.getChildren().add(vueloCard);
        }
    }

    scrollPane.setContent(vuelosContainer);

    // Botón cerrar
    Button btnCerrar = UIComponentFactory.crearBotonSecundario("Cerrar");
    btnCerrar.setOnAction(e -> dialog.close());

    vbox.getChildren().addAll(headerLabel, scrollPane, btnCerrar);

    Scene scene = new Scene(vbox, 700, 600);
    dialog.setScene(scene);

    // Forzar layout completo antes de mostrar
    dialog.show();
    dialog.sizeToScene(); // recalcula el tamaño y fuerza el layout
}


    /**
     * Muestra el formulario para eliminar vuelo con ID predefinido
     */
    public void mostrarFormularioEliminarVueloConId(String idVuelo) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Eliminar Vuelo - ID: " + idVuelo);
        dialog.initStyle(StageStyle.UTILITY);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(24));
        vbox.setStyle("-fx-background-color: #ffffff;");

        try {
            Vuelo vuelo = edespol.redvuelos.Persistencia.buscarVueloPorId(idVuelo);
            if (vuelo == null) {
                mostrarAlerta("Error", "Vuelo no encontrado", Alert.AlertType.ERROR);
                dialog.close();
                return;
            }

            // Header del formulario
            Label headerLabel = new Label("Confirmar Eliminación del Vuelo");
            headerLabel.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #d32f2f; -fx-padding: 0 0 16 0;");

            // Información del vuelo
            Label lblInfo = new Label(String.format("¿Está seguro de eliminar este vuelo?\n\n" +
                    "ID: %s\n" +
                                    "Origen: %s\n" +
                "Destino: %s\n" +
                "Aerolínea: %s\n" +
                "Hora: %s", 
                    vuelo.getId(),
                    vuelo.getOrigenCodigo(),
                    vuelo.getDestinoCodigo(),
                    vuelo.getAerolineaCodigo() != null ? vuelo.getAerolineaCodigo() : "N/A",
                    vuelo.getHoraSalida() != null ? vuelo.getHoraSalida() : "N/A"));
            
            lblInfo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-text-fill: #424242; -fx-padding: 16; -fx-wrap-text: true;");

            // Botones de acción
            HBox botonesContainer = new HBox(12);
            botonesContainer.setAlignment(Pos.CENTER_RIGHT);
            
            Button btnCancelar = UIComponentFactory.crearBotonSecundario("Cancelar");
            Button btnEliminar = UIComponentFactory.crearBotonPrimario("Eliminar Vuelo");
            btnEliminar.setStyle(btnEliminar.getStyle().replace("#1976d2", "#d32f2f").replace("#1565c0", "#c62828").replace("#0d47a1", "#b71c1c"));

            btnCancelar.setOnAction(e -> dialog.close());
            btnEliminar.setOnAction(e -> {
                try {
                    edespol.redvuelos.Persistencia.eliminarVuelo(idVuelo);
                    dialog.close();
                    onMapaUpdate.run(); // ACTUALIZAR MAPA
                    mostrarAlerta("Éxito", "Vuelo eliminado correctamente", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarAlerta("Error", "Error al eliminar: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            });

            botonesContainer.getChildren().addAll(btnCancelar, btnEliminar);
            vbox.getChildren().addAll(headerLabel, lblInfo, botonesContainer);

        } catch (Exception ex) {
            ex.printStackTrace();
            Label lblError = new Label("Error al cargar vuelo: " + ex.getMessage());
            lblError.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-text-fill: #d32f2f; -fx-padding: 16;");
            vbox.getChildren().add(lblError);
        }

        dialog.setScene(new Scene(vbox, 450, 350));
        dialog.showAndWait();
    }
    
    /**
     * Muestra el formulario para editar vuelo (método legacy)
     */
    public void mostrarFormularioEditarVuelo() {
        mostrarFormularioEliminarVuelo(); // Reutilizar el formulario de eliminar
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
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif;");
        
        alert.showAndWait();
    }
}
