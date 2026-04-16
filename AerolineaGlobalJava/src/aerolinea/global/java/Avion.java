/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aerolinea.global.java;

/**
 *
 * @author Usuario
 */
public class Avion {
// datos de registro de avion

    private String matricula;
    private String modelo;
    private int filas;
    private int columnas;
    private Asiento[][] asientos;

    public Avion(String matricula, String modelo, int filas, int columnas) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.filas = filas;
        this.columnas = columnas;
        asientos = new Asiento[filas][columnas];
        inicializarAsientos();
    }

    public Asiento[] getAsientos(int Asiento) {
        return this.asientos[Asiento];
    }

    private void inicializarAsientos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                TipoClase clase;

                if (i < 2) {
                    clase = TipoClase.PRIMERA;
                } else if (i < 5) {
                    clase = TipoClase.EJECUTIVA;
                } else {
                    clase = TipoClase.ECONOMICA;
                }

                asientos[i][j] = new Asiento(i, j, clase);
            }

        }

    }
// si esta ocupado

    public double ocupacion() {
        int ocupados = 0;
        int total = filas * columnas;

        for (Asiento[] fila : asientos) {
            for (Asiento a : fila) {
                if (a.isOcupado()) {
                    ocupados++;
                }
            }
        }

        return (ocupados * 100.0) / total;
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    @Override
    public String toString() {
        return matricula + " - " + modelo;

    }

}
