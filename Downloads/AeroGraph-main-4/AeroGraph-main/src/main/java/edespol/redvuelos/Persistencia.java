/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edespol.redvuelos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;

public class Persistencia {

    // ======================================================
    // 🔹 INICIALIZACIÓN DE FIREBASE
    // ======================================================
    
    private static void initializeFirebase() throws IOException {
        try {
            FirebaseConfig.initFirebase();
        } catch (IOException e) {
            throw new IOException("Error al inicializar Firebase: " + e.getMessage(), e);
        }
    }

    // ======================================================
    // 🔹 AEROPUERTOS
    // ======================================================

    public static List<Aeropuerto> cargarAeropuertos() throws IOException, Exception {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = db.collection("aeropuertos").get();
        for (DocumentSnapshot doc : future.get().getDocuments()) {
            Aeropuerto a = new Aeropuerto();
            a.setCodigo(doc.getString("codigo"));
            a.setNombre(doc.getString("nombre"));
            a.setCiudad(doc.getString("ciudad"));
            a.setPais(doc.getString("pais"));
            a.setLatitud(doc.getDouble("latitud") != null ? doc.getDouble("latitud") : 0.0);
            a.setLongitud(doc.getDouble("longitud") != null ? doc.getDouble("longitud") : 0.0);
            aeropuertos.add(a);
        }
        return aeropuertos;
    }

    public static void guardarAeropuerto(Aeropuerto a) throws IOException {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        Map<String, Object> data = new HashMap<>();
        data.put("codigo", a.getCodigo());
        data.put("nombre", a.getNombre());
        data.put("ciudad", a.getCiudad());
        data.put("pais", a.getPais());
        data.put("latitud", a.getLatitud());
        data.put("longitud", a.getLongitud());

        db.collection("aeropuertos").document(a.getCodigo()).set(data);
    }

    public static void eliminarAeropuerto(String codigo) throws IOException {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        db.collection("aeropuertos").document(codigo).delete();
    }

    // ======================================================
    // 🔹 VUELOS
    // ======================================================

    public static List<Vuelo> cargarVuelos() throws IOException, Exception {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        List<Vuelo> vuelos = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = db.collection("vuelos").get();
        for (DocumentSnapshot doc : future.get().getDocuments()) {
            Vuelo v = new Vuelo();
            v.setId(doc.getId()); // usamos el id del documento
            v.setOrigenCodigo(doc.getString("origenCodigo"));
            v.setDestinoCodigo(doc.getString("destinoCodigo"));
            v.setAerolineaCodigo(doc.getString("aerolineaCodigo"));
            v.setDistanciaKm(doc.getDouble("distanciaKm") != null ? doc.getDouble("distanciaKm") : 0.0);
            v.setDuracionMin(doc.getLong("duracionMin") != null ? doc.getLong("duracionMin").intValue() : 0);
            v.setCostoUsd(doc.getDouble("costoUsd") != null ? doc.getDouble("costoUsd") : 0.0);
            v.setHoraSalida(doc.getString("horaSalida"));
            vuelos.add(v);
        }
        return vuelos;
    }

    public static void guardarVuelo(Vuelo v) throws IOException {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        Map<String, Object> data = new HashMap<>();
        data.put("origenCodigo", v.getOrigenCodigo());
        data.put("destinoCodigo", v.getDestinoCodigo());
        data.put("aerolineaCodigo", v.getAerolineaCodigo());
        data.put("distanciaKm", v.getDistanciaKm());
        data.put("duracionMin", v.getDuracionMin());
        data.put("costoUsd", v.getCostoUsd());
        data.put("horaSalida", v.getHoraSalida());

        if (v.getId() == null || v.getId().isEmpty()) {
            // si no tiene id, Firestore genera uno nuevo
            db.collection("vuelos").add(data);
        } else {
            // si ya tiene id, se actualiza o guarda con ese mismo id
            db.collection("vuelos").document(v.getId()).set(data);
        }
    }

    public static void eliminarVuelo(String id) throws IOException {
        initializeFirebase(); // ✅ Inicialización agregada
        Firestore db = FirebaseConfig.getFirestore();
        db.collection("vuelos").document(id).delete();
    }

    // ======================================================
    // 🔹 MÉTODOS ADICIONALES CON MANEJO DE ERRORES MEJORADO
    // ======================================================

    public static Aeropuerto buscarAeropuertoPorCodigo(String codigo) throws IOException {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            
            DocumentSnapshot doc = db.collection("aeropuertos").document(codigo).get().get();
            
            if (doc.exists()) {
                Aeropuerto a = new Aeropuerto();
                a.setCodigo(doc.getString("codigo"));
                a.setNombre(doc.getString("nombre"));
                a.setCiudad(doc.getString("ciudad"));
                a.setPais(doc.getString("pais"));
                a.setLatitud(doc.getDouble("latitud") != null ? doc.getDouble("latitud") : 0.0);
                a.setLongitud(doc.getDouble("longitud") != null ? doc.getDouble("longitud") : 0.0);
                return a;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al buscar aeropuerto: " + e.getMessage(), e);
        }
    }

