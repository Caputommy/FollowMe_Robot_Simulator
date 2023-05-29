package it.unicam.cs.followme.util;

import java.util.Random;

/**
 * Representation of a numeric real range
 * @param start lower inclusive bound of the range
 * @param end upper inclusive bound of the range
 */
public record DoubleRange(double start, double end){

    public DoubleRange(double start, double end) {
        this.start = Math.min(start, end);
        this.end = Math.max(start, end);
    }

    public double getRandomDoubleInRange() {
        return this.randomInRange(new Random());
    }

    public double getRandomDoubleInRange(long seed) {
        return this.randomInRange(new Random(seed));
    }

    private double randomInRange(Random rand) {
        return start + rand.nextDouble()*(end-start);
    }
}
