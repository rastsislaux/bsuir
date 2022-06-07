package by.siluk.library;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List<AbstractBook> createTestLibrary() {
        List<AbstractBook> abstractBooks = new ArrayList<>();
        abstractBooks.add(new ElectronicBook("Author 1", "Book 1"));
        abstractBooks.add(new PaperBook("Author 2", "Book 2"));
        abstractBooks.add(new ElectronicBook("Author 3", "Book 3"));
        abstractBooks.add(new PaperBook("Author 4", "Book 4"));
        abstractBooks.add(new ElectronicBook("Author 5", "Book 5"));
        abstractBooks.add(new PaperBook("Author 6", "Book 6"));
        return abstractBooks;
    }

    public static void printLibrary(List<AbstractBook> library) {
        int i = 0;
        for (AbstractBook book : library) {
            System.out.format("%d. %s: %s%n", ++i, book.getAuthor(), book.getName());
        }
    }

    public static void showMenu() {
        System.out.print("Доступные команды:\n" +
                "\tЧитатель:\n" +
                "\t\t1. Оформить заказ на книгу\n" +
                "\t\t2. Читать книгу в читальном зале\n" +
                "\t\t3. Забрать книгу с собой\n" +
                "\tБиблиотекарь:\n" +
                "\t\t4. Выдать абонемент\n" +
                "\tАдминистратор:\n" +
                "\t\t6. Внести пользователя в чёрный список\n" +
                "\t7. Просмореть библиотеку\n" +
                "\t8. Выход\n" +
                "Ваш выбор: ");
    }

    public static void handle(Scanner in, String choice, List<AbstractBook> library,
                              List<String> banList, List<Abonement> requstList,
                              List<Abonement> abonementList) {
        try {
            switch (choice) {
                case "1" -> makeRequestForBookMenu(in, library, banList, requstList);
                case "2" -> readBook(in, library, true, abonementList);
                case "3" -> readBook(in, library, false, abonementList);
                case "4" -> giveAbonement(in, library, requstList, abonementList);
                case "6" -> ban(in, banList);
                case "7" -> printLibrary(library);
                case "8" -> System.out.println("До свидания.");
            }
        } catch (NoBookError e) {
            System.out.println("Нет такой книги."); // Добавление ЛР2
        }
    }

    private static void ban(Scanner in, List<String> banList) {
        System.out.print("Введите имя для внесения в ЧС: ");
        String name = in.next();
        banList.add(name);
    }

    private static void giveAbonement(Scanner in, List<AbstractBook> library,
                                      List<Abonement> requstList,
                                      List<Abonement> abonementList) {

        int i = 0;
        for (Abonement abonement : requstList) {
            System.out.format("%d. %s: %s - in hall: %b%n", ++i, abonement.getBook().getAuthor(),
                                                            abonement.getBook().getName(),
                                                            abonement.isInReadingHall());
        }

        System.out.print("Выберите номер заявки для одобрения:");
        int choice = in.nextInt();
        if (choice < 1 || choice > requstList.size()) {
            System.out.println("Нет такой заявки.");
            return;
        }

        abonementList.add(requstList.get(choice - 1));
        requstList.remove(choice - 1);

    }

    static class NoBookError extends Exception { } // Добавление ЛР2
    private static void readBook(Scanner in, List<AbstractBook> library, boolean inReadingHall,
                                 List<Abonement> abonementList) throws NoBookError {

        printLibrary(library);
        System.out.print("Выберите номер книги:");
        int choice = in.nextInt();
        if (choice < 1 || choice > library.size()) {
            throw new NoBookError(); // Добавление ЛР2
        }

        if (!library.get(choice - 1).isReadable(abonementList, inReadingHall)) {
            System.out.println("Вы не можете прочитать эту книгу. Оформите заявку на абонемент.");
            return;
        }

        library.get(choice - 1).read();

    }

    private static void makeRequestForBookMenu(Scanner in, List<AbstractBook> library,
                                               List<String> banList, List<Abonement> requestList) {
        System.out.print("Как вас зовут?");
        if (banList.contains(in.next())) {
            System.out.println("Вам недоступна библиотека.");
            return;
        }

        printLibrary(library);
        System.out.print("Выберите номер книги:");
        int choice = in.nextInt();
        if (choice < 1 || choice > library.size()) {
            System.out.println("Нет такой книги.");
            return;
        }

        System.out.print("Вы хотите её читать в читальном зале или с собой? (0/1): ");
        boolean inReadingHall = in.nextInt() == 0;

        requestList.add(new Abonement(library.get(choice - 1), inReadingHall));
    }

    // Добавление ЛР3
    public static void writeInfoToFile(List<AbstractBook> library,
                                       List<Abonement> requestList,
                                       List<Abonement> abonementList,
                                       List<String> banList) {
        try {
            FileOutputStream fos = new FileOutputStream("info.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(library);
            oos.writeObject(requestList);
            oos.writeObject(abonementList);
            oos.writeObject(banList);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось открыть файл с информацией для записи.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
            System.exit(1);
        }
    }

    public static <K> K readObjectFromFile(ObjectInputStream ois) {
        K library;
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

        String choice = "";
        Scanner in = new Scanner(System.in);

        while(!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Введите 1 для чтения данных из файла или 2 для создания тестовой библиотеки:");
            choice = in.next();
        }

        List<AbstractBook> library = createTestLibrary();
        ArrayList<Abonement> requestList = new ArrayList<>();
        ArrayList<Abonement> abonementList = new ArrayList<>();
        ArrayList<String> banList = new ArrayList<>();

        if (choice.equals("1")) {
            try {
                FileInputStream fis = new FileInputStream("info.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                library = readObjectFromFile(ois);
                requestList = readObjectFromFile(ois);
                abonementList = readObjectFromFile(ois);
                banList = readObjectFromFile(ois);
            } catch (FileNotFoundException e) {
                System.out.println("Файл с данными не найден.");
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        while (!choice.equals("8")) {
            showMenu();
            try {
                choice = in.next();
                handle(in, choice, library, banList, requestList, abonementList);
            } catch (InputMismatchException e) {
                System.out.println("Неверный формат ввода."); // Добавление ЛР2
            }
        }

        writeInfoToFile(library, requestList, abonementList, banList);

    }

}
