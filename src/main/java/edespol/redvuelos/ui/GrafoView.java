package edespol.redvuelos.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edespol.redvuelos.dominio.Aeropuerto;
import edespol.redvuelos.grafo.AristaVuelo;
import edespol.redvuelos.grafo.GrafoAeropuertos;
import edespol.redvuelos.grafo.VerticeAeropuerto;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class GrafoView extends Pane {

    public enum LayoutMode {
        CIRCULAR,
        GEO
    }

    private final Group content;
    private final Group edgesLayer;
    private final Group nodesLayer;

    private GrafoAeropuertos grafo;
    private LayoutMode layoutMode;
    private final Map<String, NodeVisual> nodos;
    private java.util.List<String> rutaResaltada;

    // Pan & Zoom
    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging;

    // Callback al seleccionar nodo
    private java.util.function.Consumer<String> onAirportClick;

    public GrafoView() {
        this.content = new Group();
        this.edgesLayer = new Group();
        this.nodesLayer = new Group();
        this.content.getChildren().addAll(edgesLayer, nodesLayer);
        this.getChildren().add(content);

        this.layoutMode = LayoutMode.CIRCULAR;
        this.nodos = new HashMap<>();
        this.rutaResaltada = new ArrayList<>();

        enablePanAndZoom();
        setStyle("-fx-background-color: white;");
    }

    public void setGraph(GrafoAeropuertos g) {
        this.grafo = g;
        redraw();
    }

    public void setLayoutMode(LayoutMode mode) {
        this.layoutMode = mode;
        redraw();
    }

    public void setOnAirportClick(java.util.function.Consumer<String> cb) {
        this.onAirportClick = cb;
    }

    public void highlightPath(java.util.List<String> codes) {
        this.rutaResaltada = (codes == null) ? new ArrayList<>() : new ArrayList<>(codes);
        redrawEdgesOnly();
        highlightNodesInPath();
    }

    private void redraw() {
        nodesLayer.getChildren().clear();
        edgesLayer.getChildren().clear();
        nodos.clear();

        if (grafo == null || grafo.getVertices().isEmpty()) {
            return;
        }

        // Calcula posiciones
        Map<String, Point2D> pos = (layoutMode == LayoutMode.GEO)
                                   ? computeGeoLayout()
                                   : computeCircularLayout();

        // Crea nodos
        for (VerticeAeropuerto v : grafo.getVertices()) {
            Aeropuerto a = v.getDato();
            if (a == null || a.getCodigo() == null) {
                continue;
            }

            Point2D p = pos.get(a.getCodigo());
            if (p == null) {
                continue;
            }

            NodeVisual nv = createNodeVisual(a.getCodigo(), a.getNombre(), p.getX(), p.getY());
            nodos.put(a.getCodigo(), nv);
            nodesLayer.getChildren().add(nv.group);
        }

        // Crea aristas
        drawEdges();

        // Trae nodos al frente
        nodesLayer.toFront();
    }

    private void redrawEdgesOnly() {
        edgesLayer.getChildren().clear();
        if (grafo == null) {
            return;
        }
        drawEdges();
        nodesLayer.toFront();
    }

    private void drawEdges() {
        if (grafo == null) {
            return;
        }

        for (VerticeAeropuerto v : grafo.getVertices()) {
            Aeropuerto a = v.getDato();
            if (a == null || a.getCodigo() == null) {
                continue;
            }
            NodeVisual from = nodos.get(a.getCodigo());
            if (from == null) {
                continue;
            }

            LinkedList<AristaVuelo> sal = v.getSalientes();
            for (AristaVuelo e : sal) {
                NodeVisual to = nodos.get(e.getCodDestino());
                if (to == null) {
                    continue;
                }

                boolean onPath = isEdgeOnHighlightedPath(a.getCodigo(), e.getCodDestino());

                Line line = new Line(from.x, from.y, to.x, to.y);
                line.setStroke(onPath ? Color.ORANGE : Color.GRAY);
                line.setStrokeWidth(onPath ? 3.0 : 1.5);

                Polygon arrow = createArrowHead(from.x, from.y, to.x, to.y, onPath ? Color.ORANGE : Color.GRAY);

                edgesLayer.getChildren().addAll(line, arrow);
            }
        }
    }

    private boolean isEdgeOnHighlightedPath(String from, String to) {
        if (rutaResaltada == null || rutaResaltada.size() < 2) {
            return false;
        }
        for (int i = 0; i < rutaResaltada.size() - 1; i++) {
            if (rutaResaltada.get(i).equals(from) && rutaResaltada.get(i + 1).equals(to)) {
                return true;
            }
        }
        return false;
    }

    private void highlightNodesInPath() {
        for (NodeVisual nv : nodos.values()) {
            nv.circle.setFill(Color.web("#4c6ef5")); // azul por defecto
            nv.circle.setRadius(10);
            nv.label.setFill(Color.BLACK);
        }
        if (rutaResaltada == null) {
            return;
        }
        for (String code : rutaResaltada) {
            NodeVisual nv = nodos.get(code);
            if (nv != null) {
                nv.circle.setFill(Color.ORANGE);
                nv.circle.setRadius(11);
                nv.label.setFill(Color.DARKRED);
            }
        }
    }

    private NodeVisual createNodeVisual(String code, String name, double x, double y) {
        Circle c = new Circle();
        c.setCenterX(x);
        c.setCenterY(y);
        c.setRadius(10);
        c.setFill(Color.web("#4c6ef5"));
        c.setStroke(Color.WHITE);
        c.setStrokeWidth(1.5);

        Text t = new Text(code);
        t.setX(x + 12);
        t.setY(y + 4);
        t.setFill(Color.BLACK);

        Group g = new Group(c, t);

        g.setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY) {
                if (onAirportClick != null) {
                    onAirportClick.accept(code);
                }
            }
            evt.consume();
        });

        NodeVisual nv = new NodeVisual();
        nv.group = g;
        nv.circle = c;
        nv.label = t;
        nv.x = x;
        nv.y = y;
        nv.code = code;
        nv.name = name;
        return nv;
    }

    private Polygon createArrowHead(double x1, double y1, double x2, double y2, Color color) {
        double arrowLength = 12;
        double arrowWidth = 7;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double xTip = x2 - cos * 10; // que no pise el nodo destino
        double yTip = y2 - sin * 10;

        double xLeft = xTip - cos * arrowLength + -sin * arrowWidth;
        double yLeft = yTip - sin * arrowLength +  cos * arrowWidth;

        double xRight = xTip - cos * arrowLength +  sin * arrowWidth;
        double yRight = yTip - sin * arrowLength + -cos * arrowWidth;

        Polygon arrowHead = new Polygon(
            xTip, yTip,
            xLeft, yLeft,
            xRight, yRight
        );
        arrowHead.setFill(color);
        return arrowHead;
    }

    // ---------- Layouts ----------

    private Map<String, Point2D> computeCircularLayout() {
        Map<String, Point2D> pos = new HashMap<>();

        int n = grafo.getVertices().size();
        if (n == 0) {
            return pos;
        }

        double w = getWidth() > 0 ? getWidth() : 800;
        double h = getHeight() > 0 ? getHeight() : 600;

        double cx = w / 2.0;
        double cy = h / 2.0;
        double radius = Math.min(w, h) * 0.35;

        for (int i = 0; i < n; i++) {
            VerticeAeropuerto v = grafo.getVertices().get(i);
            Aeropuerto a = v.getDato();
            if (a == null || a.getCodigo() == null) {
                continue;
            }
            double theta = 2.0 * Math.PI * i / n;
            double x = cx + radius * Math.cos(theta);
            double y = cy + radius * Math.sin(theta);
            pos.put(a.getCodigo(), new Point2D(x, y));
        }
        return pos;
    }

    private Map<String, Point2D> computeGeoLayout() {
        Map<String, Point2D> pos = new HashMap<>();

        double w = getWidth() > 0 ? getWidth() : 800;
        double h = getHeight() > 0 ? getHeight() : 600;

        // Margen interno
        double pad = 40.0;

        // Encontrar min/max lat/lng
        double minLat = Double.POSITIVE_INFINITY;
        double maxLat = Double.NEGATIVE_INFINITY;
        double minLng = Double.POSITIVE_INFINITY;
        double maxLng = Double.NEGATIVE_INFINITY;

        for (VerticeAeropuerto v : grafo.getVertices()) {
            Aeropuerto a = v.getDato();
            if (a == null) {
                continue;
            }
            minLat = Math.min(minLat, a.getLatitud());
            maxLat = Math.max(maxLat, a.getLatitud());
            minLng = Math.min(minLng, a.getLongitud());
            maxLng = Math.max(maxLng, a.getLongitud());
        }

        double latRange = Math.max(1e-6, maxLat - minLat);
        double lngRange = Math.max(1e-6, maxLng - minLng);

        for (VerticeAeropuerto v : grafo.getVertices()) {
            Aeropuerto a = v.getDato();
            if (a == null || a.getCodigo() == null) {
                continue;
            }

            // Normalización simple (no Mercator exacto, pero suficiente)
            double nx = (a.getLongitud() - minLng) / lngRange;
            double ny = 1.0 - (a.getLatitud() - minLat) / latRange; // invertir Y para que norte arriba

            double x = pad + nx * (w - 2 * pad);
            double y = pad + ny * (h - 2 * pad);

            pos.put(a.getCodigo(), new Point2D(x, y));
        }

        return pos;
    }

    // ---------- Pan & Zoom ----------

    private void enablePanAndZoom() {
        // Zoom con rueda
        this.addEventFilter(ScrollEvent.SCROLL, e -> {
            double factor = (e.getDeltaY() > 0) ? 1.1 : 0.9;
            content.setScaleX(content.getScaleX() * factor);
            content.setScaleY(content.getScaleY() * factor);
            e.consume();
        });

        // Pan con arrastre
        this.setOnMousePressed(e -> {
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
            dragging = true;
        });

        this.setOnMouseDragged(e -> {
            if (!dragging) {
                return;
            }
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            content.setTranslateX(content.getTranslateX() + dx);
            content.setTranslateY(content.getTranslateY() + dy);
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        this.setOnMouseReleased(e -> {
            dragging = false;
        });
    }

    // ---------- Helper interno ----------

    private static class NodeVisual {
        Group group;
        Circle circle;
        Text label;
        double x;
        double y;
        String code;
        String name;
    }
}
