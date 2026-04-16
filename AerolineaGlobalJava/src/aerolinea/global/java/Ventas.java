package aerolinea.global.java;

/**
 * @author Home
 */
public class Ventas {

    public static Reserva comprarAsiento(Vuelo vuelo, Pasajero pasajero,
            int fila, int columna) {
        if (!vuelo.asientoDisponible(fila, columna)) {
            return null;
        }

        double precio = vuelo.getPrecioBase();
        double ocupacion = vuelo.calcularOcupacion();

        if (ocupacion > 0.8) {
            precio *= 1.20;
        }
        if (pasajero.getNivel().equalsIgnoreCase("Platino")) {
            precio *= 0.90;
        } else if (pasajero.getNivel().equalsIgnoreCase("Oro")) {
            precio *= 0.95;
        }

        vuelo.ocuparAsiento(fila, columna);

        // Tiquete ahora recibe Vuelo para que Cancelacion pueda llamar getVuelo()
        Tiquete t = new Tiquete(pasajero, vuelo, fila, columna, precio);
        return new Reserva(t);
    }
}