    public static boolean aeropuertoExiste(String codigo) throws IOException {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            
            DocumentSnapshot doc = db.collection("aeropuertos").document(codigo).get().get();
            return doc.exists();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al verificar aeropuerto: " + e.getMessage(), e);
        }
    }

    // ======================================================
    // 🔹 MÉTODOS PARA BÚSQUEDA DE VUELOS
    // ======================================================

    public static Vuelo buscarVueloPorId(String id) throws IOException {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            
            DocumentSnapshot doc = db.collection("vuelos").document(id).get().get();
            
            if (doc.exists()) {
                Vuelo v = new Vuelo();
                v.setId(doc.getId());
                v.setOrigenCodigo(doc.getString("origenCodigo"));
                v.setDestinoCodigo(doc.getString("destinoCodigo"));
                v.setAerolineaCodigo(doc.getString("aerolineaCodigo"));
                v.setDistanciaKm(doc.getDouble("distanciaKm") != null ? doc.getDouble("distanciaKm") : 0.0);
                v.setDuracionMin(doc.getLong("duracionMin") != null ? doc.getLong("duracionMin").intValue() : 0);
                v.setCostoUsd(doc.getDouble("costoUsd") != null ? doc.getDouble("costoUsd") : 0.0);
                v.setHoraSalida(doc.getString("horaSalida"));
                return v;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al buscar vuelo: " + e.getMessage(), e);
        }
    }

    public static List<Vuelo> buscarVuelosPorOrigen(String codigoOrigen) throws IOException, Exception {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            List<Vuelo> vuelos = new ArrayList<>();

            ApiFuture<QuerySnapshot> future = db.collection("vuelos")
                    .whereEqualTo("origenCodigo", codigoOrigen)
                    .get();
            
            for (DocumentSnapshot doc : future.get().getDocuments()) {
                Vuelo v = new Vuelo();
                v.setId(doc.getId());
                v.setOrigenCodigo(doc.getString("origenCodigo"));
                v.setDestinoCodigo(doc.getString("destinoCodigo"));
                v.setAerolineaCodigo(doc.getString("aerolineaCodigo"));
                v.setDistanciaKm(doc.getDouble("distanciaKm") != null ? doc.getDouble("distanciaKm") : 0.0);
                v.setDuracionMin(doc.getLong("duracionMin") != null ? doc.getLong("duracionMin").intValue() : 0);
                v.setCostoUsd(doc.getDouble("costoUsd") != null ? doc.getDouble("costoUsd") : 0.0);
                v.setHoraSalida(doc.getString("horaSalida"));
                vuelos.add(v);
            }
            return vuelos;
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al buscar vuelos por origen: " + e.getMessage(), e);
        }
    }

    public static List<Vuelo> buscarVuelosPorDestino(String codigoDestino) throws IOException, Exception {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            List<Vuelo> vuelos = new ArrayList<>();

            ApiFuture<QuerySnapshot> future = db.collection("vuelos")
                    .whereEqualTo("destinoCodigo", codigoDestino)
                    .get();
            
            for (DocumentSnapshot doc : future.get().getDocuments()) {
                Vuelo v = new Vuelo();
                v.setId(doc.getId());
                v.setOrigenCodigo(doc.getString("origenCodigo"));
                v.setDestinoCodigo(doc.getString("destinoCodigo"));
                v.setAerolineaCodigo(doc.getString("aerolineaCodigo"));
                v.setDistanciaKm(doc.getDouble("distanciaKm") != null ? doc.getDouble("distanciaKm") : 0.0);
                v.setDuracionMin(doc.getLong("duracionMin") != null ? doc.getLong("duracionMin").intValue() : 0);
                v.setCostoUsd(doc.getDouble("costoUsd") != null ? doc.getDouble("costoUsd") : 0.0);
                v.setHoraSalida(doc.getString("horaSalida"));
                vuelos.add(v);
            }
            return vuelos;
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al buscar vuelos por destino: " + e.getMessage(), e);
        }
    }

    public static List<Vuelo> buscarVuelosPorAerolinea(String codigoAerolinea) throws IOException, Exception {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            List<Vuelo> vuelos = new ArrayList<>();

            ApiFuture<QuerySnapshot> future = db.collection("vuelos")
                    .whereEqualTo("aerolineaCodigo", codigoAerolinea)
                    .get();
            
            for (DocumentSnapshot doc : future.get().getDocuments()) {
                Vuelo v = new Vuelo();
                v.setId(doc.getId());
                v.setOrigenCodigo(doc.getString("origenCodigo"));
                v.setDestinoCodigo(doc.getString("destinoCodigo"));
                v.setAerolineaCodigo(doc.getString("aerolineaCodigo"));
                v.setDistanciaKm(doc.getDouble("distanciaKm") != null ? doc.getDouble("distanciaKm") : 0.0);
                v.setDuracionMin(doc.getLong("duracionMin") != null ? doc.getLong("duracionMin").intValue() : 0);
                v.setCostoUsd(doc.getDouble("costoUsd") != null ? doc.getDouble("costoUsd") : 0.0);
                v.setHoraSalida(doc.getString("horaSalida"));
                vuelos.add(v);
            }
            return vuelos;
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al buscar vuelos por aerolínea: " + e.getMessage(), e);
        }
    }

    public static boolean vueloExiste(String id) throws IOException {
        try {
            initializeFirebase();
            Firestore db = FirebaseConfig.getFirestore();
            
            DocumentSnapshot doc = db.collection("vuelos").document(id).get().get();
            return doc.exists();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error al verificar vuelo: " + e.getMessage(), e);
        }
    }
    
   

}