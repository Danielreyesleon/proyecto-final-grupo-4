package aerolinea.global.java;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada del sistema AeroGo.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
