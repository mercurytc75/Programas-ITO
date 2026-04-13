package engine;

import model.Jugador;
import model.casillas.PropiedadCasilla;

@FunctionalInterface
public interface PurchaseDecisionStrategy {
    boolean shouldBuy(Jugador jugador, PropiedadCasilla propiedadCasilla);
}
