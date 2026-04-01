package util;

import java.util.Random;

public final class DiceUtils {
    private static final Random RANDOM = new Random();

    private DiceUtils() {
    }

    public static int lanzarDosDados() {
        return RANDOM.nextInt(6) + 1 + RANDOM.nextInt(6) + 1;
    }
}
