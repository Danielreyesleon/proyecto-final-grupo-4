package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;

/**
 * Ventana de compra de tiquete con mapa visual de asientos.
 */
public class VentanaCompra extends JDialog {

    private final Vuelo vuelo;
    private final List<Reserva> reservas;

    // Formulario
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JComboBox<String> cmbNivel;

    // Asiento seleccionado
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private JLabel lblAsientoSel;
    private JLabel lblPrecioSel;
    private JButton btnComprar;

    // Botones del mapa
    private JButton[][] botonesAsientos;

    // ─────────────────────────────────────────────────────────────
    public VentanaCompra(Vuelo vuelo, List<Reserva> reservas, JFrame padre) {
        super(padre, "Comprar Tiquete", true);
        this.vuelo = vuelo;
        this.reservas = reservas;

        setSize(860, 620);
        setMinimumSize(new Dimension(780, 560));
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(VentanaPrincipal.C_FONDO);
        setLayout(new BorderLayout(0, 0));

        add(crearHeader(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel crearHeader() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(8, 10, 24),
                        getWidth(), 0, new Color(20, 28, 55)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(VentanaPrincipal.C_DORADO);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 60));
        p.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        JLabel titulo = new JLabel("COMPRA DE TIQUETE");
        titulo.setFont(new Font("Dialog", Font.BOLD, 18));
        titulo.setForeground(VentanaPrincipal.C_TEXTO);

        JLabel sub = new JLabel("Vuelo " + vuelo.getCodigo());
        sub.setFont(new Font("Monospaced", Font.PLAIN, 11));
        sub.setForeground(VentanaPrincipal.C_DORADO);

        JPanel izq = new JPanel(new GridLayout(2, 1, 0, 2));
        izq.setOpaque(false);
        izq.add(titulo);
        izq.add(sub);
        p.add(izq, BorderLayout.WEST);
        return p;
    }

    // ── Cuerpo: formulario izquierda + mapa derecha ───────────────
    private JPanel crearCuerpo() {
        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setBackground(VentanaPrincipal.C_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        p.add(crearFormulario(), BorderLayout.WEST);
        p.add(crearMapaAsientos(), BorderLayout.CENTER);
        return p;
    }

    // ── Formulario de datos ───────────────────────────────────────
    private JPanel crearFormulario() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        p.setPreferredSize(new Dimension(240, 0));

        p.add(seccion("DATOS DEL PASAJERO"));
        p.add(Box.createVerticalStrut(14));

        txtNombre = crearCampo();
        txtCedula = crearCampo();
        cmbNivel = new JComboBox<>(new String[]{"Regular", "Oro", "Platino"});
        estilizarCombo(cmbNivel);

        p.add(etiqueta("Nombre"));
        p.add(Box.createVerticalStrut(4));
        p.add(txtNombre);
        p.add(Box.createVerticalStrut(12));
        p.add(etiqueta("Cedula"));
        p.add(Box.createVerticalStrut(4));
        p.add(txtCedula);
        p.add(Box.createVerticalStrut(12));
        p.add(etiqueta("Nivel de pasajero"));
        p.add(Box.createVerticalStrut(4));
        p.add(cmbNivel);
        p.add(Box.createVerticalStrut(24));

        p.add(seccion("ASIENTO SELECCIONADO"));
        p.add(Box.createVerticalStrut(10));

        lblAsientoSel = new JLabel("Ninguno");
        lblAsientoSel.setFont(new Font("Monospaced", Font.BOLD, 15));
        lblAsientoSel.setForeground(VentanaPrincipal.C_DORADO);
        lblAsientoSel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPrecioSel = new JLabel("$0.00");
        lblPrecioSel.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblPrecioSel.setForeground(VentanaPrincipal.C_TEXTO_DIM);
        lblPrecioSel.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(lblAsientoSel);
        p.add(Box.createVerticalStrut(4));
        p.add(lblPrecioSel);
        p.add(Box.createVerticalGlue());

        btnComprar = crearBoton("CONFIRMAR COMPRA", VentanaPrincipal.C_VERDE);
        btnComprar.setEnabled(false);
        btnComprar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnComprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnComprar.addActionListener(e -> procesarCompra());
        p.add(btnComprar);

        return p;
    }

    // ── Mapa visual de asientos ───────────────────────────────────
    private JPanel crearMapaAsientos() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setBackground(VentanaPrincipal.C_FONDO);

