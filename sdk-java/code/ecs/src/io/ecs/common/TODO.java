package io.ecs.common;

public class TODO {

  public static <T> T throwing() {
    throw new NotImplementedError();
  }

  private static class NotImplementedError extends RuntimeException {
  }

}
