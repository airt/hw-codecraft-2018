package io.ecs.common;

public class TODO {

    public static <T> T throwing() {
        throw new NotImplementedError();
    }

    static class NotImplementedError extends RuntimeException {
    }

}
