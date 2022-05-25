package by.shobik;

import java.util.Scanner;
import java.util.TreeMap;

public class Segment {

    private Pair<Double, Double> start = new Pair<>();
    private Pair<Double, Double> finish = new Pair<>();

    Segment(double xS, double yS, double xF, double yF) {
        start.first = xS;
        start.second = yS;
        finish.first = xF;
        finish.second = yF;
    }

    public double calculateK() {
        return (finish.second - start.second) / (finish.first - start.first);
    }

    public double calculateB(double k) {
        return start.second - k * start.first;
    }

    public double calculateB() {
        return calculateB(calculateK());
    }

    public static class NoIntersectionException extends RuntimeException {

    }

    Pair<Double, Double> getIntersection(Segment other) {

        double k1 = calculateK();
        double k2 = other.calculateK();
        double b1 = calculateB(k1);
        double b2 = other.calculateB(k2);

        double x = (b2-b1)/(k1-k2);
        double y = k1*x + b1;

        if (start.first <= x && x <= finish.first &&
            start.second <= y && y <= finish.second) {
            return new Pair<>(x, y);
        }

        throw new NoIntersectionException();

    }

    @Override
    public String toString() {
        return "Segment{" +
                "start=" + start +
                ", finish=" + finish +
                '}';
    }

    public static void main(String[] args) {

        System.out.println("Введите количество отрезков: ");
        Scanner in = new Scanner(System.in);
        TreeMap<Double, Segment> treeMap = new TreeMap<>();

        double yMin1 = Double.MAX_VALUE;
        double yMin2 = Double.MAX_VALUE;

        int count = in.nextInt();
        for (int i = 0; i < count; i++) {
            System.out.print("Введите " + (i + 1) + " отрезок (xS, yS, xF, yF): ");

            double xS = in.nextDouble();
            double yS = in.nextDouble();
            double xF = in.nextDouble();
            double yF = in.nextDouble();

            treeMap.put(yS, new Segment(xS, yS, xF, yF));
        }

        Segment min1 = treeMap.values().stream().min((a, b) -> a.start.second > b.start.second ? 1 : 0).get();
        treeMap.remove(min1.start.second);
        Segment min2 = treeMap.values().stream().min((a, b) -> a.start.second > b.start.second ? 1 : 0).get();

        Pair<Double, Double> intersection = new Pair<>();
        try {
            intersection = min1.getIntersection(min2);
        } catch (NoIntersectionException e) {
            System.out.println("No intersection.");
        }

        System.out.println(intersection);

    }

}