package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Ventana principal del sistema AeroGo. Reemplaza el menú de JOptionPane del
 * Main original.
 */
public class VentanaPrincipal extends JFrame {

    // ── Colores ───────────────────────────────────────────────────
    static final Color C_FONDO = new Color(10, 12, 20);
    static final Color C_PANEL = new Color(18, 22, 36);
    static final Color C_PANEL2 = new Color(24, 30, 50);
    static final Color C_BORDE = new Color(40, 50, 80);
    static final Color C_DORADO = new Color(212, 175, 95);
    static final Color C_DORADO_C = new Color(240, 210, 130);
    static final Color C_AZUL = new Color(60, 130, 230);
    static final Color C_VERDE = new Color(50, 200, 120);
    static final Color C_ROJO = new Color(220, 70, 70);
    static final Color C_TEXTO = new Color(220, 225, 240);
    static final Color C_TEXTO_DIM = new Color(110, 120, 150);

    // ── Estado global del sistema ─────────────────────────────────
    private final Avion avion;
    private final Vuelo vuelo;
    private final List<Reserva> reservas = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────
    public VentanaPrincipal() {
        avion = new Avion("AV-001", "Airbus A320", 10, 6);
        vuelo = new Vuelo("AG-345", avion, 200.0);

        setTitle("AeroGo — Sistema de Reservas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setMinimumSize(new Dimension(600, 460));
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_FONDO);
        setLayout(new BorderLayout());

        add(panelHeader(), BorderLayout.NORTH);
        add(panelMenu(), BorderLayout.CENTER);
        add(panelFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel panelHeader() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(8, 10, 24),
                        getWidth(), 0, new Color(20, 28, 55)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_DORADO);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 110));
        p.setBorder(BorderFactory.createEmptyBorder(20, 36, 20, 36));

        JLabel logo = new JLabel("AEROGO");
        logo.setFont(new Font("Monospaced", Font.BOLD, 14));
        logo.setForeground(C_DORADO);
        logo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_DORADO, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));

        JLabel subtitulo = new JLabel("SISTEMA DE RESERVAS AEREAS");
        subtitulo.setFont(new Font("Dialog", Font.BOLD, 22));
        subtitulo.setForeground(C_TEXTO);

        JLabel vuelo_lbl = new JLabel("Vuelo " + vuelo.getCodigo() + "  |  " + avion.toString());
        vuelo_lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        vuelo_lbl.setForeground(C_TEXTO_DIM);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(subtitulo);
        textos.add(vuelo_lbl);

        p.add(logo, BorderLayout.WEST);
        p.add(textos, BorderLayout.CENTER);
        textos.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        return p;
    }

    // ── Menú central con botones grandes ─────────────────────────
    private JPanel panelMenu() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(C_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 0, 10, 0);
        g.weightx = 1;
        g.gridx = 0;

        g.gridy = 0;
        p.add(botonMenu("COMPRAR TIQUETE",
                "Selecciona asiento, ingresa datos y reserva tu vuelo.",
                C_AZUL, e -> abrirCompra()), g);

        g.gridy = 1;
        p.add(botonMenu("VER RESERVAS",
                "Consulta todas las reservas activas del sistema.",
                C_VERDE, e -> abrirReservas()), g);

        g.gridy = 2;
        p.add(botonMenu("CANCELAR RESERVA",
                "Cancela una reserva por numero de tiquete o cedula.",
                C_ROJO, e -> abrirCancelaciones()), g);

        g.gridy = 3;
        p.add(botonMenu("SALIR",
                "Cerrar el sistema.",
                new Color(60, 65, 90), e -> System.exit(0)), g);

        return p;
    }

    // botón de menú con título y descripción
    private JPanel botonMenu(String titulo, String desc, Color color, ActionListener al) {
        JPanel card = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PANEL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDE, 1),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // barra de color izquierda
        JPanel barra = new JPanel();
        barra.setBackground(color);
        barra.setPreferredSize(new Dimension(5, 0));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setForeground(C_TEXTO);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblDesc.setForeground(C_TEXTO_DIM);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 3));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblDesc);

        JLabel flecha = new JLabel("  >");
        flecha.setFont(new Font("Monospaced", Font.BOLD, 18));
        flecha.setForeground(color);

        card.add(barra, BorderLayout.WEST);
        card.add(textos, BorderLayout.CENTER);
        card.add(flecha, BorderLayout.EAST);

        // hover
        Color[] bg = {C_PANEL};
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bg[0] = C_PANEL2;
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bg[0] = C_PANEL;
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                al.actionPerformed(null);
            }
        });

        return card;
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel panelFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDE));
        JLabel lbl = new JLabel("AeroGo  //  Sistema de Reservas v1.0");
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 10));
        lbl.setForeground(C_TEXTO_DIM);
        p.add(lbl);
        return p;
    }

    // ── Navegación ────────────────────────────────────────────────
    private void abrirCompra() {
        new VentanaCompra(vuelo, reservas, this);
    }

    private void abrirReservas() {
        new VentanaReservas(reservas, this);
    }

    private void abrirCancelaciones() {
        new VentanaCancelaciones(reservas, this);
    }

    // ── MAIN ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
