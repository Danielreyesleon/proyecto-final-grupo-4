package aerolinea.global.java;

/**
 * @author Usuario
 */
public class Asiento {
// datos asientos
    private int fila;
    private int columna;
    private TipoClase clase;
    private boolean ocupado;

    public Asiento(int fila, int columna, TipoClase clase) {
        this.fila = fila;
        this.columna = columna;
        this.clase = clase;
        this.ocupado = false;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void ocupar() {
        ocupado = true;
    }

    public int getFila() {
        return fila;
    }      // ← AGREGADO

    public int getColumna() {
        return columna;
    }  

    public TipoClase getClase() {
        return clase;
    }

    @Override
    public String toString() {
        if (ocupado) {
            return "[x]";
        }
        switch (clase) {
            case PRIMERA:
                return "[P]";
            case EJECUTIVA:
                return "[E]";
            default:
                return "[ ]";
        }
    }
}
