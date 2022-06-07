package by.siluk.taxidepot;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static TaxiDepot createTestDepot() {
        TaxiDepot taxiDepot = new TaxiDepot();

        taxiDepot.add(new AutomaticTransmissionVehicle("BMW", 12, 150000, 180));
        taxiDepot.add(new ManualTransmissionVehicle("Audi", 8, 120000, 160));
        taxiDepot.add(new AutomaticTransmissionVehicle("Mercedes", 6, 100000, 170));
        taxiDepot.add(new ManualTransmissionVehicle("Toyota", 4, 50000, 150));
        taxiDepot.add(new AutomaticTransmissionVehicle("Lada", 30, 15, 60));
        taxiDepot.add(new ManualTransmissionVehicle("Volkswagen", 10, 7000, 170));

        return taxiDepot;
    }

    public static void printDepot(TaxiDepot taxiDepot) {
        int i = 0;
        for (Vehicle vehicle : taxiDepot) {
            System.out.format("%d. %s - fuel consumption: %d, cost: %d, max speed: %d%n",
                    ++i,
                    vehicle.getName(),
                    vehicle.getFuelConsumption(),
                    vehicle.getCost(),
                    vehicle.getMaxSpeed());
        }
    }

    public static void showMenu() {
        System.out.print("Доступые команды:\n" +
                "\t1. Вывести список машин в таксопарке\n" +
                "\t2. Подсчитать стоимость таксопарка\n" +
                "\t3. Сортировать по расходу топлива\n" +
                "\t4. Найти автомобили по диапозону скорости\n" +
                "\t5. Выход\n" +
                "Ваш выбор:");
    }

    public static void findBySpeedRangeMenu(Scanner in, TaxiDepot taxiDepot) {
        try {
            System.out.print("Введите нижнюю границу скорости: ");
            int min = in.nextInt();
            System.out.print("Введите верхнюю границу скорости: ");
            int max = in.nextInt();
            printDepot(taxiDepot.getBySpeedRange(min, max));
        } catch (InputMismatchException e) {
            System.out.println("Неверный формат ввода.");
            return;
        }
    }

    public static class NoCommandError extends RuntimeException { }

    public static void handle(String choice, TaxiDepot taxiDepot, Scanner in) {
        switch (choice) {
            case "1" -> printDepot(taxiDepot);
            case "2" -> System.out.println("Стоимость всего таксопарка: " + taxiDepot.getCost());
            case "3" -> taxiDepot.sortByFuelConsumption();
            case "4" -> findBySpeedRangeMenu(in, taxiDepot);
            case "5" -> System.out.println("Выходим...");
            default -> throw new NoCommandError();
        }
    }

    public static <K> K writeObjectToFile(String path, K object) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static <K> K readObjectFromFile(ObjectInputStream ois) {
        K result;
        try {
            return (K) ois.readObject();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении из файла: " + e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        TaxiDepot taxiDepot = createTestDepot();
        Scanner in = new Scanner(System.in);
        String choice = "";

        while (!choice.equals("да") && !choice.equals("нет")) {
            System.out.println("Хотите загрузить информацию из файла?");
            choice = in.next();
        }

        if (choice.equals("да")) {
            try {
                FileInputStream fis = new FileInputStream("info.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                taxiDepot = readObjectFromFile(ois);
                ois.close();
                fis.close();
            } catch (IOException e) {
                System.out.println("Ошибка при открытии файла: "  + e.getMessage());
                System.exit(1);
            }
        }


        while (!choice.equals("5")) {
            showMenu();
            choice = in.next();
            try {
                handle(choice, taxiDepot, in);
            } catch (NoCommandError e) {
                System.out.println("Неизвестная команда.");
            }
        }

        writeObjectToFile("info.ser", taxiDepot);
    }

}
