package com.example.rosaceae.Util;

import java.util.Random;

public class StringHanlder {
    public static String randomStringGenerator(int length) {
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String normalLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String values = capitalLetters + normalLetters +
                numbers;
        Random randomizer = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(values.charAt(randomizer.nextInt(values.length())));
        }
        return sb.toString();
    }
}
