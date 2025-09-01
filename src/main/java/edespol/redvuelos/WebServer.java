/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edespol.redvuelos;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebServer {
    private static HttpServer server;
    private static final int PORT = 8080;

    public static void start() throws IOException {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            server.createContext("/", exchange -> {
                try {
                    System.out.println("Solicitud recibida en el servidor web");
                    
                    // Verificar que el archivo existe
                    java.nio.file.Path htmlPath = Paths.get("src/main/resources/map.html");
                    if (!Files.exists(htmlPath)) {
                        System.err.println("Archivo no encontrado: " + htmlPath.toAbsolutePath());
                        String error = "Error: Archivo map.html no encontrado en " + htmlPath.toAbsolutePath();
                        exchange.sendResponseHeaders(404, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                    
                    // Leer el archivo HTML
                    String htmlContent = new String(Files.readAllBytes(htmlPath));
                    System.out.println("Archivo HTML leído correctamente (" + htmlContent.length() + " caracteres)");
                    
                    // Configurar headers
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    exchange.sendResponseHeaders(200, htmlContent.getBytes("UTF-8").length);
                    
                    // Enviar respuesta
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlContent.getBytes("UTF-8"));
                    os.close();
                    
                    System.out.println("Respuesta enviada correctamente");
                    
                } catch (IOException e) {
                    System.err.println("Error en el servidor web: " + e.getMessage());
                    try {
                        String error = "Error interno del servidor: " + e.getMessage();
                        exchange.sendResponseHeaders(500, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                    } catch (IOException ex) {
                        System.err.println("Error al enviar respuesta de error: " + ex.getMessage());
                    }
                }
            });
            
            server.start();
            System.out.println("Web server iniciado en http://localhost:" + PORT);
            System.out.println("Servidor web listo para recibir peticiones");
            
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor web: " + e.getMessage());
            throw e;
        }
    }

    public static void stop() {
        if (server != null) {
            System.out.println("Deteniendo servidor web...");
            server.stop(0);
            System.out.println("Servidor web detenido");
        }
    }
}