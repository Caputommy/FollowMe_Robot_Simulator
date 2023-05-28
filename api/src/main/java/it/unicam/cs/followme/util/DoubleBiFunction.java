package it.unicam.cs.followme.util;
@FunctionalInterface
public interface DoubleBiFunction<R> {
    R apply(double value1, double value2);
}
