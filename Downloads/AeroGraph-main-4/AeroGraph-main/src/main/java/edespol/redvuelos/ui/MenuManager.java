package edespol.redvuelos.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Manager para gestionar el menú lateral de la aplicación
 */
public class MenuManager {
    
    private VBox menuContainer;
    private Stage ownerStage;
    
    // Referencias a los botones para configurar eventos
    private Button btnAgregarAeropuerto;
    private Button btnEditarAeropuerto;
    private Button btnEliminarAeropuerto;
    private Button btnAgregarVuelo;
    private Button btnEditarVuelo;
    private Button btnEliminarVuelo;
    private Button btnListarVuelos;
    private Button btnBuscarRuta;
    private Button btnBuscarRutaBFS;
    private Button btnBuscarRutaDFS;
    private Button btnEstadisticas;
    private Button btnDemo;
    private Button btnRecargarMapa;
    
    public MenuManager(Stage ownerStage) {
        this.ownerStage = ownerStage;
        crearMenu();
    }
    
    /**
     * Crea el menú lateral completo
     */
    private void crearMenu() {
        // Menú lateral modernizado con Material Design
        VBox menu = new VBox(16);
        menu.setPadding(new Insets(24, 20, 24, 20));
        menu.setPrefWidth(300);
        menu.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-width: 0 1 0 0;");

        // Header de la aplicación
        VBox header = crearHeader();
        
        // Secciones del menú
        VBox aeropuertosSection = crearSeccionAeropuertos();
        VBox vuelosSection = crearSeccionVuelos();
        VBox rutasSection = crearSeccionRutas();
        VBox estadisticasSection = crearSeccionEstadisticas();
        
        // Botón de recarga
        btnRecargarMapa = UIComponentFactory.crearBotonMenu("Recargar Mapa", "");
        btnRecargarMapa.setStyle(btnRecargarMapa.getStyle() + " -fx-background-color: #4caf50; -fx-text-fill: white;");

        // Crear contenedor principal del menú con scroll
        VBox menuContent = new VBox(16);
        menuContent.setPadding(new Insets(24, 20, 24, 20));
        menuContent.getChildren().addAll(
                header,
                aeropuertosSection,
                vuelosSection,
                rutasSection,
                estadisticasSection,
                new Separator(),
                btnRecargarMapa
        );

        // Crear ScrollPane para el menú
        ScrollPane menuScrollPane = crearScrollPane(menuContent);
        menu.getChildren().add(menuScrollPane);
        
        this.menuContainer = menu;
    }
    
    /**
     * Crea el header de la aplicación
     */
    private VBox crearHeader() {
        VBox header = new VBox(8);
        header.setPadding(new Insets(0, 0, 24, 0));
        
        Label appTitle = new Label("AeroGraph");
        appTitle.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1976d2;");
        
        Label appSubtitle = new Label("Sistema de Gestión de Vuelos");
        appSubtitle.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-text-fill: #666666;");
        
        header.getChildren().addAll(appTitle, appSubtitle);
        return header;
    }
    
    /**
     * Crea la sección de aeropuertos
     */
    private VBox crearSeccionAeropuertos() {
        VBox aeropuertosSection = UIComponentFactory.crearSeccionMenu("Aeropuertos", "");
        
        btnAgregarAeropuerto = UIComponentFactory.crearBotonMenu("Agregar Aeropuerto", "");
        btnEditarAeropuerto = UIComponentFactory.crearBotonMenu("Editar Aeropuerto", "");
        btnEliminarAeropuerto = UIComponentFactory.crearBotonMenu("Eliminar Aeropuerto", "");
        
        aeropuertosSection.getChildren().addAll(btnAgregarAeropuerto, btnEditarAeropuerto, btnEliminarAeropuerto);
        return aeropuertosSection;
    }
    
    /**
     * Crea la sección de vuelos
     */
    private VBox crearSeccionVuelos() {
        VBox vuelosSection = UIComponentFactory.crearSeccionMenu("Vuelos", "");
        
        btnAgregarVuelo = UIComponentFactory.crearBotonMenu("Agregar Vuelo", "");
        btnEditarVuelo = UIComponentFactory.crearBotonMenu("Editar Vuelo", "");
        btnEliminarVuelo = UIComponentFactory.crearBotonMenu("Eliminar Vuelo", "");
        btnListarVuelos = UIComponentFactory.crearBotonMenu("Listar Vuelos", "");
        
        vuelosSection.getChildren().addAll(btnAgregarVuelo, btnEditarVuelo, btnEliminarVuelo, btnListarVuelos);
        return vuelosSection;
    }
    
