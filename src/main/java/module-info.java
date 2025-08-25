module edespol.redvuelos {
    requires javafx.controls;

    // App
    exports edespol.redvuelos;

    // Exponer paquetes para que la UI acceda al modelo y la persistencia
    exports edespol.redvuelos.dominio;
    exports edespol.redvuelos.grafo;
    exports edespol.redvuelos.persistencia;
}
