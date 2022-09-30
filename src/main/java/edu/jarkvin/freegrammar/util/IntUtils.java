package edu.jarkvin.freegrammar.util;

import edu.jarkvin.freegrammar.exception.InvalidBoundException;

import java.util.Random;

public class IntUtils {
    private static final Random random = new Random();

    private IntUtils(){

    }

    //Devuelve un entero aleatorio de un rango
    public static int getRandomValue(int bound) {
        if(bound < 1){
            throw new InvalidBoundException("El rango debe ser positivo.");
        }
        return random.nextInt(bound);
    }
}
