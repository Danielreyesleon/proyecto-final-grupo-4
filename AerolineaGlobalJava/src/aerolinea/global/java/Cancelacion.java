package aerolinea.global.java;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * 
 */
public class Cancelacion {

    private List<Reserva> reservas;
    private List<Reserva> historialCancelaciones;

    public Cancelacion(List<Reserva> reservas) {
        this.reservas = reservas;
        this.historialCancelaciones = new ArrayList<>();
    }

    public boolean cancelarPorTiquete(String numeroTiquete) {
        for (Reserva reserva : reservas) {
            // CORRECCIÓN: se usa getNumero() que ahora existe en Tiquete
            if (reserva.getTiquete().getNumero().equalsIgnoreCase(numeroTiquete)) {

                if (reserva.getEstado().equalsIgnoreCase("Cancelada")) {
                    JOptionPane.showMessageDialog(null,
                            "La reserva con tiquete " + numeroTiquete + " ya está cancelada.",
                            "Reserva ya cancelada", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                reserva.cancelar();
                historialCancelaciones.add(reserva);

                JOptionPane.showMessageDialog(null,
                        "Reserva con tiquete " + numeroTiquete + " cancelada exitosamente.",
                        "Cancelación exitosa", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }

        JOptionPane.showMessageDialog(null,
                "No se encontró ninguna reserva con el tiquete: " + numeroTiquete,
                "Reserva no encontrada", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public int cancelarPorPasajero(String idPasajero) {
        int canceladas = 0;

        for (Reserva reserva : reservas) {
            Pasajero p = reserva.getTiquete().getPasajero();
            if (p != null && p.getId().equalsIgnoreCase(idPasajero)) {
                if (!reserva.getEstado().equalsIgnoreCase("Cancelada")) {
                    reserva.cancelar();
                    historialCancelaciones.add(reserva);
                    canceladas++;
                }
            }
        }

        if (canceladas > 0) {
            JOptionPane.showMessageDialog(null,
                    "Se cancelaron " + canceladas + " reserva(s) del pasajero con ID: " + idPasajero,
                    "Cancelaciones realizadas", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se encontraron reservas activas para el pasajero: " + idPasajero,
                    "Sin reservas activas", JOptionPane.WARNING_MESSAGE);
        }

        return canceladas;
    }

    public void mostrarHistorialCancelaciones() {
        if (historialCancelaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay cancelaciones registradas en esta sesión.",
                    "Historial vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("===== HISTORIAL DE CANCELACIONES =====\n");
        for (Reserva r : historialCancelaciones) {
            // CORRECCIÓN: getNumero(), getVuelo().getCodigo() y getNombre() ahora existen
            sb.append("- Tiquete: ").append(r.getTiquete().getNumero())
                    .append(" | Vuelo: ").append(r.getTiquete().getVuelo().getCodigo())
                    .append(" | Pasajero: ").append(r.getTiquete().getPasajero().getNombre())
                    .append(" | Estado: ").append(r.getEstado())
                    .append("\n");
        }
        sb.append("======================================");

        JOptionPane.showMessageDialog(null, sb.toString(),
                "Historial de Cancelaciones", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<Reserva> getHistorialCancelaciones() {
        return historialCancelaciones;
    }
}
