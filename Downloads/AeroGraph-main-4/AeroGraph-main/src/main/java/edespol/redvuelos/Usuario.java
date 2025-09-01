/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edespol.redvuelos;

/**
 *
 * @author camig
 */


import java.io.*;

// 1. Clase que representa los datos del usuario
class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String correo;
    private String password;

    public Usuario(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Correo: " + correo + ", Password: " + password;
    }
}