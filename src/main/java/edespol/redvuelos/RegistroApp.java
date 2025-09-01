package edespol.redvuelos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author camig
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class RegistroApp extends Application {

      @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registro");

        // Contenedor principal
        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");

        // Título
        Label titulo = new Label("Registro");
        titulo.getStyleClass().add("titulo");
        

        // Campos
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.getStyleClass().add("campo");
        txtNombre.setPrefWidth(400); // ancho preferido
        txtNombre.setMaxWidth(400);  // máximo ancho


        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo");
        txtCorreo.getStyleClass().add("campo");
        txtCorreo.setPrefWidth(400); // ancho preferido
        txtCorreo.setMaxWidth(400);  // máximo ancho
        
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.getStyleClass().add("campo");
        txtPassword.setPrefWidth(400); // ancho preferido
        txtPassword.setMaxWidth(400);  // máximo ancho
        
      


        // Botones
        Button btnRegistro = new Button("Registro");
        btnRegistro.getStyleClass().add("btn-registrar");

        Button btnLogin = new Button("Login");
        btnLogin.getStyleClass().add("btn-login");

        HBox botones = new HBox(15, btnRegistro, btnLogin);
        botones.setAlignment(Pos.CENTER);

        // Agregar al card
        card.getChildren().addAll(titulo, txtNombre, txtCorreo, txtPassword, botones);

        // Fondo gris
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #d3d3d3;");

        Scene scene = new Scene(root, 400, 400);

        // Vincular CSS
        
        scene.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
// 2. Evento al hacer clic en "Registro"
        btnRegistro.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String correo = txtCorreo.getText();
            String password = txtPassword.getText();

            Usuario usuario = new Usuario(nombre, correo, password);

            // 3. Guardar en archivo
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("usuarios.dat", true))) {
                oos.writeObject(usuario);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario registrado correctamente!");
                alert.showAndWait();
                
                // Limpiar campos
                txtNombre.clear();
                txtCorreo.clear();
                txtPassword.clear();
                  // Abrir ventana del mapa
        Stage mapaStage = new Stage();
        Mapa mapa = new Mapa();
        //Scene scene1 = new Scene(mapa.crearMapa(), 1000, 700);
       // mapaStage.setScene(scene1);
        mapaStage.setTitle("AeroGraph - Red de Vuelos");
        mapaStage.show();

        // Cerrar ventana de registro
        primaryStage.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}