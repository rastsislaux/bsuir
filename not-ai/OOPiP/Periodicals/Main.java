package by.yasenko.subscription;

import java.io.*;
import java.util.*;

public class Main {

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

    public static <K> K readObjectFromFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            K result = (K) ois.readObject();
            ois.close();
            fis.close();
            return result;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении из файла: " + e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static final String periodicalsPath = "periodicals.dat";
    public static final String applicationsPath = "applications.dat";

    public static void printApplications(List<Application> applications) {
        int i = 0;
        for (Application application : applications) {
            System.out.println(++i + ". " + application);
        }
    }

    public static void printPeriodicals(List<Periodical> periodicals) {
        int i = 0;
        for (Periodical periodical : periodicals) {
            System.out.println(++i + ". " + periodical.getName() + ": " + periodical.getCost() + "$");
        }
    }

    public static class OutOfBoundsException extends RuntimeException { }

    public static int readInt(Scanner in, int min, int max) throws OutOfBoundsException {
        while (true) {
            try {
                int i = in.nextInt();
                if (i < min || i > max) throw new OutOfBoundsException();
                return i;
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод.");
            }
        }
    }

    public static void main(String[] args) {
        List<Periodical> periodicals = new ArrayList<>();
        List<Application> applications = new ArrayList<>();

        if (new File(periodicalsPath).exists()) {
            periodicals = readObjectFromFile(periodicalsPath);
        }

        if (new File(applicationsPath).exists()) {
            applications = readObjectFromFile(applicationsPath);
        }

        Scanner in = new Scanner(System.in);
        String command = "";
        command_loop: while (!command.equals("0")) {
            System.out.printf("Команды:%n" +
                    "\t0 - выход%n" +
                    "\t1 - оставить заявку%n" +
                    "\t2 - добавить журнал%n" +
                    "\t3 - добавить газету%n" +
                    "\t4 - заблокировать заявку%n" +
                    "\t5 - просмотреть издания%n" +
                    "\t6 - просмотреть заявки%n");
            command = in.next();
            switch (command) {
                case "0" -> System.out.printf("Выход.%n");
                case "1" -> {
                    printPeriodicals(periodicals);
                    System.out.println("Выберите периодические издания для подписки (ввод чисел через запятую):");
                    in.nextLine();
                    List<String> periodicalsStr = Arrays.stream(in.nextLine().split(",")).toList();
                    Application application = new Application();
                    for (String str : periodicalsStr) {
                        try {
                            int index = Integer.parseInt(str.strip());
                            if (index < 1 || index > periodicals.size()) throw new OutOfBoundsException();
                            application.periodicals.add(periodicals.get(index - 1));
                        } catch (NumberFormatException | OutOfBoundsException e) {
                            System.out.println("Неверный ввод.");
                            continue command_loop;
                        }
                    }
                    applications.add(application);
                }
                case "2" -> {
                    System.out.println("Введите название журнала и цену подписки через пробел:");
                    try {
                        periodicals.add(new Magazine(in.next(), readInt(in, 0, 10000)));
                    } catch (OutOfBoundsException e) {
                        System.out.println("Неверный ввод.");
                    }
                }
                case "3" -> {
                    System.out.println("Введите название газеты и цену подписки через пробел:");
                    try {
                        periodicals.add(new Newspaper(in.next(), readInt(in, 0, 10000)));
                    } catch (OutOfBoundsException e) {
                        System.out.println("Неверный ввод.");
                    }
                }
                case "4" -> {
                    printApplications(applications);
                    System.out.println("Введите номер заявки для блокировки: ");
                    int index = readInt(in, 1, applications.size());
                    applications.get(index - 1).setBanned(true);
                }
                case "5" -> printPeriodicals(periodicals);
                case "6" -> printApplications(applications);
            }
        }

        writeObjectToFile(periodicalsPath, periodicals);
        writeObjectToFile(applicationsPath, applications);
    }
}