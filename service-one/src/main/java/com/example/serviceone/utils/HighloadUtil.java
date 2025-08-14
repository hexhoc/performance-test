package com.example.serviceone.utils;

import lombok.SneakyThrows;

import java.util.Random;

public class HighloadUtil {

    private static final Random random = new Random();

    public static void randomCase() {
        randomSleep();
        randomException();
    }

    @SneakyThrows
    private static void randomSleep() {
        if (random.nextDouble() < 0.05) {
            Thread.sleep(3_000);
        }
    }

    private static void randomException() {
        if (random.nextDouble() < 0.05) {
            throw new RuntimeException("Exception thrown with 5% chance");
        }
    }
}
