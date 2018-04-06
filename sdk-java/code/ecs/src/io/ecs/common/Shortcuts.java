package io.ecs.common;

@SuppressWarnings("WeakerAccess")
public class Shortcuts {

    public static <T> void println(T x) {
        System.out.println(x);
    }

    public static void println(double x) {
        System.out.println(x);
    }

    public static <T> T throwing(RuntimeException e) {
        throw e;
    }

}