        // Leyenda
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leyenda.setOpaque(false);
        leyenda.add(itemLeyenda("Primera", new Color(212, 175, 95)));
        leyenda.add(itemLeyenda("Ejecutiva", new Color(60, 130, 230)));
        leyenda.add(itemLeyenda("Economia", new Color(50, 150, 100)));
        leyenda.add(itemLeyenda("Ocupado", new Color(80, 30, 30)));
        wrapper.add(leyenda, BorderLayout.NORTH);

        // Grid de asientos
        Asiento[][] asientos = vuelo.getAvion().getAsientos();
        int filas = asientos.length;
        int columnas = asientos[0].length;

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(VentanaPrincipal.C_FONDO);
        botonesAsientos = new JButton[filas][columnas];

        // CORRECCIÓN: renombrado a 'gbc' para no chocar con el 'g' de paintComponent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);

        // Cabecera de columnas (A, B, C...)
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel vacio = new JLabel("");
        vacio.setPreferredSize(new Dimension(30, 20));
        grid.add(vacio, gbc);

        for (int c = 0; c < columnas; c++) {
            gbc.gridx = c + 1;
            JLabel lc = new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER);
            lc.setFont(new Font("Monospaced", Font.BOLD, 11));
            lc.setForeground(VentanaPrincipal.C_TEXTO_DIM);
            lc.setPreferredSize(new Dimension(46, 20));
            grid.add(lc, gbc);
        }

        // Filas de asientos
        for (int f = 0; f < filas; f++) {
            gbc.gridy = f + 1;
            gbc.gridx = 0;

            JLabel lf = new JLabel(String.valueOf(f), SwingConstants.CENTER);
            lf.setFont(new Font("Monospaced", Font.BOLD, 11));
            lf.setForeground(VentanaPrincipal.C_TEXTO_DIM);
            lf.setPreferredSize(new Dimension(30, 44));
            grid.add(lf, gbc);

            for (int c = 0; c < columnas; c++) {
                final int fi = f;
                final int ci = c;
                Asiento a = asientos[f][c];

                // CORRECCIÓN: clase anónima con paintComponent usa 'gr' en vez de 'g'
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics gr) {
                        Graphics2D g2 = (Graphics2D) gr.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                        g2.setColor(getForeground());
                        g2.setFont(getFont());
                        FontMetrics fm = g2.getFontMetrics();
                        String txt = getText();
                        g2.drawString(txt,
                                (getWidth() - fm.stringWidth(txt)) / 2,
                                (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                        g2.dispose();
                    }
                };

                btn.setText(f + "" + (char) ('A' + c));
                btn.setFont(new Font("Monospaced", Font.BOLD, 10));
                btn.setPreferredSize(new Dimension(46, 44));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                if (a.isOcupado()) {
                    btn.setBackground(new Color(80, 30, 30));
                    btn.setForeground(new Color(150, 80, 80));
                    btn.setEnabled(false);
                } else {
                    // CORRECCIÓN: switch con if-else para evitar problemas de versión
                    Color base;
                    if (a.getClase() == TipoClase.PRIMERA) {
                        base = new Color(80, 65, 20);
                    } else if (a.getClase() == TipoClase.EJECUTIVA) {
                        base = new Color(20, 50, 100);
                    } else {
                        base = new Color(20, 65, 45);
                    }
                    btn.setBackground(base);
                    btn.setForeground(VentanaPrincipal.C_TEXTO);
                    btn.addActionListener(e -> seleccionarAsiento(fi, ci, btn));
                }

                botonesAsientos[f][c] = btn;
                gbc.gridx = c + 1;
                grid.add(btn, gbc);
            }
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBackground(VentanaPrincipal.C_FONDO);
        scroll.getViewport().setBackground(VentanaPrincipal.C_FONDO);
        scroll.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1));
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel itemLeyenda(String texto, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel cuadro = new JLabel("  ");
        cuadro.setBackground(color);
        cuadro.setOpaque(true);
        cuadro.setPreferredSize(new Dimension(14, 14));
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        lbl.setForeground(VentanaPrincipal.C_TEXTO_DIM);
        p.add(cuadro);
        p.add(lbl);
        return p;
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel crearFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 7));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.C_BORDE));
        JLabel lbl = new JLabel("> Selecciona un asiento en el mapa y completa los datos.");
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setForeground(VentanaPrincipal.C_TEXTO_DIM);
        p.add(lbl);
        return p;
    }

    // ── Lógica ────────────────────────────────────────────────────
    private void seleccionarAsiento(int fila, int col, JButton btnSel) {
        // Desmarcar anterior
        if (filaSeleccionada >= 0) {
            Asiento ant = vuelo.getAvion().getAsientos()[filaSeleccionada][columnaSeleccionada];
            // CORRECCIÓN: if-else en vez de switch expression
            Color base;
            if (ant.getClase() == TipoClase.PRIMERA) {
                base = new Color(80, 65, 20);
            } else if (ant.getClase() == TipoClase.EJECUTIVA) {
                base = new Color(20, 50, 100);
            } else {
                base = new Color(20, 65, 45);
            }
            botonesAsientos[filaSeleccionada][columnaSeleccionada].setBackground(base);
            botonesAsientos[filaSeleccionada][columnaSeleccionada]
                    .setForeground(VentanaPrincipal.C_TEXTO);
        }

        filaSeleccionada = fila;
        columnaSeleccionada = col;
        btnSel.setBackground(VentanaPrincipal.C_DORADO);
        btnSel.setForeground(new Color(10, 12, 20));

        Asiento a = vuelo.getAvion().getAsientos()[fila][col];
        String nivel = (String) cmbNivel.getSelectedItem();
        double precio = Tarificador.calcularPrecio(vuelo, a,
                new Pasajero("tmp", "tmp", nivel));

        lblAsientoSel.setText(fila + "-" + (char) ('A' + col) + "  [" + a.getClase() + "]");
        lblPrecioSel.setText(String.format("Precio estimado: $%.2f", precio));
        btnComprar.setEnabled(true);
    }

    private void procesarCompra() {
        String nombre = txtNombre.getText().trim();
        String cedula = txtCedula.getText().trim();
        String nivel = (String) cmbNivel.getSelectedItem();

        if (nombre.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completa el nombre y la cedula antes de continuar.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un asiento en el mapa.",
                    "Sin asiento", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pasajero pasajero = new Pasajero(cedula, nombre, nivel);
        Reserva r = Ventas.comprarAsiento(vuelo, pasajero,
                filaSeleccionada, columnaSeleccionada);

        if (r != null) {
            reservas.add(r);
            botonesAsientos[filaSeleccionada][columnaSeleccionada]
                    .setBackground(new Color(80, 30, 30));
            botonesAsientos[filaSeleccionada][columnaSeleccionada]
                    .setForeground(new Color(150, 80, 80));
            botonesAsientos[filaSeleccionada][columnaSeleccionada].setEnabled(false);

            JOptionPane.showMessageDialog(this,
                    "Compra exitosa!\n"
                    + "Tiquete:  " + r.getTiquete().getNumero() + "\n"
                    + "Pasajero: " + nombre + "\n"
                    + "Asiento:  " + filaSeleccionada + "-"
                    + (char) ('A' + columnaSeleccionada) + "\n"
                    + String.format("Precio:   $%.2f", r.getTiquete().getPrecio()),
                    "Tiquete confirmado", JOptionPane.INFORMATION_MESSAGE);

            // Reset formulario
            txtNombre.setText("");
            txtCedula.setText("");
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            lblAsientoSel.setText("Ninguno");
            lblPrecioSel.setText("$0.00");
            btnComprar.setEnabled(false);

        } else {
            JOptionPane.showMessageDialog(this,
                    "El asiento ya fue ocupado. Selecciona otro.",
                    "Asiento no disponible", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers UI ────────────────────────────────────────────────
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

    private JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Dialog", Font.PLAIN, 13));
        t.setBackground(VentanaPrincipal.C_PANEL2);
        t.setForeground(VentanaPrincipal.C_TEXTO);
        t.setCaretColor(VentanaPrincipal.C_DORADO);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
    }

    private void estilizarCombo(JComboBox<String> cb) {
        cb.setBackground(VentanaPrincipal.C_PANEL2);
        cb.setForeground(VentanaPrincipal.C_TEXTO);
        cb.setFont(new Font("Dialog", Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.C_BORDE, 1));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(new Color(10, 12, 20));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(color.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(color);
                }
            }
        });
        return b;
    }
}