    /**
     * Crea la sección de búsqueda de rutas
     */
    private VBox crearSeccionRutas() {
        VBox rutasSection = UIComponentFactory.crearSeccionMenu("Búsqueda de Rutas", "");
        
        btnBuscarRuta = UIComponentFactory.crearBotonMenu("Ruta Óptima (Dijkstra)", "");
        btnBuscarRutaBFS = UIComponentFactory.crearBotonMenu("Búsqueda BFS", "");
        btnBuscarRutaDFS = UIComponentFactory.crearBotonMenu("Búsqueda DFS", "");
        
        rutasSection.getChildren().addAll(btnBuscarRuta, btnBuscarRutaBFS, btnBuscarRutaDFS);
        return rutasSection;
    }
    
    /**
     * Crea la sección de estadísticas
     */
    private VBox crearSeccionEstadisticas() {
        VBox estadisticasSection = UIComponentFactory.crearSeccionMenu("Estadísticas", "");
        
        btnEstadisticas = UIComponentFactory.crearBotonMenu("Ver Estadísticas", "");
        btnDemo = UIComponentFactory.crearBotonMenu("Ejecutar Demo", "");
        
        estadisticasSection.getChildren().addAll(btnEstadisticas, btnDemo);
        return estadisticasSection;
    }
    
    /**
     * Crea el ScrollPane del menú
     */
    private ScrollPane crearScrollPane(VBox content) {
        ScrollPane menuScrollPane = new ScrollPane(content);
        menuScrollPane.setFitToWidth(true);
        menuScrollPane.setPrefWidth(300);
        menuScrollPane.getStyleClass().add("menu-scroll-pane");
        menuScrollPane.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 0 1 0 0; " +
            "-fx-background: transparent;"
        );

        // Configurar políticas de scroll
        menuScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        menuScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Aplicar estilos modernos a la barra de scroll
        menuScrollPane.setStyle(menuScrollPane.getStyle() + 
            " -fx-background: transparent;" +
            " -fx-background-color: transparent;"
        );
        
        return menuScrollPane;
    }
    
    /**
     * Obtiene el contenedor del menú
     */
    public VBox getMenuContainer() {
        return menuContainer;
    }
    
    /**
     * Obtiene el botón de agregar aeropuerto
     */
    public Button getBtnAgregarAeropuerto() {
        return btnAgregarAeropuerto;
    }
    
    /**
     * Obtiene el botón de editar aeropuerto
     */
    public Button getBtnEditarAeropuerto() {
        return btnEditarAeropuerto;
    }
    
    /**
     * Obtiene el botón de eliminar aeropuerto
     */
    public Button getBtnEliminarAeropuerto() {
        return btnEliminarAeropuerto;
    }
    
    /**
     * Obtiene el botón de agregar vuelo
     */
    public Button getBtnAgregarVuelo() {
        return btnAgregarVuelo;
    }
    
    /**
     * Obtiene el botón de editar vuelo
     */
    public Button getBtnEditarVuelo() {
        return btnEditarVuelo;
    }
    
    /**
     * Obtiene el botón de eliminar vuelo
     */
    public Button getBtnEliminarVuelo() {
        return btnEliminarVuelo;
    }
    
    /**
     * Obtiene el botón de listar vuelos
     */
    public Button getBtnListarVuelos() {
        return btnListarVuelos;
    }
    
    /**
     * Obtiene el botón de buscar ruta
     */
    public Button getBtnBuscarRuta() {
        return btnBuscarRuta;
    }
    
    /**
     * Obtiene el botón de buscar ruta BFS
     */
    public Button getBtnBuscarRutaBFS() {
        return btnBuscarRutaBFS;
    }
    
    /**
     * Obtiene el botón de buscar ruta DFS
     */
    public Button getBtnBuscarRutaDFS() {
        return btnBuscarRutaDFS;
    }
    
    /**
     * Obtiene el botón de estadísticas
     */
    public Button getBtnEstadisticas() {
        return btnEstadisticas;
    }
    
    /**
     * Obtiene el botón de demo
     */
    public Button getBtnDemo() {
        return btnDemo;
    }
    
    /**
     * Obtiene el botón de recargar mapa
     */
    public Button getBtnRecargarMapa() {
        return btnRecargarMapa;
    }
}
