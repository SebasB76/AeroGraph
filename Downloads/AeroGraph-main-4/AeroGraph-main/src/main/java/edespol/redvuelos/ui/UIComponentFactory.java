package edespol.redvuelos.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Factory para crear componentes UI reutilizables con Material Design
 */
public class UIComponentFactory {
    
    /**
     * Crea un campo de texto moderno con Material Design
     */
    public static TextField crearCampoTexto(String label, String placeholder, boolean requerido) {
        VBox campoContainer = new VBox(8);
        
        Label labelCampo = new Label(label + (requerido ? "*" : ""));
        labelCampo.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #424242;");
        
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.setPadding(new Insets(12.0, 16.0, 12.0, 16.0));
        campo.setStyle(
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
        
        // Efectos focus
        campo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                campo.setStyle(campo.getStyle().replace("#e0e0e0", "#1976d2").replace("#fafafa", "#ffffff"));
            } else {
                campo.setStyle(campo.getStyle().replace("#1976d2", "#e0e0e0").replace("#ffffff", "#fafafa"));
            }
        });
        
        campoContainer.getChildren().addAll(labelCampo, campo);
        return campo;
    }
    
    /**
     * Crea un botón primario con Material Design
     */
    public static Button crearBotonPrimario(String texto) {
        Button button = new Button(texto);
        button.setPadding(new Insets(12.0, 24.0, 12.0, 24.0));
        button.setStyle(
            "-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #1976d2; " +
            "-fx-border-color: transparent; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        // Efectos hover
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("#1976d2", "#1565c0")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("#1565c0", "#1976d2")));
        button.setOnMousePressed(e -> button.setStyle(button.getStyle().replace("#1565c0", "#0d47a1")));
        button.setOnMouseReleased(e -> button.setStyle(button.getStyle().replace("#0d47a1", "#1565c0")));
        
        return button;
    }
    
    /**
     * Crea un botón secundario con Material Design
     */
    public static Button crearBotonSecundario(String texto) {
        Button button = new Button(texto);
        button.setPadding(new Insets(12.0, 24.0, 12.0, 24.0));
        button.setStyle(
            "-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 500; " +
            "-fx-text-fill: #1976d2; " +
            "-fx-background-color: transparent; " +
            "-fx-border-color: #1976d2; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        // Efectos hover
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("transparent", "#e3f2fd")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("#e3f2fd", "transparent")));
        button.setOnMousePressed(e -> button.setStyle(button.getStyle().replace("#e3f2fd", "#bbdefb")));
        button.setOnMouseReleased(e -> button.setStyle(button.getStyle().replace("#bbdefb", "#e3f2fd")));
        
        return button;
    }
    
    /**
     * Crea un botón de menú con Material Design
     */
    public static Button crearBotonMenu(String texto, String icono) {
        String buttonText = icono.isEmpty() ? texto : icono + " " + texto;
        Button button = new Button(buttonText);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPadding(new Insets(12.0, 16.0, 12.0, 16.0));
        button.setStyle(
            "-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #424242; " +
            "-fx-background-color: #f5f5f5; " +
            "-fx-border-color: transparent; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand; " +
            "-fx-alignment: center-left;"
        );
        
        // Efectos hover y focus
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("#f5f5f5", "#e3f2fd")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("#e3f2fd", "#f5f5f5")));
        button.setOnMousePressed(e -> button.setStyle(button.getStyle().replace("#e3f2fd", "#bbdefb")));
        button.setOnMouseReleased(e -> button.setStyle(button.getStyle().replace("#bbdefb", "#e3f2fd")));
        
        return button;
    }
    
    /**
     * Crea una sección de menú con título
     */
    public static VBox crearSeccionMenu(String titulo, String icono) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(0, 0, 20, 0));
        
        String sectionTitleText = icono.isEmpty() ? titulo : icono + " " + titulo;
        Label sectionTitle = new Label(sectionTitleText);
        sectionTitle.setStyle("-fx-font-family: 'Segoe UI', 'Roboto', sans-serif; -fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #424242; -fx-padding: 0 0 8 0;");
        
        section.getChildren().add(sectionTitle);
        return section;
    }
}
