package io.ecs.common;

@SuppressWarnings("WeakerAccess")
public class Tuple2<T1, T2> {

  public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
    return new Tuple2<>(_1, _2);
  }

  final T1 _1;
  final T2 _2;

  private Tuple2(T1 _1, T2 _2) {
    this._1 = _1;
    this._2 = _2;
  }

  public T1 _1() {
    return _1;
  }

  public T2 _2() {
    return _2;
  }

}
