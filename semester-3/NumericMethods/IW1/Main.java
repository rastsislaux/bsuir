package org.lipski.chmipz1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        double x;
        System.out.print("x = ");
        x = in.nextDouble();

        Solver solver = new SolverWithIssue(x);
        Solver goodSolver = new GoodSolver(x);
        try {
            System.out.printf("Result by tailor = %f%n", solver.solve());
        } catch (StackOverflowError e) {
            System.out.printf("Impossible to solve by taylor due to stackoverflow error.%n");
        }
        try {
            System.out.printf("Result by good solver = %f%n", goodSolver.solve());
        } catch (StackOverflowError e) {
            System.out.printf("Impossible to solve by good solver due to stackoverflow error.%n");
        }
        System.out.printf("Error is %f%%%n", Math.abs(goodSolver.solve() - Math.sin(x)) / Math.sin(x) * 100);
        System.out.printf("Result by Java = %f%n", Math.sin(x));
    }
}