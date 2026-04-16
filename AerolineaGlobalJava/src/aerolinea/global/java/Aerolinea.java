/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aerolinea.global.java;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Aerolinea {
//dato aerolinea

    private String nombre;
    private ArrayList<Avion> aviones;
    private ArrayList<Vuelo> vuelos;

    public Aerolinea(String nombre) {
        this.nombre = nombre;
        aviones = new ArrayList<>();
        vuelos = new ArrayList<>();

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void agregarAvion(Avion a) {
        aviones.add(a);

    }

    public void agregarVuelo(Vuelo v) {
        vuelos.add(v);
    }

    public int getCantidadAviones() {
        return aviones.size();

    }

    public Avion seleccionarAvion() {
        String lista = "";
        for (int i = 0; i < aviones.size(); i++) {
            lista += ". " + aviones.get(i) + "\n";
        }

        int opcion = Integer.parseInt(JOptionPane.showInputDialog("seleccione un vuelo:\n" + lista));

        return aviones.get(opcion);

    }

    public Vuelo seleccionarVuelo() {

        if (vuelos.isEmpty()) {
            return null;
        }

        String lista = "";
        for (int i = 0; i < vuelos.size(); i++) {
            lista += i + ". " + vuelos.get(i) + "\n";
        }
        int opcion = Integer.parseInt(JOptionPane.showInputDialog("seleccione  un vuelo:\n" + lista));
        return vuelos.get(opcion);

    }

    public String generarMapaAsientos(Vuelo vuelo) {
        StringBuilder sb = new StringBuilder("Mapa del avion:\n");
        for (Asiento[] fila : vuelo.getAvion().getAsientos()) {
            for (Asiento a : fila) {
                sb.append(a.toString());
            }
            sb.append("\n");
        }
        return sb.toString();

    }

    public Asiento seleccionarAsiento(Vuelo vuelo) {
        JOptionPane.showConfirmDialog(null, generarMapaAsientos(vuelo));
        int fila = Integer.parseInt(JOptionPane.showInputDialog("Fila: "));
        int col = Integer.parseInt(JOptionPane.showInputDialog("columna: "));
        return vuelo.getAvion().getAsientos()[fila][col];

    }

}
