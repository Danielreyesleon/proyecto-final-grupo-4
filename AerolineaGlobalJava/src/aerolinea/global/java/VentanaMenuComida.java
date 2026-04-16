package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Menu a Bordo — UI dark mode, estilo aerolínea de lujo.
 *
 * @author Home
 */
public class VentanaMenuComida extends JFrame {

    //  PALETA DE COLORES
    private static final Color C_FONDO = new Color(10, 12, 20);   // negro azulado profundo
    private static final Color C_PANEL = new Color(18, 22, 36);   // panel oscuro
    private static final Color C_PANEL2 = new Color(24, 30, 50);   // panel secundario
    private static final Color C_BORDE = new Color(40, 50, 80);   // borde sutil
    private static final Color C_DORADO = new Color(212, 175, 95);   // dorado elegante
    private static final Color C_DORADO_CLARO = new Color(240, 210, 130);  // dorado hover
    private static final Color C_AZUL = new Color(60, 130, 230);  // azul acento
    private static final Color C_VERDE = new Color(50, 200, 120);  // confirmación
    private static final Color C_ROJO = new Color(220, 70, 70);   // quitar
    private static final Color C_TEXTO = new Color(220, 225, 240);  // texto principal
    private static final Color C_TEXTO_DIM = new Color(110, 120, 150);  // texto secundario
    private static final Color C_BADGE_BEBIDA = new Color(30, 80, 160);  // categoría bebida
    private static final Color C_BADGE_SNACK = new Color(100, 60, 160);  // categoría snack
    private static final Color C_BADGE_COMIDA = new Color(160, 80, 30);   // categoría comida
    private static final Color C_BADGE_PREM = new Color(150, 120, 20);   // categoría premium

    //  DATOS
    private final Asiento asiento;
    private final String nombrePasajero;

    private final List<ItemComida> menu = new ArrayList<>();
    private final Map<String, Integer> carrito = new LinkedHashMap<>();

    //  COMPONENTES UI
    private JPanel panelCarrito;
    private JLabel lblTotal;
    private JLabel lblEstado;
    private JLabel lblBadgeCarrito;

