# AeroGraph - Sistema de Gestión de Vuelos Inteligente



**AeroGraph** ha sido completamente rediseñado con una interfaz moderna y elegante que sigue los principios de **Material Design 3.0**, ofreciendo una experiencia de usuario excepcional.

### ✨ **Características de la Neva Interfaz:**

#### **🎯 Diseño Material Design:**
- **Paleta de colores moderna**: Azul primario (#1976d2) con acentos complementarios
- **Tipografía Roboto/Segoe UI**: Legible y profesional
- **Espaciado coherente**: Sistema de espaciado de 8px para consistencia visual
- **Sombras y elevación**: Efectos sutiles para jerarquía visual

#### **🔧 Componentes :**
- **Botones con estados**: Hover, focus, pressed con transiciones suaves
- **Campos de texto elegantes**: Bordes redondeados y efectos de focus
- **Tarjetas interactivas**: Diseño de tarjetas para listas de vuelos
- **Iconos emoji**: Iconografía moderna y amigable
- **Navegación intuitiva**: Menú lateral organizado por secciones


## 📋 Descripción del Proyecto

AeroGraph es una aplicación JavaFX avanzada que implementa un sistema completo de gestión de vuelos para aeropuertos. La aplicación utiliza estructuras de datos de grafos para modelar la red de vuelos, implementando algoritmos de búsqueda de rutas como Dijkstra, BFS y DFS.

## 🎯 Objetivos del Proyecto

- **Visualización Inteligente**: Representar visualmente la red de vuelos usando Google Maps integrado
- **Gestión Completa**: CRUD completo para aeropuertos y vuelos
- **Búsqueda Avanzada**: Múltiples algoritmos de búsqueda de rutas (Dijkstra, BFS, DFS)
- **Análisis Estadístico**: Estadísticas de conectividad y análisis de la red
- **Persistencia Robusta**: Almacenamiento en Firebase Firestore
- **Interfaz Moderna**: Diseño Material Design 3.0 con JavaFX

## 🚀 Funcionalidades Implementadas

### ✅ **Funcionalidades Principales**
- **Gestión de Aeropuertos**: Agregar, editar, eliminar y listar aeropuertos
- **Gestión de Vuelos**: Agregar, editar, eliminar y listar vuelos
- **Búsqueda de Rutas**: 
  - Algoritmo de Dijkstra para rutas óptimas
  - Búsqueda en anchura (BFS) para rutas con menos escalas
  - Búsqueda en profundidad (DFS) para exploración de rutas
- **Visualización en Mapa**: Integración con Google Maps para mostrar la red
- **Estadísticas**: Análisis de conectividad y métricas de la red
- **Demo Automático**: Funcionalidad de demostración del sistema

### ✅ **Características Técnicas**
- **Persistencia**: Firebase Firestore para almacenamiento de datos
- **Arquitectura**: Patrón Manager para separación de responsabilidades
- **UI Moderna**: Material Design 3.0 con JavaFX
- **Integración Web**: JxBrowser para Google Maps
- **Manejo de Errores**: Sistema robusto de manejo de excepciones

## 🏗️ Arquitectura del Sistema

### **Estructura de Paquetes**
```
edespol.redvuelos/
├── dominio/           # Entidades del dominio
├── grafo/            # Implementación del grafo
├── ui/               # Componentes de interfaz
├── App.java          # Punto de entrada principal
├── Mapa.java         # Coordinador principal
├── RedVuelosManager.java # Lógica de negocio
├── Persistencia.java # Capa de persistencia
├── FirebaseConfig.java # Configuración de Firebase
└── WebServer.java    # Servidor web local
```

## 📚 **DOCUMENTACIÓN DETALLADA DEL CÓDIGO**

### **1. CLASES PRINCIPALES**

#### **`App.java` - Punto de Entrada**
```java
// Clase principal que extiende Application de JavaFX
// Delega la inicialización a Mapa.java para mantener separación de responsabilidades
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Mapa.start(stage); // Delega al coordinador principal
    }
}
```

#### **`Mapa.java` - Coordinador Principal**
```java
// Clase coordinadora que orquesta toda la aplicación
// Responsabilidades:
// - Inicialización de Firebase y servidor web
// - Creación de managers especializados
// - Configuración de la interfaz principal
// - Manejo de eventos de botones
// - Coordinación entre componentes

public class Mapa extends Application {
    // Managers especializados para cada área funcional
    private MapaManager mapaManager;        // Gestión del mapa
    private MenuManager menuManager;        // Gestión del menú lateral
    private FormularioManager formularioManager; // Gestión de formularios
    private RutaManager rutaManager;       // Gestión de búsqueda de rutas
    private EstadisticasManager estadisticasManager; // Gestión de estadísticas
    
    @Override
    public void start(Stage stage) {
        // 1. Inicializar servicios (Firebase, WebServer)
        // 2. Crear managers especializados
        // 3. Configurar interfaz principal
        // 4. Configurar eventos de botones
        // 5. Cargar mapa inicial
    }
}
```

### **2. MANAGERS DE INTERFAZ (Paquete `ui/`)**

#### **`UIComponentFactory.java` - Factory de Componentes UI**
```java
// Factory que centraliza la creación de componentes UI reutilizables
// Aplica Material Design 3.0 consistentemente en toda la aplicación

public class UIComponentFactory {
    
    // Crea campos de texto con estilos modernos y efectos de focus
    public static TextField crearCampoTexto(String label, String placeholder, boolean requerido)
    
    // Crea botones primarios (azules) con efectos hover
    public static Button crearBotonPrimario(String texto)
    
    // Crea botones secundarios (outlined) con efectos hover
    public static Button crearBotonSecundario(String texto)
    
    // Crea botones de menú con estilos consistentes
    public static Button crearBotonMenu(String texto, String icono)
    
    // Crea secciones de menú con títulos estilizados
    public static VBox crearSeccionMenu(String titulo, String icono)
}
```

#### **`MenuManager.java` - Gestión del Menú Lateral**
```java
// Manager que construye y gestiona todo el menú lateral de la aplicación
// Organiza las funcionalidades en secciones lógicas

public class MenuManager {
    
    // Secciones del menú:
    // - Aeropuertos: CRUD de aeropuertos
    // - Vuelos: CRUD de vuelos
    // - Búsqueda de Rutas: Algoritmos de búsqueda
    // - Estadísticas: Análisis de la red
    
    private void crearSeccionAeropuertos() {
        // Crea botones para agregar, editar, eliminar aeropuertos
        // Cada botón se conecta con FormularioManager
    }
    
    private void crearSeccionVuelos() {
        // Crea botones para gestionar vuelos
        // Incluye listado de vuelos existentes
    }
    
    private void crearSeccionRutas() {
        // Botones para diferentes algoritmos de búsqueda
        // Dijkstra, BFS, DFS
    }
    
    private void crearSeccionEstadisticas() {
        // Botones para ver estadísticas y ejecutar demo
    }
}
```

#### **`FormularioManager.java` - Gestión de Formularios**
```java
// Manager que maneja todos los formularios de la aplicación
// Implementa CRUD completo para aeropuertos y vuelos

public class FormularioManager {
    
    // FORMULARIOS DE AEROPUERTOS:
    
    public void mostrarFormularioAgregarAeropuerto() {
        // Formulario para crear nuevos aeropuertos
        // Campos: código IATA, nombre, ciudad, país, latitud, longitud
        // Validaciones: campos obligatorios, código único
        // Persistencia: guarda en Firebase
        // Actualización: refresca el mapa automáticamente
    }
    
    public void mostrarFormularioEditarAeropuerto() {
        // Formulario para modificar aeropuertos existentes
        // Búsqueda por código IATA
        // Pre-llenado de campos con datos actuales
        // Validación y persistencia
    }
    
    public void mostrarFormularioEliminarAeropuerto() {
        // Formulario para eliminar aeropuertos
        // Búsqueda y confirmación antes de eliminar
        // Verificación de dependencias
    }
    
    // FORMULARIOS DE VUELOS:
    
    public void mostrarFormularioAgregarVuelo() {
        // Formulario para crear nuevos vuelos
        // Campos: origen, destino, aerolínea, distancia, duración, costo, hora
        // Validaciones: aeropuertos deben existir
        // Generación automática de ID único
    }
    
    public void mostrarFormularioEditarVueloConId(String idVuelo) {
        // Formulario para modificar vuelos existentes
        // Pre-llenado con datos actuales del vuelo
        // Validación de aeropuertos
    }
    
    public void mostrarFormularioEliminarVuelo() {
        // Formulario para eliminar vuelos
        // Búsqueda por ID
        // Confirmación antes de eliminar
    }
    
    public void mostrarListaVuelos() {
        // Vista de todos los vuelos en el sistema
        // Tarjetas individuales para cada vuelo
        // Botones de editar/eliminar por vuelo
        // Diseño responsive con scroll
    }
    
    // MÉTODOS AUXILIARES:
    
    private VBox crearTarjetaVuelo(Vuelo vuelo, Stage dialog) {
        // Crea tarjetas visuales para cada vuelo
        // Muestra información: origen → destino, aerolínea, hora
        // Botones de acción integrados
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        // Sistema de alertas estilizado
        // Aplica estilos Material Design a los diálogos
    }
}
```

#### **`RutaManager.java` - Gestión de Búsqueda de Rutas**
```java
// Manager que implementa todos los algoritmos de búsqueda de rutas
// Proporciona interfaces para Dijkstra, BFS y DFS

public class RutaManager {
    
    public void mostrarFormularioBuscarRutaOptima() {
        // Formulario para búsqueda con algoritmo de Dijkstra
        // Selección de origen y destino desde ComboBox
        // Selección de métrica (distancia, tiempo, costo)
        // Resultado detallado con ruta y vuelos específicos
        // Muestra peso total de la ruta
    }
    
    public void mostrarFormularioBuscarRutaBFS() {
        // Formulario para búsqueda en anchura
        // Encuentra rutas con menor número de escalas
        // Resultado: secuencia de aeropuertos
        // Ideal para conexiones directas
    }
    
    public void mostrarFormularioBuscarRutaDFS() {
        // Formulario para búsqueda en profundidad
        // Explora rutas alternativas
        // Útil para encontrar conexiones indirectas
    }
    
    private void cargarAeropuertosEnComboBox(ComboBox<String> cmbOrigen, ComboBox<String> cmbDestino) {
        // Carga todos los aeropuertos en ComboBox
        // Formato: "CODIGO - Nombre (Ciudad, País)"
        // Selección automática del primer elemento
    }
    
    private String extraerCodigoAeropuerto(String textoDescriptivo) {
        // Extrae código IATA del texto descriptivo del ComboBox
        // Parsea "GYE - Aeropuerto José Joaquín de Olmedo (Guayaquil, Ecuador)" → "GYE"
    }
}
```

#### **`EstadisticasManager.java` - Gestión de Estadísticas**
```java
// Manager que proporciona análisis y estadísticas de la red
// Ejecuta cálculos en hilos separados para no bloquear la UI

public class EstadisticasManager {
    
    public void mostrarEstadisticas() {
        // Muestra estadísticas completas de conectividad
        // Ejecuta en hilo separado para no bloquear la UI
        // Estadísticas incluyen:
        // - Conexiones por aeropuerto
        // - Aeropuerto más/menos conectado
        // - Total de aeropuertos y vuelos
        // - Resumen general de la red
    }
    
    public void ejecutarDemo() {
        // Ejecuta demostración automática del sistema
        // Muestra capacidades de la aplicación
        // Ejecuta en hilo separado
        // Notifica al usuario cuando inicia
    }
}
```

#### **`MapaManager.java` - Gestión del Mapa**
```java
// Manager que gestiona la integración con Google Maps
// Maneja el navegador JxBrowser y la comunicación JavaScript

public class MapaManager {
    
    private void inicializarNavegador() {
        // Inicializa JxBrowser con licencia
        // Configura motor de renderizado acelerado por hardware
        // Carga el servidor web local (map.html)
    }
    
    public void cargarMapa(List<Aeropuerto> aeropuertos, List<Vuelo> vuelos) {
        // Convierte datos a JSON
        // Ejecuta JavaScript para actualizar el mapa
        // Maneja sincronización con el estado del mapa
        // Espera a que el mapa esté listo antes de dibujar
    }
    
    private String convertirAeropuertosAJson(List<Aeropuerto> aeropuertos) {
        // Convierte lista de aeropuertos a formato JSON
        // Incluye: código, nombre, ciudad, país, latitud, longitud
        // Escapa caracteres especiales para JSON válido
    }
    
    private String convertirVuelosAJson(List<Vuelo> vuelos) {
        // Convierte lista de vuelos a formato JSON
        // Incluye: origen, destino, aerolínea, distancia, duración, costo, hora
        // Formato compatible con Google Maps
    }
    
    public void recargarMapa() {
        // Recarga completamente el mapa
        // Útil para limpiar estado o aplicar cambios
    }
    
    public void cerrar() {
        // Cierra el navegador y libera recursos
        // Importante para evitar memory leaks
    }
}
```

### **3. LÓGICA DE NEGOCIO**

#### **`RedVuelosManager.java` - Orquestador de Lógica**
```java
// Clase que coordina toda la lógica de negocio
// Integra el grafo con la interfaz de usuario

public class RedVuelosManager {
    
    public GrafoAeropuertos.RutaOptima encontrarRutaOptima(String origen, String destino, MetricaPeso metrica) {
        // Implementa algoritmo de Dijkstra
        // Busca ruta más corta según métrica especificada
        // Retorna ruta completa con aeropuertos y vuelos
        // Maneja casos de aeropuertos no conectados
    }
    
    public List<String> buscarRutaBFS(String origen, String destino) {
        // Implementa búsqueda en anchura
        // Encuentra ruta con menor número de escalas
        // Retorna secuencia de códigos de aeropuertos
    }
    
    public List<String> buscarRutaDFS(String origen, String destino) {
        // Implementa búsqueda en profundidad
        // Explora rutas alternativas
        // Útil para análisis de conectividad
    }
    
    public void ejecutarDemo() {
        // Ejecuta demostración completa del sistema
        // Muestra todas las funcionalidades
        // Genera datos de ejemplo
        // Ejecuta búsquedas de prueba
    }
}
```

### **4. ESTRUCTURAS DE DATOS**

#### **`GrafoAeropuertos.java` - Implementación del Grafo**
```java
// Implementa grafo dirigido y ponderado usando listas de adyacencia
// Algoritmos: Dijkstra, BFS, DFS, análisis de conectividad

public class GrafoAeropuertos {
    
    // ESTRUCTURA INTERNA:
    // - Map<String, List<Arista>>: Lista de adyacencia
    // - Cada aeropuerto tiene lista de vuelos salientes
    // - Aristas contienen información del vuelo (peso, destino)
    
    public RutaOptima encontrarRutaOptima(String origen, String destino, MetricaPeso metrica) {
        // Algoritmo de Dijkstra optimizado
        // Usa PriorityQueue para eficiencia
        // Reconstruye ruta completa
        // Calcula peso total según métrica
    }
    
    public List<String> buscarRutaBFS(String origen, String destino) {
        // BFS con cola y mapa de predecesores
        // Encuentra ruta con menos escalas
        // Reconstruye camino desde destino
    }
    
    public List<String> buscarRutaDFS(String origen, String destino) {
        // DFS recursivo con backtracking
        // Explora todas las rutas posibles
        // Retorna primera ruta encontrada
    }
    
    public Map<String, Integer> obtenerEstadisticasConectividad() {
        // Calcula conexiones salientes por aeropuerto
        // Útil para análisis de centralidad
        // Retorna mapa aeropuerto → número de conexiones
    }
    
    public String obtenerAeropuertoMasConectado() {
        // Encuentra aeropuerto con más vuelos salientes
        // Análisis de centralidad de la red
    }
    
    public String obtenerAeropuertoMenosConectado() {
        // Encuentra aeropuerto con menos vuelos salientes
        // Identifica puntos débiles de la red
    }
}
```

#### **`Aeropuerto.java` - Entidad de Dominio**
```java
// POJO que representa un aeropuerto en el sistema
// Contiene toda la información geográfica y administrativa

public class Aeropuerto {
    private String codigo;        // Código IATA (3-4 letras)
    private String nombre;        // Nombre completo del aeropuerto
    private String ciudad;        // Ciudad donde se ubica
    private String pais;          // País de ubicación
    private double latitud;       // Coordenada geográfica
    private double longitud;      // Coordenada geográfica
    
    // Métodos getter/setter estándar
    // toString() para representación legible
    // equals() y hashCode() para comparaciones
}
```

#### **`Vuelo.java` - Entidad de Dominio**
```java
// POJO que representa un vuelo en el sistema
// Define conexión entre dos aeropuertos

public class Vuelo {
    private String id;                    // Identificador único del vuelo
    private String origenCodigo;          // Código IATA del aeropuerto origen
    private String destinoCodigo;         // Código IATA del aeropuerto destino
    private String aerolineaCodigo;       // Código de la aerolínea
    private double distanciaKm;           // Distancia en kilómetros
    private int duracionMin;              // Duración en minutos
    private double costoUsd;              // Costo en dólares
    private String horaSalida;            // Hora de salida (HH:MM)
    
    // Métodos getter/setter estándar
    // toString() para representación legible
    // equals() y hashCode() para comparaciones
}
```

### **5. PERSISTENCIA Y CONFIGURACIÓN**

#### **`Persistencia.java` - Capa de Persistencia**
```java
// Clase que maneja toda la persistencia de datos
// Integra con Firebase Firestore

public class Persistencia {
    
    // AEROPUERTOS:
    public static void guardarAeropuerto(Aeropuerto aeropuerto)
    public static Aeropuerto buscarAeropuertoPorCodigo(String codigo)
    public static List<Aeropuerto> cargarAeropuertos()
    public static void eliminarAeropuerto(String codigo)
    public static boolean aeropuertoExiste(String codigo)
    
    // VUELOS:
    public static void guardarVuelo(Vuelo vuelo)
    public static Vuelo buscarVueloPorId(String id)
    public static List<Vuelo> cargarVuelos()
    public static void eliminarVuelo(String id)
    
    // MÉTODOS AUXILIARES:
    private static String generarIdUnico() // Genera IDs únicos para vuelos
    private static void inicializarFirebase() // Configura conexión Firebase
}
```

#### **`FirebaseConfig.java` - Configuración de Firebase**
```java
// Clase que maneja la configuración y conexión con Firebase
// Inicializa el SDK de Firebase Admin

public class FirebaseConfig {
    
    public static void initFirebase() throws IOException {
        // Lee archivo de credenciales (serviceAccountKey.json)
        // Configura credenciales de Firebase Admin
        // Inicializa Firestore
        // Configura timeouts y reintentos
    }
    
    private static FileInputStream getServiceAccountKey() {
        // Busca archivo de credenciales en el classpath
        // Maneja errores de archivo no encontrado
    }
}
```

#### **`WebServer.java` - Servidor Web Local**
```java
// Servidor HTTP simple que sirve el archivo map.html
// Permite que JxBrowser cargue el mapa desde localhost

public class WebServer {
    
    public static void start() throws IOException {
        // Inicia servidor HTTP en puerto 8080
        // Sirve archivo map.html desde recursos
        // Configura MIME types para HTML y JavaScript
        // Maneja requests GET para el mapa
    }
    
    public static void stop() {
        // Detiene el servidor web
        // Libera recursos del puerto
    }
}
```

### **6. INTERFAZ WEB (map.html)**
```html
<!-- Archivo HTML que contiene Google Maps y lógica JavaScript -->
<!-- Se sirve desde WebServer local -->

<script>
// FUNCIONES PRINCIPALES:
function initMap() {
    // Inicializa Google Maps
    // Configura centro y zoom inicial
    // Prepara funciones de dibujo
}

function updateMapData(aeropuertos, vuelos) {
    // Actualiza el mapa con nuevos datos
    // Limpia marcadores y rutas existentes
    // Dibuja aeropuertos como marcadores
    // Dibuja vuelos como líneas entre aeropuertos
}

function drawAirports(aeropuertos) {
    // Crea marcadores para cada aeropuerto
    // Configura info windows con detalles
    // Aplica estilos personalizados
}

function drawFlights(vuelos) {
    // Dibuja líneas entre aeropuertos conectados
    // Aplica colores según aerolínea
    // Añade tooltips con información del vuelo
}

// FUNCIONES AUXILIARES:
function waitMapReady(callback) {
    // Espera a que el mapa esté completamente cargado
    // Ejecuta callback cuando esté listo
}

function escapeHtml(text) {
    // Escapa caracteres HTML para seguridad
    // Previene XSS en info windows
}
</script>
```

## 🔧 **INSTALACIÓN Y CONFIGURACIÓN**

### **Requisitos del Sistema**
- **Java**: Versión 17 o superior
- **Maven**: Versión 3.6+ para gestión de dependencias
- **Firebase**: Proyecto configurado con Firestore habilitado
- **Internet**: Conexión para Google Maps y Firebase

### **Configuración de Firebase**
1. Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Habilitar Firestore Database
3. Generar clave de servicio (serviceAccountKey.json)
4. Colocar archivo en `src/main/resources/`

### **Dependencias Maven**
```xml
<!-- JavaFX para interfaz gráfica -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>22.0.1</version>
</dependency>

<!-- Firebase Admin para persistencia -->
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>

<!-- JxBrowser para integración web -->
<dependency>
    <groupId>com.teamdev.jxbrowser</groupId>
    <artifactId>jxbrowser-win64</artifactId>
    <version>8.11.0</version>
</dependency>
```

## 🚀 **USO DE LA APLICACIÓN**

### **1. Inicio y Configuración**
- La aplicación inicia automáticamente Firebase y servidor web
- Carga datos existentes desde Firestore
- Inicializa Google Maps integrado

### **2. Gestión de Aeropuertos**
- **Agregar**: Formulario con validaciones y coordenadas geográficas
- **Editar**: Búsqueda por código IATA y modificación de datos
- **Eliminar**: Confirmación antes de eliminar con verificación de dependencias

### **3. Gestión de Vuelos**
- **Agregar**: Formulario con validación de aeropuertos existentes
- **Editar**: Modificación de vuelos existentes con validaciones
- **Eliminar**: Eliminación segura con confirmación
- **Listar**: Vista completa de todos los vuelos con acciones

### **4. Búsqueda de Rutas**
- **Dijkstra**: Ruta óptima según métrica (distancia, tiempo, costo)
- **BFS**: Ruta con menor número de escalas
- **DFS**: Exploración de rutas alternativas

### **5. Estadísticas y Análisis**
- **Conectividad**: Análisis de conexiones por aeropuerto
- **Centralidad**: Identificación de aeropuertos más/menos conectados
- **Demo**: Funcionalidad de demostración automática


## 🏆 **LOGROS TÉCNICOS**

### **Arquitectura**
- **Separación de Responsabilidades**: Cada manager tiene una función específica
- **Patrón Factory**: UIComponentFactory para componentes reutilizables
- **Inyección de Dependencias**: Managers se pasan entre componentes
- **Manejo de Estado**: Coordinación centralizada en Mapa.java

### **Performance**
- **Algoritmos Optimizados**: Dijkstra con PriorityQueue, BFS/DFS eficientes
- **Carga Asíncrona**: Estadísticas y demo en hilos separados
- **Lazy Loading**: Datos se cargan solo cuando se necesitan
- **Caching**: Datos se mantienen en memoria para operaciones repetidas

### **Calidad de Código**
- **Principios SOLID**: Aplicados consistentemente
- **Manejo de Errores**: Try-catch en todas las operaciones críticas
- **Validaciones**: Verificación de datos en todos los formularios
- **Logging**: Sistema de logs para debugging y monitoreo

## 📝 **CONCLUSIÓN**

AeroGraph representa una implementación completa y profesional de un sistema de gestión de vuelos, demostrando:

1. **Dominio Técnico**: Implementación correcta de estructuras de datos y algoritmos
2. **Arquitectura Sólida**: Diseño modular y mantenible
3. **Interfaz Profesional**: UI moderna y funcional
4. **Integración Avanzada**: Firebase, Google Maps, JxBrowser
5. **Documentación Completa**: Código bien documentado y explicado


-

