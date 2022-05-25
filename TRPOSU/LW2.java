package com.company;

import java.util.*;

public class BoolVector {

    // Реализация булевого вектора

    private ArrayList<Boolean> vector = new ArrayList<>();

    BoolVector() {}

    BoolVector(List<Boolean> booleans) {
        Collections.copy(vector, booleans);
    }

    BoolVector(BoolVector other) {
        vector = new ArrayList<>(other.vector);
    }

    public void add(boolean bool) {
        vector.add(bool);
    }

    public void remove(int index) {
        vector.remove(index);
    }

    public boolean get(int index) {
        return vector.get(index);
    }

    public void set(int index, boolean bool) {
        vector.set(index, bool);
    }

    public int size() {
        return vector.size();
    }

    public BoolVector conjunction(BoolVector other) {

        if (vector.size() != other.vector.size()) {
            throw new IllegalArgumentException("Вектора, над которыми выполняется операция " +
                    "конъюнкции должны быть одной длины.");
        }

        BoolVector result = new BoolVector();
        for (int i = 0; i < vector.size(); i++) {
            result.add(vector.get(i) && other.vector.get(i));
        }
        return result;

    }

    public BoolVector disjunction(BoolVector other) {

        if (vector.size() != other.vector.size()) {
            throw new IllegalArgumentException("Вектора, над которыми выполняется операция " +
                    "дизъюнкции должны быть одной длины.");
        }

        BoolVector result = new BoolVector();
        for (int i = 0; i < vector.size(); i++) {
            result.add(vector.get(i) || other.vector.get(i));
        }
        return result;

    }

    public BoolVector negation() {

        BoolVector result = new BoolVector();
        for (Boolean bool : vector) {
            result.add(!bool);
        }
        return result;

    }

    public int count(boolean bool) {
        int counter = 0;
        for (Boolean aBool : vector) {
            if (bool == aBool) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (Boolean bool : vector) {
            sb.append(bool ? 1 : 0)
              .append(",");
        }
        sb.deleteCharAt(sb.length() - 1)
          .append(")");
        return sb.toString();
    }

    // Тестовое приложение

    public static int readInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод, попробуйте ещё раз.");
                scanner.nextLine();
            }
        }
    }

    public static BoolVector readBoolVector(Scanner scanner) {

        System.out.print("Введите длину вектора: ");
        int length = readInt(scanner);
        System.out.print("Введите элементы вектора: ");
        BoolVector booleans = new BoolVector();
        for (int i = 0; i < length; i++) {
            booleans.add(readInt(scanner) != 0);
        }
        return booleans;

    }

    public static BoolVector randomVector(int length) {
        Random r = new Random();
        BoolVector booleans = new BoolVector();
        for (int i = 0; i < length; i++) {
            booleans.add(r.nextBoolean());
        }
        return booleans;
    }

    public static BoolVector randomVector() {
        Random r = new Random();
        return randomVector(r.nextInt(10) + 5);
    }

    public static void test(BoolVector vector1, BoolVector vector2) {

        System.out.println("Первый вектор: " + vector1 + "\nВторой вектор: " + vector2 +
                           "\nРезультат конъюнкции: " + vector1.conjunction(vector2) +
                           "\nРезультат дизъюнкции: " + vector1.disjunction(vector2) +
                           "\nРезультат отрицания первого: " + vector1.negation() +
                           "\nРезультат отрицания второго: " + vector2.negation() +
                           "\nКоличество нулей и единиц в первом: " + vector1.count(false) + ", " +
                                                                      vector1.count(true) +
                           "\nКоличество нулей и единиц во втором: " + vector2.count(false) + ", " +
                                                                       vector2.count(true));

    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int command;
        testing: while (true) {
            System.out.println("1) Ручной тест\n2) Случайный тест\n3) Выход");
            command = readInt(in);
            switch (command) {
                case 1 -> {
                    System.out.println("Введите первый вектор:");
                    BoolVector vector1 = readBoolVector(in);
                    System.out.println("Введите второй вектор:");
                    BoolVector vector2 = readBoolVector(in);
                    test(vector1, vector2);
                }
                case 2 -> {
                    BoolVector vector1 = randomVector(),
                               vector2 = randomVector(vector1.size());
                    test(vector1, vector2);
                }
                case 3 -> { break testing; }
                default -> System.out.println("Неверный номер команды. Попробуйте ещё раз.");
            }
        }
        in.close();
    }

}
