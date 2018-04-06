package io.ecs.common;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class IntTuple2 {

    public static IntTuple2 of(int _1, int _2) {
        return new IntTuple2(_1, _2);
    }

    private final int _1;
    private final int _2;

    private IntTuple2(int _1, int _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public int _1() {
        return _1;
    }

    public int _2() {
        return _2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntTuple2)) return false;
        IntTuple2 t = (IntTuple2) o;
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
