/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aerolinea.global.java;

/**
 *
 * @author Home
 */
public class Reserva {
// datos de reserva
    private Tiquete tiquete;
    private String estado;

    public Reserva(Tiquete tiquete) {
        this.tiquete = tiquete;
        this.estado = "Activa";
    }

    public Tiquete getTiquete() {
        return tiquete;
    }

    public String getEstado() {
        return estado;
    }

    public void cancelar() {
        estado = "Cancelada";
    }
}
