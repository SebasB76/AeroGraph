OJO: se debe colocar las keys.!
Este proyecto es un sistema que representa una red de aeropuertos y vuelos utilizando grafos. Permite calcular rutas, buscar conexiones entre aeropuertos y obtener estadísticas de conectividad. Además de los algoritmos, incluye interfaz visual con JavaFX, persistencia de datos y un servidor web embebido.

Funciones principales:

Algoritmo de Dijkstra: calcula la ruta óptima entre dos aeropuertos según la métrica elegida (distancia, tiempo o costo).

Búsqueda en anchura (BFS): encuentra la ruta más corta en cantidad de saltos.

Búsqueda en profundidad (DFS): recorre caminos en profundidad hasta encontrar el destino.

Estadísticas: muestra el aeropuerto con más conexiones, el menos conectado, número total de aeropuertos y vuelos.

Persistencia: guarda y carga datos de aeropuertos y vuelos, con integración a Firebase.

Interfaz visual: permite al usuario gestionar aeropuertos, vuelos y rutas a través de formularios y un mapa.

Servidor web: expone algunos datos a través de un servidor embebido.

Estructura del sistema:

Modelo: clases Aeropuerto, Vuelo, VerticeAeropuerto, AristaVuelo y GrafoAeropuertos con los algoritmos y utilidades.

Algoritmos: Dijkstra, BFS y DFS implementados en GrafoAeropuertos.

Interfaz: clases de JavaFX como FormularioManager, MenuManager, RutaManager y MapaManager.

Persistencia: clase Persistencia con métodos para manejar los datos, incluyendo conexión a Firebase.

Servidor: clase WebServer para atender consultas desde navegador.
