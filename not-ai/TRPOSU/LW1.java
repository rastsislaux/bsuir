package com.company;

/*
В приведенных ниже заданиях необходимо вывести внизу фамилию разработчика, дату получения задания,
а также дату сдачи задания. Для работы с динамическим массивом вводить его с клавиатуры и
необходимо использовать утилитный класс Arrays и его стандартные методы. В заданиях на числа объект
можно создавать в виде массива символов. Ввести n чисел с консоли (1-15 вариант добавлять новый
элемент в начало списка, 16-30 – в конец списка).

Перестроить матрицу, переставляя в ней строки так, чтобы сумма элементов в строках полученной матрицы возрастала.
*/

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static int readInt(Scanner scanner) {

        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод, попробуйте ещё раз.");
            }
        }

    }

    public static void swapLines(int[][] matrix, int i, int j) {
        int[] temp = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = temp;
    }

    public static int[][] readMatrix(Scanner scanner, int n, int m) {

        int[][] result = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = readInt(scanner);
            }
        }
        return result;

    }

    public static int[][] sortLines(int[][] matrix) {

        int[][] result = matrix.clone();
        for (int i = 0; i < result.length; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if (Arrays.stream(result[i]).sum() > Arrays.stream(result[j]).sum()) {
                    swapLines(result, i, j);
                }
            }
        }
        return result;

    }

    public static void printMatrix(int[][] matrix) {

        for (int[] line : matrix) {
            for (int element : line) {
                System.out.print(element + " ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("ФИО\nДата получения задания: ???\nДата сдачи задания: ???");

        System.out.print("Ввведите размер матрицы (N M): ");
        int n = readInt(in),
            m = readInt(in);

        System.out.println("Введите матрицу:");
        int[][] matrix = readMatrix(in, n, m);

        printMatrix(
                sortLines(matrix)
        );

    }
}
