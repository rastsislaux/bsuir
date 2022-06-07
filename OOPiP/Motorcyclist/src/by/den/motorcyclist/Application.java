package by.den.motorcyclist;

import java.io.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static Motorcyclist deserialize(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Motorcyclist motorcyclist = (Motorcyclist) ois.readObject();
            ois.close();
            fis.close();
            return motorcyclist;
        } catch (IOException e) {
            System.out.println("Ошибка при десериализации файла: " + e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: невозможно десериализовать файл: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static void serialize(String path, Motorcyclist motorcyclist) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(motorcyclist);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файла: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Ошибка при сериализации файла: " + e.getMessage());
            System.exit(1);
        }
    }

    public static String path = "motorcyclist.bin";

    public static int readInt(Scanner in, int min, int max) {
        while (true) {
            try {
                int x = in.nextInt();
                if (x < min || x > max) throw new IllegalArgumentException("Число не входит в допустимые пределы.");
                return x;
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод.");
            }
        }
    }

    public static void printAmmunition(List<Ammunition> motorcyclist) {
        int i = 0;
        for (Ammunition ammunition : motorcyclist) {
            System.out.println(++i + ". " + ammunition);
        }
    }

    public static void printAmmunition(Motorcyclist motorcyclist) {
        printAmmunition(motorcyclist.getAmmunitionList());
    }

    public static void main(String[] args) {
        File f = new File(path);
        Motorcyclist motorcyclist = new Motorcyclist();
        if (f.exists()) { motorcyclist = deserialize(path); }
        Scanner in = new Scanner(System.in);
        String command = "";
        while (!command.equals("выход")) {
            System.out.println("Команды:" +
                               "\n\tдобавить - добавить элемент аммуниции" +
                               "\n\tудалить - удалить элемент аммуниции" +
                               "\n\tсписок - список аммуниции" +
                               "\n\tстоимость - стоимость всей аммуниции" +
                               "\n\tсортировка - сортировка аммуниции по весу" +
                               "\n\tфильтр - фильтровать по цене" +
                               "\n\tвыход - выход :)" +
                               "\nВаша команда:");
            command = in.next();
            try {
                switch (command) {

                    case "добавить" -> {
                        System.out.println("Введите название: ");
                        String name = in.next();
                        System.out.println("Введите вес (>1): ");
                        int weight = readInt(in, 1, Integer.MAX_VALUE);
                        System.out.println("Введите тип:" +
                                "\n\t1 - Шлем" +
                                "\n\t2 - Куртка" +
                                "\n\t3 - Перчатки" +
                                "\nВыбор: ");
                        int type = readInt(in, 1, 3);
                        switch (type) {
                            case 1 -> motorcyclist.addAmmunition(new Helmet(name, weight));
                            case 2 -> motorcyclist.addAmmunition(new Jacket(name, weight));
                            case 3 -> motorcyclist.addAmmunition(new Gloves(name, weight));
                            // Это исключение не появляется, если все возможные выборы обработаны.
                            default -> throw new IllegalStateException("Unexpected value: " + type);
                        }
                    }

                    case "удалить" -> {
                        System.out.println("Введите номер: ");
                        motorcyclist.removeAmmunition(readInt(in, 1, motorcyclist.getAmmunitionAmount()) - 1);
                    }

                    case "список" -> printAmmunition(motorcyclist);

                    case "стоимость" -> System.out.println("Стоимость всей аммуниции: " + motorcyclist.getAllCost());

                    case "сортировка" -> {
                        motorcyclist.sortByWeight();
                        printAmmunition(motorcyclist);
                    }

                    case "фильтр" -> {
                        System.out.println("Введите минимальную цену:");
                        int min = readInt(in, 0, Integer.MAX_VALUE);
                        System.out.println("Введите максимальную цену (>" + min + "):");
                        int max = readInt(in, min, Integer.MAX_VALUE);
                        List<Ammunition> ammunitionList = motorcyclist.filterByCost(min, max);
                        printAmmunition(ammunitionList);
                    }

                    case "выход" -> System.out.println("До свидания.");

                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        serialize(path, motorcyclist);
    }
}
