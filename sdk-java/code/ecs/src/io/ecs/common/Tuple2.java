package io.ecs.common;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class Tuple2<T1, T2> {

    public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
        return new Tuple2<>(_1, _2);
    }

    public final T1 _1;
    public final T2 _2;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2)) return false;
        Tuple2<?, ?> t = (Tuple2<?, ?>) o;
        return Objects.equals(_1, t._1) && Objects.equals(_2, t._2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }

}
