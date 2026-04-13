package Monopoly;

public class DiceRoll {
    private final int dado1;
    private final int dado2;

    public DiceRoll(int dado1, int dado2) {
        this.dado1 = dado1;
        this.dado2 = dado2;
    }

    public int getDado1() {
        return dado1;
    }

    public int getDado2() {
        return dado2;
    }

    public int getTotal() {
        return dado1 + dado2;
    }

    public boolean esDoble() {
        return dado1 == dado2;
    }
}