    //  CONSTRUCTOR
    public VentanaMenuComida(Asiento asiento, String nombrePasajero) {
        this.asiento = asiento;
        this.nombrePasajero = nombrePasajero;

        cargarMenu();
        construirUI();
        actualizarCarritoUI();

        setTitle("AeroGo  —  Menu a Bordo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_FONDO);
        setVisible(true);
    }

    //  LOGICA: CARGAR MENU
    private void cargarMenu() {
        menu.add(new ItemComida("B01", "Agua mineral", "Botella 500 ml", 2.50, null, "BEBIDA"));
        menu.add(new ItemComida("B02", "Jugo de naranja", "Vaso 250 ml, natural", 3.00, null, "BEBIDA"));
        menu.add(new ItemComida("B03", "Refresco", "Lata 355 ml", 2.00, null, "BEBIDA"));
        menu.add(new ItemComida("S01", "Snack de mani", "Bolsa 50 g", 2.00, null, "SNACK"));
        menu.add(new ItemComida("S02", "Galletas saladas", "Paquete surtido", 2.50, null, "SNACK"));
        menu.add(new ItemComida("C01", "Sandwich de pollo", "Pan integral, lechuga, tomate", 7.00, null, "COMIDA"));
        menu.add(new ItemComida("C02", "Wrap vegetariano", "Tortilla, hummus, vegetales", 6.50, null, "COMIDA"));
        menu.add(new ItemComida("E01", "Vino tinto", "Copa 150 ml, Merlot", 9.00, TipoClase.EJECUTIVA, "PREMIUM"));
        menu.add(new ItemComida("E02", "Cerveza artesanal", "Lata 355 ml", 6.00, TipoClase.EJECUTIVA, "PREMIUM"));
        menu.add(new ItemComida("E03", "Plato caliente", "Pollo o pasta + pan", 18.00, TipoClase.EJECUTIVA, "PREMIUM"));
        menu.add(new ItemComida("P01", "Champagne", "Copa 150 ml, Brut", 14.00, TipoClase.PRIMERA, "PRIMERA"));
        menu.add(new ItemComida("P02", "Menu gourmet", "3 tiempos + maridaje", 35.00, TipoClase.PRIMERA, "PRIMERA"));
    }

    //  CONSTRUCCION DE LA UI
    private void construirUI() {
        setLayout(new BorderLayout(0, 0));
        add(panelNorte(), BorderLayout.NORTH);
        add(panelCentro(), BorderLayout.CENTER);
        add(panelSur(), BorderLayout.SOUTH);
    }

    //  NORTE: barra superior estilo cabina de primera clase
    private JPanel panelNorte() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // fondo degradado
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(8, 10, 24),
                        getWidth(), 0, new Color(20, 28, 55));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // linea dorada inferior
                g2.setColor(C_DORADO);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));
        p.setPreferredSize(new Dimension(0, 72));

        // -- Izquierda: logo + título --
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izq.setOpaque(false);

        JLabel logo = new JLabel("AEROGO");
        logo.setFont(new Font("Monospaced", Font.BOLD, 13));
        logo.setForeground(C_DORADO);
        logo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_DORADO, 1),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));

        JLabel sep = new JLabel("   //   ");
        sep.setFont(new Font("Dialog", Font.PLAIN, 16));
        sep.setForeground(C_BORDE);

        JLabel titulo = new JLabel("MENU A BORDO");
        titulo.setFont(new Font("Dialog", Font.BOLD, 20));
        titulo.setForeground(C_TEXTO);

        izq.add(logo);
        izq.add(sep);
        izq.add(titulo);

        // -- Derecha: datos del pasajero en chips --
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        der.setOpaque(false);
        char col = (char) ('A' + asiento.getColumna());
        der.add(chip("PASAJERO", nombrePasajero.toUpperCase()));
        der.add(chip("ASIENTO", asiento.getFila() + "" + col));
        der.add(chip("CLASE", asiento.getClase().toString().toUpperCase()));

        p.add(izq, BorderLayout.WEST);
        p.add(der, BorderLayout.EAST);
        return p;
    }

    // chip de info del header
    private JPanel chip(String etiqueta, String valor) {
        JPanel c = new JPanel(new BorderLayout(0, 2));
        c.setOpaque(false);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDE, 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)));

        JLabel lEtiq = new JLabel(etiqueta);
        lEtiq.setFont(new Font("Monospaced", Font.PLAIN, 9));
        lEtiq.setForeground(C_DORADO);

        JLabel lVal = new JLabel(valor);
        lVal.setFont(new Font("Dialog", Font.BOLD, 12));
        lVal.setForeground(C_TEXTO);

        c.add(lEtiq, BorderLayout.NORTH);
        c.add(lVal, BorderLayout.CENTER);
        return c;
    }

    //  CENTRO: menu + carrito lado a lado
    private JPanel panelCentro() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(C_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        p.add(panelListaMenu(), BorderLayout.CENTER);
        p.add(panelLateralCarrito(), BorderLayout.EAST);
        return p;
    }

    // Lista de items del menu 
    private JPanel panelListaMenu() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setBackground(C_FONDO);

        // Subtítulo
        JLabel sub = new JLabel("  Selecciona tus items");
        sub.setFont(new Font("Dialog", Font.BOLD, 13));
        sub.setForeground(C_TEXTO_DIM);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        wrapper.add(sub, BorderLayout.NORTH);

        // Grid de tarjetas
        JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setBackground(C_FONDO);
        grid.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        String catActual = "";
        for (ItemComida item : menu) {
            if (!estaDisponible(item)) {
                continue;
            }
            if (!item.getEmoji().equals(catActual)) {
                catActual = item.getEmoji();
                if (!catActual.equals(menu.stream()
                        .filter(this::estaDisponible)
                        .findFirst()
                        .map(ItemComida::getEmoji).orElse(""))) {
                    grid.add(Box.createVerticalStrut(8));
                }
                grid.add(etiquetaCategoria(catActual));
                grid.add(Box.createVerticalStrut(6));
            }
            grid.add(tarjetaItem(item));
            grid.add(Box.createVerticalStrut(5));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(C_PANEL);

        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // etiqueta de categoría
    private JPanel etiquetaCategoria(String cat) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        Color color = switch (cat) {
            case "BEBIDA" ->
                C_BADGE_BEBIDA;
            case "SNACK" ->
                C_BADGE_SNACK;
            case "COMIDA" ->
                C_BADGE_COMIDA;
            default ->
                C_BADGE_PREM;
        };

        JLabel lbl = new JLabel("  " + cat + "  ");
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(color);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        p.add(lbl);
        return p;
    }

    // tarjeta de un ítem
    private JPanel tarjetaItem(ItemComida item) {
        Color[] estado = {C_PANEL};

        JPanel t = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(estado[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
            }
        };
        t.setOpaque(false);
        t.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // textos
        JPanel textos = new JPanel(new BorderLayout(0, 4));
        textos.setOpaque(false);

        JLabel nombre = new JLabel(item.getNombre());
        nombre.setFont(new Font("Dialog", Font.BOLD, 13));
        nombre.setForeground(C_TEXTO);

        JLabel desc = new JLabel(item.getDescripcion());
        desc.setFont(new Font("Dialog", Font.PLAIN, 11));
        desc.setForeground(C_TEXTO_DIM);

        textos.add(nombre, BorderLayout.NORTH);
        textos.add(desc, BorderLayout.CENTER);

        // precio + botón
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        der.setOpaque(false);

        JLabel precio = new JLabel(String.format("$%.2f", item.getPrecio()));
        precio.setFont(new Font("Dialog", Font.BOLD, 15));
        precio.setForeground(C_DORADO);

        JButton btn = boton("+ AGREGAR", C_VERDE);
        btn.addActionListener(e -> agregarItem(item));

        der.add(precio);
        der.add(btn);

        t.add(textos, BorderLayout.CENTER);
        t.add(der, BorderLayout.EAST);

        // hover
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                estado[0] = C_PANEL2;
                t.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                estado[0] = C_PANEL;
                t.repaint();
            }
        });

        return t;
    }

    //Panel lateral: carrito 
    private JPanel panelLateralCarrito() {
        JPanel lateral = new JPanel(new BorderLayout(0, 0));
        lateral.setPreferredSize(new Dimension(290, 0));
        lateral.setBackground(C_PANEL);
        lateral.setBorder(BorderFactory.createLineBorder(C_BORDE, 1));

        // Header del carrito
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(C_PANEL2);
        hdr.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDE),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        JLabel tit = new JLabel("MI PEDIDO");
        tit.setFont(new Font("Monospaced", Font.BOLD, 13));
        tit.setForeground(C_DORADO);

        lblBadgeCarrito = new JLabel("0");
        lblBadgeCarrito.setFont(new Font("Dialog", Font.BOLD, 11));
        lblBadgeCarrito.setForeground(Color.WHITE);
        lblBadgeCarrito.setBackground(C_AZUL);
        lblBadgeCarrito.setOpaque(true);
        lblBadgeCarrito.setHorizontalAlignment(SwingConstants.CENTER);
        lblBadgeCarrito.setPreferredSize(new Dimension(24, 20));
        lblBadgeCarrito.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        hdr.add(tit, BorderLayout.WEST);
        hdr.add(lblBadgeCarrito, BorderLayout.EAST);

        // Lista del carrito
        panelCarrito = new JPanel();
        panelCarrito.setLayout(new BoxLayout(panelCarrito, BoxLayout.Y_AXIS));
        panelCarrito.setBackground(C_PANEL);
        panelCarrito.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(panelCarrito);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(C_PANEL);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        // Footer con total y botón
        JPanel footer = new JPanel(new BorderLayout(0, 10));
        footer.setBackground(C_PANEL2);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDE),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        lblTotal = new JLabel("TOTAL:  $0.00");
        lblTotal.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblTotal.setForeground(C_DORADO_CLARO);
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnConfirmar = boton("CONFIRMAR PEDIDO", C_DORADO);
        btnConfirmar.setForeground(new Color(10, 12, 20));
        btnConfirmar.setFont(new Font("Dialog", Font.BOLD, 13));
        btnConfirmar.setPreferredSize(new Dimension(258, 40));
        btnConfirmar.addActionListener(e -> confirmarPedido());

        footer.add(lblTotal, BorderLayout.NORTH);
        footer.add(btnConfirmar, BorderLayout.SOUTH);

        lateral.add(hdr, BorderLayout.NORTH);
        lateral.add(scroll, BorderLayout.CENTER);
        lateral.add(footer, BorderLayout.SOUTH);
        return lateral;
    }

    //  SUR: barra de estado
    private JPanel panelSur() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 7));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDE));

        JLabel punto = new JLabel(">");
        punto.setFont(new Font("Monospaced", Font.BOLD, 12));
        punto.setForeground(C_DORADO);

        lblEstado = new JLabel("Selecciona un item para agregarlo al pedido.");
        lblEstado.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblEstado.setForeground(C_TEXTO_DIM);

        p.add(punto);
        p.add(lblEstado);
        return p;
    }

    //  LOGICA DE NEGOCIO
    private void agregarItem(ItemComida item) {
        carrito.merge(item.getCodigo(), 1, Integer::sum);
        actualizarCarritoUI();
        lblEstado.setText("\"" + item.getNombre() + "\" agregado al pedido.");
    }

    private void quitarItem(String codigo) {
        carrito.compute(codigo, (k, v) -> (v == null || v <= 1) ? null : v - 1);
        actualizarCarritoUI();
        lblEstado.setText("Item eliminado del pedido.");
    }

    private void confirmarPedido() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El carrito esta vacio. Agrega al menos un item.",
                    "Carrito vacio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        char col = (char) ('A' + asiento.getColumna());
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("           RESUMEN DE TU PEDIDO\n");
        sb.append("============================================\n");
        sb.append(String.format("  Pasajero : %s%n", nombrePasajero));
        sb.append(String.format("  Asiento  : %d-%c   Clase: %s%n",
                asiento.getFila(), col, asiento.getClase()));
        sb.append("--------------------------------------------\n");

        double total = 0;
        for (Map.Entry<String, Integer> e : carrito.entrySet()) {
            ItemComida item = buscarItem(e.getKey());
            if (item != null) {
                double sub = item.getPrecio() * e.getValue();
                total += sub;
                sb.append(String.format("  %dx %-24s $%6.2f%n",
                        e.getValue(), item.getNombre(), sub));
            }
        }
        sb.append("============================================\n");
        sb.append(String.format("  TOTAL                             $%6.2f%n", total));

        int op = JOptionPane.showConfirmDialog(this, sb.toString(),
                "Confirmar pedido", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (op == JOptionPane.OK_OPTION) {
            carrito.clear();
            actualizarCarritoUI();
            lblEstado.setText("Pedido confirmado. Buen provecho!");
            JOptionPane.showMessageDialog(this,
                    "Pedido realizado con exito!\nSe le servira en breve.",
                    "Pedido confirmado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Actualiza el panel del carrito 
    private void actualizarCarritoUI() {
        panelCarrito.removeAll();

        int totalItems = carrito.values().stream().mapToInt(Integer::intValue).sum();
        lblBadgeCarrito.setText(String.valueOf(totalItems));

        if (carrito.isEmpty()) {
            // Estado vacío
            JPanel vacio = new JPanel(new GridBagLayout());
            vacio.setOpaque(false);
            GridBagConstraints g = new GridBagConstraints();
            g.gridy = 0;
            JLabel ico = new JLabel("[ vacio ]");
            ico.setFont(new Font("Monospaced", Font.BOLD, 14));
            ico.setForeground(C_BORDE);
            vacio.add(ico, g);
            g.gridy = 1;
            g.insets = new Insets(6, 0, 0, 0);
            JLabel msg = new JLabel("Agrega items al pedido");
            msg.setFont(new Font("Dialog", Font.PLAIN, 11));
            msg.setForeground(C_TEXTO_DIM);
            vacio.add(msg, g);
            panelCarrito.add(vacio);
            lblTotal.setText("TOTAL:  $0.00");

        } else {
            double total = 0;
            for (Map.Entry<String, Integer> e : carrito.entrySet()) {
                ItemComida item = buscarItem(e.getKey());
                if (item == null) {
                    continue;
                }

                double sub = item.getPrecio() * e.getValue();
                total += sub;

                JPanel fila = new JPanel(new BorderLayout(8, 0));
                fila.setOpaque(false);
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0,
                                new Color(40, 50, 80, 100)),
                        BorderFactory.createEmptyBorder(8, 4, 8, 4)));

                // Badge cantidad
                JLabel badge = new JLabel(String.valueOf(e.getValue()),
                        SwingConstants.CENTER);
                badge.setFont(new Font("Monospaced", Font.BOLD, 11));
                badge.setForeground(C_DORADO);
                badge.setBackground(new Color(40, 35, 10));
                badge.setOpaque(true);
                badge.setPreferredSize(new Dimension(26, 26));
                badge.setBorder(BorderFactory.createLineBorder(C_DORADO, 1));

                JLabel lNombre = new JLabel(item.getNombre());
                lNombre.setFont(new Font("Dialog", Font.PLAIN, 12));
                lNombre.setForeground(C_TEXTO);

                JPanel centro = new JPanel(new BorderLayout(8, 0));
                centro.setOpaque(false);
                centro.add(badge, BorderLayout.WEST);
                centro.add(lNombre, BorderLayout.CENTER);

                // precio + quitar
                JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
                der.setOpaque(false);

                JLabel lSub = new JLabel(String.format("$%.2f", sub));
                lSub.setFont(new Font("Monospaced", Font.BOLD, 12));
                lSub.setForeground(C_DORADO_CLARO);

                JButton btnQ = boton("-", C_ROJO);
                btnQ.setFont(new Font("Dialog", Font.BOLD, 13));
                btnQ.setPreferredSize(new Dimension(26, 26));
                btnQ.addActionListener(ev -> quitarItem(item.getCodigo()));

                der.add(lSub);
                der.add(btnQ);

                fila.add(centro, BorderLayout.CENTER);
                fila.add(der, BorderLayout.EAST);
                panelCarrito.add(fila);
                panelCarrito.add(Box.createVerticalStrut(2));
            }
            lblTotal.setText(String.format("TOTAL:  $%.2f", total));
        }

        panelCarrito.revalidate();
        panelCarrito.repaint();
    }

    //  UTILIDADES
    private boolean estaDisponible(ItemComida item) {
        if (item.getClaseMinima() == null) {
            return true;
        }
        TipoClase ca = asiento.getClase();
        TipoClase cm = item.getClaseMinima();
        if (cm == TipoClase.ECONOMICA) {
            return true;
        }
        if (cm == TipoClase.EJECUTIVA) {
            return ca == TipoClase.EJECUTIVA || ca == TipoClase.PRIMERA;
        }
        if (cm == TipoClase.PRIMERA) {
            return ca == TipoClase.PRIMERA;
        }
        return false;
    }

    private ItemComida buscarItem(String codigo) {
        return menu.stream()
                .filter(i -> i.getCodigo().equals(codigo))
                .findFirst().orElse(null);
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
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(color);
            }
        });
        return b;
    }

    //  CLASE INTERNA: ItemComida
    public static class ItemComida {

        private final String codigo;
        private final String nombre;
        private final String descripcion;
        private final double precio;
        private final TipoClase claseMinima;
        private final String emoji;

        public ItemComida(String codigo, String nombre, String descripcion,
                double precio, TipoClase claseMinima, String emoji) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.claseMinima = claseMinima;
            this.emoji = emoji;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public double getPrecio() {
            return precio;
        }

        public TipoClase getClaseMinima() {
            return claseMinima;
        }

        public String getEmoji() {
            return emoji;
        }
    }
}
