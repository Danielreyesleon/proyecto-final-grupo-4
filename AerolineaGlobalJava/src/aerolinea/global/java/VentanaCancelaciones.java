package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Ventana para cancelar reservas y ver el historial de cancelaciones.
 */
public class VentanaCancelaciones extends JDialog {

    private final List<Reserva> reservas;
    private final Cancelacion cancelacion;

    private JTextField txtTiquete;
    private JTextField txtCedula;
    private DefaultTableModel modeloHistorial;

    public VentanaCancelaciones(List<Reserva> reservas, JFrame padre) {
        super(padre, "Cancelar Reserva", true);
        this.reservas = reservas;
        this.cancelacion = new Cancelacion(reservas);

        setSize(780, 540);
        setMinimumSize(new Dimension(680, 460));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.C_FONDO);
        setLayout(new BorderLayout(0, 0));

        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(8, 10, 24), getWidth(), 0, new Color(20, 28, 55)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(VentanaPrincipal.C_ROJO);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 60));
        p.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        JLabel titulo = new JLabel("CANCELACION DE RESERVAS");
        titulo.setFont(new Font("Dialog", Font.BOLD, 18));
        titulo.setForeground(VentanaPrincipal.C_TEXTO);

        JLabel sub = new JLabel("Cancela por numero de tiquete o por cedula del pasajero");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 11));
        sub.setForeground(new Color(200, 100, 100));

        JPanel izq = new JPanel(new GridLayout(2, 1, 0, 2));
        izq.setOpaque(false);
        izq.add(titulo);
        izq.add(sub);
        p.add(izq, BorderLayout.WEST);
        return p;
    }

    // ── Cuerpo: formularios arriba + historial abajo ──────────────
    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(VentanaPrincipal.C_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        p.add(panelFormularios(), BorderLayout.NORTH);
        p.add(panelHistorial(), BorderLayout.CENTER);
        return p;
    }

    // ── Formularios de cancelación ────────────────────────────────
    private JPanel panelFormularios() {
        JPanel p = new JPanel(new GridLayout(1, 2, 14, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 130));

        // -- Por tiquete --
        JPanel porTiquete = tarjeta();
        porTiquete.add(seccion("CANCELAR POR TIQUETE"));
        porTiquete.add(Box.createVerticalStrut(10));
        porTiquete.add(etiqueta("Numero de tiquete (ej: TK-0001)"));
        porTiquete.add(Box.createVerticalStrut(4));
        txtTiquete = campo();
        porTiquete.add(txtTiquete);
        porTiquete.add(Box.createVerticalStrut(10));
        JButton btnTiquete = boton("CANCELAR TIQUETE", VentanaPrincipal.C_ROJO);
        btnTiquete.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTiquete.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnTiquete.addActionListener(e -> cancelarPorTiquete());
        porTiquete.add(btnTiquete);

        // -- Por cedula --
        JPanel porCedula = tarjeta();
        porCedula.add(seccion("CANCELAR POR PASAJERO"));
        porCedula.add(Box.createVerticalStrut(10));
        porCedula.add(etiqueta("Cedula / ID del pasajero"));
        porCedula.add(Box.createVerticalStrut(4));
        txtCedula = campo();
        porCedula.add(txtCedula);
        porCedula.add(Box.createVerticalStrut(10));
        JButton btnCedula = boton("CANCELAR TODAS", new Color(180, 80, 20));
        btnCedula.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCedula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnCedula.addActionListener(e -> cancelarPorCedula());
        porCedula.add(btnCedula);

        p.add(porTiquete);
        p.add(porCedula);
        return p;
    }

    // ── Historial de cancelaciones ────────────────────────────────
    private JPanel panelHistorial() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);

        JLabel lbl = new JLabel("HISTORIAL DE CANCELACIONES");
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(VentanaPrincipal.C_DORADO);
        p.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Tiquete", "Pasajero", "Vuelo", "Asiento", "Precio", "Estado"};
        modeloHistorial = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloHistorial);
        tabla.setBackground(VentanaPrincipal.C_PANEL);
        tabla.setForeground(VentanaPrincipal.C_TEXTO);
        tabla.setFont(new Font("Dialog", Font.PLAIN, 12));
        tabla.setRowHeight(30);
        tabla.setGridColor(VentanaPrincipal.C_BORDE);
        tabla.setSelectionBackground(VentanaPrincipal.C_PANEL2);
        tabla.setSelectionForeground(VentanaPrincipal.C_DORADO);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setBackground(VentanaPrincipal.C_PANEL2);
        tabla.getTableHeader().setForeground(VentanaPrincipal.C_DORADO);
        tabla.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 11));

        // Estado siempre en rojo
        tabla.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setFont(new Font("Dialog", Font.BOLD, 11));
                setForeground(VentanaPrincipal.C_ROJO);
                setBackground(sel ? VentanaPrincipal.C_PANEL2 : VentanaPrincipal.C_PANEL);
                setHorizontalAlignment(SwingConstants.CENTER);
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
        JLabel lbl = new JLabel("> Las cancelaciones son permanentes y no se pueden revertir.");
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setForeground(new Color(180, 80, 80));
        p.add(lbl);
        return p;
    }

    // ── Lógica ────────────────────────────────────────────────────
    private void cancelarPorTiquete() {
        String num = txtTiquete.getText().trim();
        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa el numero de tiquete.",
                    "Campo vacio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = cancelacion.cancelarPorTiquete(num);
        if (ok) {
            agregarAlHistorial(cancelacion.getHistorialCancelaciones()
                    .get(cancelacion.getHistorialCancelaciones().size() - 1));
            txtTiquete.setText("");
        }
    }

    private void cancelarPorCedula() {
        String id = txtCedula.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa la cedula del pasajero.",
                    "Campo vacio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ant = cancelacion.getHistorialCancelaciones().size();
        cancelacion.cancelarPorPasajero(id);
        List<Reserva> hist = cancelacion.getHistorialCancelaciones();
        for (int i = ant; i < hist.size(); i++) {
            agregarAlHistorial(hist.get(i));
        }
        txtCedula.setText("");
    }

    private void agregarAlHistorial(Reserva r) {
        Tiquete t = r.getTiquete();
        char col = (char) ('A' + t.getColumna());
        modeloHistorial.addRow(new Object[]{
            t.getNumero(),
            t.getPasajero().getNombre(),
            t.getVuelo().getCodigo(),
            t.getFila() + "" + col,
            String.format("$%.2f", t.getPrecio()),
            r.getEstado()
        });
    }

    // ── Helpers UI ────────────────────────────────────────────────
    private JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        return p;
    }

    private JLabel seccion(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Monospaced", Font.BOLD, 10));
        l.setForeground(VentanaPrincipal.C_DORADO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Dialog", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.C_TEXTO_DIM);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Monospaced", Font.PLAIN, 13));
        t.setBackground(VentanaPrincipal.C_PANEL2);
        t.setForeground(VentanaPrincipal.C_TEXTO);
        t.setCaretColor(VentanaPrincipal.C_DORADO);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(color);
            }
        });
        return b;
    }
}
