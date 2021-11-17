package dev.khbd.interp4j.examples;

import static dev.khbd.interp4j.core.Interpolations.s;

public class Main {

    public static void main(String... args) {
        String name = "Alex";
        System.out.println(new dev.khbd.interp4j.core.internal.s.SInterpolator("Hello ", "").interpolate(name));
    }
}
