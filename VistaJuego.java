import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz gráfica básica del Blackjack.
 * Muy simple por ahora: muestra cartas como texto, saldo, apuesta y botones.
 */
public class VistaJuego extends JFrame {

    private JButton btnPedir;
    private JButton btnPlantarse;
    private JButton btnDoblar;
    private JButton btnDividir;
    private JButton btnNuevaRonda;
    private JButton btnApostar;

    private JTextField campoApuesta;
    private JLabel etiquetaSaldo;
    private JLabel etiquetaMensaje;

    private JPanel panelJugador;
    private JPanel panelCrupier;

    public VistaJuego() {
        setTitle("Blackjack - Proyecto POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Panel superior: crupier
        JPanel superior = new JPanel(new BorderLayout());
        superior.setBorder(BorderFactory.createTitledBorder("Crupier"));
        panelCrupier = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        superior.add(panelCrupier, BorderLayout.CENTER);
        add(superior, BorderLayout.NORTH);

        // Panel central: jugador
        JPanel central = new JPanel(new BorderLayout());
        central.setBorder(BorderFactory.createTitledBorder("Jugador"));
        panelJugador = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        central.add(panelJugador, BorderLayout.CENTER);
        add(central, BorderLayout.CENTER);

        // Panel inferior: controles y mensajes
        JPanel inferior = new JPanel(new BorderLayout(6, 6));
        add(inferior, BorderLayout.SOUTH);

        // Zona apuesta y saldo
        JPanel apuestaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apuestaPanel.add(new JLabel("Apuesta:"));
        campoApuesta = new JTextField("100", 6);
        apuestaPanel.add(campoApuesta);
        btnApostar = new JButton("Confirmar apuesta");
        apuestaPanel.add(btnApostar);

        etiquetaSaldo = new JLabel("Saldo: 1000");
        apuestaPanel.add(etiquetaSaldo);

        inferior.add(apuestaPanel, BorderLayout.NORTH);

        // Botones principales
        JPanel botones = new JPanel(new GridLayout(1, 5, 8, 8));
        btnPedir = new JButton("Pedir");
        btnPlantarse = new JButton("Plantarse");
        btnDoblar = new JButton("Doblar");
        btnDividir = new JButton("Dividir");
        btnNuevaRonda = new JButton("Nueva ronda");

        botones.add(btnPedir);
        botones.add(btnPlantarse);
        botones.add(btnDoblar);
        botones.add(btnDividir);
        botones.add(btnNuevaRonda);

        inferior.add(botones, BorderLayout.CENTER);

        // Mensaje
        etiquetaMensaje = new JLabel("Bienvenido. Confirma apuesta y presiona 'Nueva ronda' para comenzar.", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        inferior.add(etiquetaMensaje, BorderLayout.SOUTH);

        // Mostrar ventana
        setVisible(true);
    }

    /**
     * Vincula el listener (controlador) a los botones. Se usan comandos para identificar acciones.
     */
    public void agregarListeners(ActionListener listener) {
        btnPedir.setActionCommand("Pedir");
        btnPlantarse.setActionCommand("Plantarse");
        btnDoblar.setActionCommand("Doblar");
        btnDividir.setActionCommand("Dividir");
        btnNuevaRonda.setActionCommand("NuevaRonda");
        btnApostar.setActionCommand("Apostar");

        btnPedir.addActionListener(listener);
        btnPlantarse.addActionListener(listener);
        btnDoblar.addActionListener(listener);
        btnDividir.addActionListener(listener);
        btnNuevaRonda.addActionListener(listener);
        btnApostar.addActionListener(listener);
    }

    /**
     * Intenta parsear la apuesta que escribió el usuario; devuelve -1 si es inválida.
     */
    public int obtenerApuestaIngresada() {
        try {
            return Integer.parseInt(campoApuesta.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Actualiza todo en la vista según el estado del juego.
     */
    public void actualizarVista(EstadoJuego estado) {
        etiquetaSaldo.setText("Saldo: $" + estado.getSaldo());
        etiquetaMensaje.setText(estado.getMensaje());

        // Mostrar cartas del jugador
        panelJugador.removeAll();
        mostrarCartas(panelJugador, estado.getManoJugador());

        // Mostrar cartas del crupier
        panelCrupier.removeAll();
        mostrarCartas(panelCrupier, estado.getManoCrupier());

        revalidate();
        repaint();
    }

    /**
     * Dibuja las cartas como etiquetas simples. Buen punto para mejorar con imágenes más adelante.
     */
    private void mostrarCartas(JPanel panel, List<String> cartas) {
        for (String carta : cartas) {
            JLabel lbl = new JLabel(carta, SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(60, 80));
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 16));
            panel.add(lbl);
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
