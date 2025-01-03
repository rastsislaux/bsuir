package org.lipski.chmipz1;

public class GoodSolver implements Solver {

    private double value;

    public GoodSolver(double value) {
        while (Math.abs(value) > 5) {
            value = value - (value > 0 ? 1 : -1) * 2 * Math.PI;
        }
        this.value = value;
    }

    private long factorial(long value) {
        if (value == 1L || value == 0L) {
            return 1L;
        }

        return value * factorial(value - 1);
    }

    private double _solve(double value, long power, boolean sign) {
        var temp = (sign ? 1 : -1) * Math.pow(value, power) / factorial(power);
        if (Double.isInfinite(temp)) {
            throw new StackOverflowError("Is infinite");
        }
        if (Math.abs(temp) < 0.001) {
            return temp;
        }
        return temp + _solve(value, power + 2, !sign);
    }

    public double solve() {
        return _solve(value, 1, true);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
