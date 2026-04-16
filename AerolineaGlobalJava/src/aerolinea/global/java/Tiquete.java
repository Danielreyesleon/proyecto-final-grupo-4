package aerolinea.global.java;

/**
 * @author Home
 */
public class Tiquete {

    private static int contadorNumero = 1;          // ← para generar número único

    private String numero;                         // ← AGREGADO
    private Pasajero pasajero;
    private Vuelo vuelo;                          // ← AGREGADO
    private int fila;
    private int columna;
    private double precio;

    // Constructor actualizado: recibe Vuelo para poder retornarlo
    public Tiquete(Pasajero pasajero, Vuelo vuelo, int fila, int columna, double precio) {
        this.numero = String.format("TK-%04d", contadorNumero++); // ← genera TK-0001, TK-0002…
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.fila = fila;
        this.columna = columna;
        this.precio = precio;
    }

    public String getNumero() {
        return numero;
    }    // ← AGREGADO

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }     // ← AGREGADO

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public double getPrecio() {
        return precio;
    }
}
