package edespol.redvuelos.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.dominio.Vuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;

public class LectorCsv {

    public static void cargarAeropuertos(String ruta, GrafoAeropuertos g) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");
                if (t.length < 6) {
                    continue;
                }

                Aeropuerto a = new Aeropuerto();
                a.setCodigo(t[0]);
                a.setNombre(t[1]);
                a.setCiudad(t[2]);
                a.setPais(t[3]);
                a.setLatitud(Double.parseDouble(t[4]));
                a.setLongitud(Double.parseDouble(t[5]));

                g.agregarAeropuerto(a);
            }
        } catch (Exception e) {
            System.out.println("Error aeropuertos: " + e.getMessage());
        }
    }

    public static void cargarVuelos(String ruta, GrafoAeropuertos g) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");
                if (t.length < 7) {
                    continue;
                }

                Vuelo v = new Vuelo();
                v.setId(t[0]);
                v.setOrigenCodigo(t[1]);
                v.setDestinoCodigo(t[2]);
                v.setAerolineaCodigo(t[3]);
                v.setDistanciaKm(Double.parseDouble(t[4]));
                v.setDuracionMin(Integer.parseInt(t[5]));
                v.setCostoUsd(Double.parseDouble(t[6]));

                if (t.length >= 8) {
                    v.setHoraSalida(t[7]);
                }

                g.agregarVuelo(v);
            }
        } catch (Exception e) {
            System.out.println("Error vuelos: " + e.getMessage());
        }
    }
}
