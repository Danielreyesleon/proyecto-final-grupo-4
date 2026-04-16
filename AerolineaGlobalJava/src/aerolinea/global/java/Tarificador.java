/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aerolinea.global.java;

/**
 *
 * @author Usuario
 */
public class Tarificador {
// se calcula el presio
    public static double calcularPrecio(Vuelo vuelo, Asiento asiento, Pasajero pasajero) {
        double precio = vuelo.getPrecioBase();
        switch (asiento.getClase()) {
            case PRIMERA:
                precio *= 2.0;
                break;
            case EJECUTIVA:
                precio *= 1.5;

                break;

        }

        if (vuelo.getAvion().ocupacion() > 80) {
            precio *= 1.20;
        }

        switch (pasajero.getNivel()) {
            case "PLATINO":
                precio *= 0.90;
                break;
            case "ORO":
                precio *= 0.95;
                break;
        }
        return precio;

    }
}
