package aerolinea.global.java;

/**
 * @author Usuario
 */
public class Vuelo {

    private String codigo;       // ← AGREGADO
    private Avion avion;        // ← AGREGADO (referencia al avión real)
    private double precioBase;

    // Constructor actualizado
    public Vuelo(String codigo, Avion avion, double precioBase) {
        this.codigo = codigo;
        this.avion = avion;
        this.precioBase = precioBase;
    }

    // Constructor de compatibilidad con Main (filas/columnas simples)
    public Vuelo(int filas, int columnas, double precioBase) {
        this.codigo = "SIN-CODIGO";
        this.avion = new Avion("SIN-MATRICULA", "Generico", filas, columnas);
        this.precioBase = precioBase;
    }

    public String getCodigo() {
        return codigo;
    }      // ← AGREGADO

    public Avion getAvion() {
        return avion;
    }       // ← corregido: antes retornaba Object

    public boolean asientoDisponible(int fila, int columna) {
        return !avion.getAsientos()[fila][columna].isOcupado();
    }

    public void ocuparAsiento(int fila, int columna) {
        avion.getAsientos()[fila][columna].ocupar();
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public double calcularOcupacion() {
        return avion.ocupacion() / 100.0;
    }
}
