import java.util.List;

/**
 * Contenedor simple para pasar información del modelo a la vista.
 * Evita exponer las estructuras internas del modelo directamente.
 */
public class EstadoJuego {

    private List<String> manoJugador;
    private List<String> manoCrupier;
    private int saldo;
    private String mensaje;
    private int apuesta;

    public EstadoJuego(List<String> manoJugador, List<String> manoCrupier, int saldo, String mensaje, int apuesta) {
        this.manoJugador = manoJugador;
        this.manoCrupier = manoCrupier;
        this.saldo = saldo;
        this.mensaje = mensaje;
        this.apuesta = apuesta;
    }

    public List<String> getManoJugador() {
        return manoJugador;
    }

    public List<String> getManoCrupier() {
        return manoCrupier;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getApuesta() {
        return apuesta;
    }
}
