package edespol.redvuelos.persistencia;

import java.io.FileWriter;
import java.util.LinkedList;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.AristaVuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.VerticeAeropuerto;

public class EscritorCsv {

    public static void guardarAeropuertos(String ruta, GrafoAeropuertos g) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("code,name,city,country,lat,lng\n");

            LinkedList<Aeropuerto> lista = new LinkedList<>();
            for (VerticeAeropuerto v : g.getVertices()) {
                lista.add(v.getDato());
            }

            java.util.Collections.sort(
                lista,
                (a, b) -> {
                    String c1 = (a.getCodigo() == null) ? "" : a.getCodigo();
                    String c2 = (b.getCodigo() == null) ? "" : b.getCodigo();
                    return c1.compareTo(c2);
                }
            );

            for (Aeropuerto a : lista) {
                fw.write(
                    a.getCodigo() + "," +
                    a.getNombre() + "," +
                    a.getCiudad() + "," +
                    a.getPais() + "," +
                    a.getLatitud() + "," +
                    a.getLongitud() + "\n"
                );
            }
        } catch (Exception e) {
            System.out.println("Error guardando aeropuertos: " + e.getMessage());
        }
    }

    public static void guardarVuelos(String ruta, GrafoAeropuertos g) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("id,originCode,destCode,airlineCode,distanceKm,durationMin,costUsd,departureTime\n");

            for (VerticeAeropuerto v : g.getVertices()) {
                for (AristaVuelo e : v.getSalientes()) {
                    Vuelo f = e.getVuelo();
                    if (f == null) {
                        continue;
                    }

                    fw.write(
                        f.getId() + "," +
                        f.getOrigenCodigo() + "," +
                        f.getDestinoCodigo() + "," +
                        f.getAerolineaCodigo() + "," +
                        f.getDistanciaKm() + "," +
                        f.getDuracionMin() + "," +
                        f.getCostoUsd() + "," +
                        (f.getHoraSalida() == null ? "" : f.getHoraSalida()) + "\n"
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error guardando vuelos: " + e.getMessage());
        }
    }
}
