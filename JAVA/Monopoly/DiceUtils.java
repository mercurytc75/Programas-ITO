package Monopoly;

import java.util.Random;

public final class DiceUtils {
    private static final Random RANDOM = new Random();

    private DiceUtils() {
    }

    public static int lanzarDosDados() {
        return RANDOM.nextInt(6) + 1 + RANDOM.nextInt(6) + 1;
    }

    public static DiceRoll lanzarTirada() {
        int dado1 = RANDOM.nextInt(6) + 1;
        int dado2 = RANDOM.nextInt(6) + 1;
        return new DiceRoll(dado1, dado2);
    }
}
