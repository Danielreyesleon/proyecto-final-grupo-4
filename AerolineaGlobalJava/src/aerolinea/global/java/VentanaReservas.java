package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Ventana que muestra todas las reservas registradas en una tabla.
 */
public class VentanaReservas extends JDialog {

    private final List<Reserva> reservas;
    private JTable tabla;
    private DefaultTableModel modelo;

    public VentanaReservas(List<Reserva> reservas, JFrame padre) {
        super(padre, "Ver Reservas", true);
        this.reservas = reservas;

        setSize(720, 460);
        setMinimumSize(new Dimension(620, 380));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.C_FONDO);
        setLayout(new BorderLayout(0, 0));

        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);

        cargarDatos();
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(8, 10, 24), getWidth(), 0, new Color(20, 28, 55)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(VentanaPrincipal.C_VERDE);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 60));
        p.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        JLabel titulo = new JLabel("RESERVAS ACTIVAS");
        titulo.setFont(new Font("Dialog", Font.BOLD, 18));
        titulo.setForeground(VentanaPrincipal.C_TEXTO);

        JLabel sub = new JLabel(reservas.size() + " reservas en el sistema");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 11));
        sub.setForeground(VentanaPrincipal.C_VERDE);

        JPanel izq = new JPanel(new GridLayout(2, 1, 0, 2));
        izq.setOpaque(false);
        izq.add(titulo);
        izq.add(sub);
        p.add(izq, BorderLayout.WEST);

        JButton btnRefrescar = boton("REFRESCAR", VentanaPrincipal.C_AZUL);
        btnRefrescar.addActionListener(e -> cargarDatos());
        p.add(btnRefrescar, BorderLayout.EAST);
        return p;
    }

    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VentanaPrincipal.C_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        String[] columnas = {"Tiquete", "Pasajero", "Cedula", "Vuelo",
            "Asiento", "Clase", "Precio", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(VentanaPrincipal.C_PANEL);
        tabla.setForeground(VentanaPrincipal.C_TEXTO);
        tabla.setFont(new Font("Dialog", Font.PLAIN, 12));
        tabla.setRowHeight(32);
        tabla.setGridColor(VentanaPrincipal.C_BORDE);
        tabla.setSelectionBackground(VentanaPrincipal.C_PANEL2);
        tabla.setSelectionForeground(VentanaPrincipal.C_DORADO);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setBackground(VentanaPrincipal.C_PANEL2);
        tabla.getTableHeader().setForeground(VentanaPrincipal.C_DORADO);
        tabla.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 11));
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, VentanaPrincipal.C_BORDE));

        // Renderer para colorear la columna Estado
        tabla.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Dialog", Font.BOLD, 11));
                String estado = val == null ? "" : val.toString();
                setForeground(estado.equalsIgnoreCase("Activa")
                        ? VentanaPrincipal.C_VERDE : VentanaPrincipal.C_ROJO);
                setBackground(sel ? VentanaPrincipal.C_PANEL2 : VentanaPrincipal.C_PANEL);
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(VentanaPrincipal.C_PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1));

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 7));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.C_BORDE));
        JLabel lbl = new JLabel("> Las reservas canceladas se muestran en rojo.");
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setForeground(VentanaPrincipal.C_TEXTO_DIM);
        p.add(lbl);
        return p;
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Reserva r : reservas) {
            Tiquete t = r.getTiquete();
            char col = (char) ('A' + t.getColumna());
            Asiento a = t.getVuelo().getAvion().getAsientos()[t.getFila()][t.getColumna()];
            modelo.addRow(new Object[]{
                t.getNumero(),
                t.getPasajero().getNombre(),
                t.getPasajero().getId(),
                t.getVuelo().getCodigo(),
                t.getFila() + "" + col,
                a.getClase().toString(),
                String.format("$%.2f", t.getPrecio()),
                r.getEstado()
            });
        }
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return b;
    }
}
