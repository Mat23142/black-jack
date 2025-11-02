import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

/**
 * Controlador que conecta Vista y Modelo.
 * Aquí está el main para arrancar la aplicación.
 */
public class ControladorJuego implements ActionListener {

    private Juego modelo;
    private VistaJuego vista;

    public ControladorJuego(Juego modelo, VistaJuego vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vista.agregarListeners(this);
        // Mostrar estado inicial
        this.vista.actualizarVista(modelo.getEstado());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "Pedir":
                modelo.jugadorPedir();
                break;

            case "Plantarse":
                modelo.jugadorPlantarse();
                break;

            case "Doblar":
                modelo.jugadorDoblar();
                break;

            case "Dividir":
                modelo.jugadorDividir();
                break;

            case "Apostar":
                int nueva = vista.obtenerApuestaIngresada();
                if (nueva > 0) {
                    modelo.setApuesta(nueva);
                } else {
                    vista.mostrarMensaje("Apuesta inválida. Escribe un número menor o igual a tu saldo.");
                }
                break;

            case "NuevaRonda":
                // Antes de iniciar, nos aseguramos de que la apuesta sea válida
                modelo.iniciarRondaGUI();
                break;
        }

        // Actualizamos la vista con el estado resultante
        vista.actualizarVista(modelo.getEstado());

        // Si la ronda terminó, mostramos el resultado en un diálogo para que se note
        if (modelo.rondaTerminada()) {
            vista.mostrarMensaje(modelo.getResultadoRonda());
        }
    }

    public static void main(String[] args) {
        // Iniciar la GUI en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            Juego modelo = new Juego();
            VistaJuego vista = new VistaJuego();
            new ControladorJuego(modelo, vista);
        });
    }
}
