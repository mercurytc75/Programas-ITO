package Monopoly;

@FunctionalInterface
public interface PurchaseDecisionStrategy {
    boolean shouldBuy(Jugador jugador, PropiedadCasilla propiedadCasilla);
}
