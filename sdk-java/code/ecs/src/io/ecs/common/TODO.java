package io.ecs.common;

public class TODO {

    public static <T> T throwing() {
        throw new NotImplementedError();
    }

    public static class NotImplementedError extends RuntimeException {
    }

}
