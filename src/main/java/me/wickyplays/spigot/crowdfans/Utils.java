package me.wickyplays.spigot.crowdfans;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static int randomInt(int a, int b) {
        return ThreadLocalRandom.current().nextInt(a, b);
    }

    public static double randomDouble(double a, double b) {
        return ThreadLocalRandom.current().nextDouble(a, b);
    }
}
