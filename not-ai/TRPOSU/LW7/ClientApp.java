package by.shobik.lw4;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ClientApp {

    public static int safeNextInt(Scanner in) {
        int result;
        while (true) {
            try {
                return in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Вы ввели не число, попробуйте ещё раз.");
                in.next();
            }
        }
    }

    public static void main(String[] args) {

        List<Computer> computerList = new ArrayList<>();
        List<Domain> domainList = new ArrayList<>();

        try {
            Connection con = DatabaseConnector.getInstance();
            DatabaseExtractor de = new DatabaseExtractor(con);
            computerList = de.getComputers();
            domainList = de.getDomains();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Network localNetwork = new Network(domainList, computerList);

        List<Server> addedServers = new ArrayList<>();
        List<PersonalComputer> addedPcs = new ArrayList<>();
        List<Domain> addedDomains = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        int command = 0;
        while (command != 9) {
            System.out.print("Доступные комманды:\n" +
                    "\t1 - просмотр всех компьютеров\n" +
                    "\t2 - просмотр всех доменов\n" +
                    "\t3 - поиск компьютеров по домену\n" +
                    "\t4 - поиск компьютеров по имени\n" +
                    "\t5 - поиск компьютеров по IP\n" +
                    "\t6 - добавить персональный компьютер\n" +
                    "\t7 - добавить сервер\n" +
                    "\t8 - добавить домен\n" +
                    "\t9 - выход\n" +
                    "Ваш выбор: ");
            command = safeNextInt(in);

            switch (command) {
                case 1 -> {
                    for (Computer computer : localNetwork.getComputers()) {
                        System.out.println(computer);
                    }
                }

                case 2 -> {
                    for (Domain domain : localNetwork.getDomains()) {
                        System.out.println(domain);
                    }
                }

                case 3 -> {
                    System.out.print("Введите домен: ");
                    String domainName = in.next();
                    for (Computer computer : localNetwork.getComputersByIP(localNetwork.getIpFromDomain(domainName))) {
                        System.out.println(computer);
                    }
                }

                case 4 -> {
                    System.out.print("Введите имя: ");
                    String name = in.next();
                    for (Computer computer : localNetwork.getComputersByPredicate(
                            computer -> computer.getName().equals(name))
                    ) {
                        System.out.println(computer);
                    }
                }

                case 5 -> {
                    System.out.print("Введите IP:");
                    String ip = in.next();
                    for (Computer computer : localNetwork.getComputersByPredicate(
                            computer -> computer.getIp().getAddress().equals(ip)
                    )) {
                        System.out.println(computer);
                    }
                }

                case 6 -> {
                    System.out.print("Введите имя компьютера: ");
                    String name = in.next();
                    System.out.print("Введите имя пользователя: ");
                    String username = in.next();
                    System.out.println("Введите IP: ");
                    Ip ip = new Ip("0.0.0.0");
                    try {
                        ip = new Ip(in.next());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный IP.");
                        continue;
                    }
                    PersonalComputer newPc = new PersonalComputer(name, ip, username);
                    computerList.add(newPc);
                    addedPcs.add(newPc);
                    System.out.println("Персональный компьютер добавлен.");
                }

                case 7 -> {
                    System.out.print("Введите имя сервера: ");
                    String name = in.next();
                    System.out.print("Введите содержимое (введите STOP, чтобы закончить): ");
                    List<String> content = new ArrayList<>();
                    String content1 = "";
                    do {
                        content1 = in.next();
                        if (!content1.equals("STOP"))
                            content.add(content1);
                    } while (!content1.equals("STOP"));
                    System.out.println("Введите IP: ");
                    Ip ip = new Ip("0.0.0.0");
                    try {
                        ip = new Ip(in.next());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный IP.");
                        continue;
                    }
                    Server newServer = new Server(name, ip, content);
                    addedServers.add(newServer);
                    computerList.add(newServer);
                    System.out.println("Сервер добавлен.");
                }

                case 8 -> {
                    System.out.print("Введите доменное имя: ");
                    String name = in.next();
                    System.out.println("Введите IP:");
                    Ip ip = new Ip("0.0.0.0");
                    try {
                        ip = new Ip(in.next());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный IP.");
                        continue;
                    }
                    Domain newDomain = new Domain(name, ip);
                    domainList.add(newDomain);
                    addedDomains.add(newDomain);
                }

                case 9 -> {
                    System.out.println("Выходим.");
                }
            }

        }

        try {
            Connection con = DatabaseConnector.getInstance();
            DatabaseInjector di = new DatabaseInjector(con);
            di.injectAll(addedDomains, addedServers, addedPcs);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}