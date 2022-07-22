package com.dobiasjakub;

import java.util.Random;

public class Cube {
    private static int count;
    private static Random random;

    Cube() {
        count = 6;
        random = new Random();
    }

    public static int throwCube() {
        return random.nextInt(count)+1;
    }
}
