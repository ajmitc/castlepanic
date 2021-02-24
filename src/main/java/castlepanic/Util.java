package castlepanic;

import java.util.Date;
import java.util.Random;

public class Util {
    private static final Random GEN = new Random(new Date().getTime());

    public static int randInt(int min, int max){
        return min + GEN.nextInt(max - min);
    }

    public static int randInt(int max){
        return randInt(0, max);
    }

    private Util(){}
}
