package dev.khbd.interp4j.examples;

import static dev.khbd.interp4j.core.Interpolations.s;

public class Main {

    public static void main(String... args) {
        String name = "Alex";
        System.out.println(s("Hello ${name}"));
    }
}