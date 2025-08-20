package edespol.redvuelos.dominio;

public class Aeropuerto {
    private String codigo;
    private String nombre;
    private String ciudad;
    private String pais;
    private double latitud;
    private double longitud;

    public Aeropuerto() {
        this.codigo = null;
        this.nombre = null;
        this.ciudad = null;
        this.pais = null;           
        this.latitud = 0.0;
        this.longitud = 0.0;    
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Aeropuerto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }

    public String toStringSimmple() {
        return codigo + " - " + nombre + " - " + ciudad + " - " + pais;
    }
}

