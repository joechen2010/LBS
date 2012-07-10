package ntu.sce.fyp.easilocation.util;

/**
 * Math utility functions.
 */
public final class MathUtils {
    private MathUtils() {}

    /**
     * Equivalent to Math.max(low, Math.min(high, amount));
     */
    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     * Equivalent to Math.max(low, Math.min(high, amount));
     */
    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}