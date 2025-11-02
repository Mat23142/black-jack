import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Lógica principal del Blackjack.
 * Maneja mazo, manos, apuestas y reglas básicas (incluye doblar).
 */
public class Juego {

    private List<String> mazo;
    private List<String> manoJugador;
    private List<String> manoCrupier;

    private int saldo;
    private int apuestaActual;
    private String mensajeActual;

    private boolean rondaTerminada;

    public Juego() {
        // saldo inicial razonable para pruebas
        this.saldo = 1000;
        this.apuestaActual = 100;
        iniciarRondaGUI();
    }

    /**
     * (Re)inicia una ronda: prepara mazo, reparte cartas y revisa blackjack inicial.
     * No cobra la apuesta hasta resolver el resultado.
     */
    public void iniciarRondaGUI() {
        mazo = generarMazo();
        manoJugador = new ArrayList<>();
        manoCrupier = new ArrayList<>();
        rondaTerminada = false;
        mensajeActual = "";

        // repartir dos cartas a cada uno
        manoJugador.add(sacarCarta());
        manoCrupier.add(sacarCarta());
        manoJugador.add(sacarCarta());
        manoCrupier.add(sacarCarta());

        // Verificar blackjack del jugador (3:2)
        if (calcularValor(manoJugador) == 21) {
            mensajeActual = "¡Blackjack! Ganas 3:2 sobre tu apuesta.";
            saldo += (int)(apuestaActual * 1.5);
            rondaTerminada = true;
        }
    }

    /**
     * Establece la apuesta actual (antes de iniciar la ronda).
     * Valida que la apuesta sea positiva y no supere el saldo.
     */
    public void setApuesta(int nuevaApuesta) {
        if (nuevaApuesta > 0 && nuevaApuesta <= saldo) {
            apuestaActual = nuevaApuesta;
            mensajeActual = "Apuesta establecida en $" + apuestaActual;
        } else {
            mensajeActual = "Apuesta inválida. Usa un número entre 1 y " + saldo;
        }
    }

    public int getApuesta() {
        return apuestaActual;
    }

    /**
     * El jugador pide carta.
     */
    public void jugadorPedir() {
        if (rondaTerminada) return;

        manoJugador.add(sacarCarta());
        int valor = calcularValor(manoJugador);

        if (valor > 21) {
            mensajeActual = "Te pasaste con " + valor + ". Pierdes $" + apuestaActual + ".";
            saldo -= apuestaActual;
            rondaTerminada = true;
        } else if (valor == 21) {
            // si llega a 21 automáticamente plantar y resolver
            jugadorPlantarse();
        } else {
            mensajeActual = "Has pedido. Tu mano ahora suma " + valor + ".";
        }
    }

    /**
     * El jugador se planta; el crupier juega con su regla (plantarse en 17+).
     * Luego se resuelve el resultado y se ajusta el saldo.
     */
    public void jugadorPlantarse() {
        if (rondaTerminada) return;

        // turno del crupier
        while (calcularValor(manoCrupier) < 17) {
            manoCrupier.add(sacarCarta());
        }

        int valorJugador = calcularValor(manoJugador);
        int valorCrupier = calcularValor(manoCrupier);

        if (valorCrupier > 21 || valorJugador > valorCrupier) {
            mensajeActual = "Ganaste la ronda. Ganas $" + apuestaActual + ".";
            saldo += apuestaActual;
        } else if (valorJugador == valorCrupier) {
            mensajeActual = "Empate. No se mueve tu saldo.";
            // saldo sin cambios
        } else {
            mensajeActual = "Perdiste la ronda. Pierdes $" + apuestaActual + ".";
            saldo -= apuestaActual;
        }

        rondaTerminada = true;
    }

    /**
     * Duplicar la apuesta: solo permite pedir una carta más y luego planta automáticamente.
     * Verifica que haya saldo suficiente para doblar.
     */
    public void jugadorDoblar() {
        if (rondaTerminada) return;

        // comprobar que hay saldo para doblar
        if (saldo < apuestaActual * 2) {
            mensajeActual = "No tienes saldo suficiente para doblar.";
            return;
        }

        // doblar: aumentamos la apuesta y pedir solo una carta
        apuestaActual *= 2;
        manoJugador.add(sacarCarta());
        int valor = calcularValor(manoJugador);

        if (valor > 21) {
            mensajeActual = "Te pasaste tras doblar con " + valor + ". Pierdes $" + apuestaActual + ".";
            saldo -= apuestaActual;
            rondaTerminada = true;
        } else {
            // luego del doblar, se considera como plantarse
            jugadorPlantarse();
        }
    }

    /**
     * División (split) no implementada en esta versión básica, dejamos un mensaje.
     */
    public void jugadorDividir() {
        mensajeActual = "Dividir aún no disponible en esta versión.";
    }

    public boolean rondaTerminada() {
        return rondaTerminada;
    }

    public String getResultadoRonda() {
        return mensajeActual;
    }

    /**
     * Devuelve un EstadoJuego con copias de las manos para mostrar en la vista.
     */
    public EstadoJuego getEstado() {
        return new EstadoJuego(
                new ArrayList<>(manoJugador),
                new ArrayList<>(manoCrupier),
                saldo,
                mensajeActual.isEmpty() ? "Tu turno..." : mensajeActual,
                apuestaActual
        );
    }

    // -------------------------
    // MÉTODOS AUXILIARES PRIVADOS
    // -------------------------

    private List<String> generarMazo() {
        String[] palos = {"♠", "♥", "♦", "♣"};
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        List<String> nuevoMazo = new ArrayList<>();

        for (String palo : palos) {
            for (String valor : valores) {
                nuevoMazo.add(valor + palo);
            }
        }

        Collections.shuffle(nuevoMazo, new Random());
        return nuevoMazo;
    }

    private String sacarCarta() {
        if (mazo.isEmpty()) {
            mazo = generarMazo(); // recargar si se acaba
        }
        return mazo.remove(0);
    }

    /**
     * Calcula el valor de una mano aplicando la regla de ases (11 o 1).
     */
    private int calcularValor(List<String> mano) {
        int total = 0;
        int ases = 0;

        for (String carta : mano) {
            String valor = carta.replaceAll("[♠♥♦♣]", "");
            switch (valor) {
                case "J": case "Q": case "K":
                    total += 10;
                    break;
                case "A":
                    total += 11;
                    ases++;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }
        }

        // ajustar ases si supera 21
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }
}
