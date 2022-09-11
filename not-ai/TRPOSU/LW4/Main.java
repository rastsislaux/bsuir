package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static Network createNetwork() {
        Computer personalComputer1 = new PersonalComputer("PC-1", "176.34.31.12", "Nikolai"),
                personalComputer2 = new PersonalComputer("PC-2", "176.34.31.12", "Alexei"),
                personalComputer3 = new PersonalComputer("PC-3", "176.35.11.182", "Irina"),
                server1 = new Server("Server-1", "178.95.25.11",
                        Arrays.asList("index.html", "404.html")),
                server2 = new Server("Server-2", "102.152.11.254",
                        Arrays.asList("index.html", "404.html", "about.html"));

        try {
            Computer personalComputer4 = new PersonalComputer("PC-4", "invalid-ip", "Fedor");
        } catch (RuntimeException e) {
            System.out.println("Invalid ip address throws an exception.");
        }

        Domain pc12Domain = new Domain("pc12.by", "176.34.31.12"),
                server1Domain = new Domain("server1.by", "178.92.25.11");

        Network localNetwork = new Network();

        localNetwork.getComputers().add(personalComputer1);
        localNetwork.getComputers().add(personalComputer2);
        localNetwork.getComputers().add(personalComputer3);

        localNetwork.getComputers().add(server1);
        localNetwork.getComputers().add(server2);

        localNetwork.getDomains().add(pc12Domain);
        localNetwork.getDomains().add(server1Domain);

        return localNetwork;
    }

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

        Network localNetwork = createNetwork();

        Scanner in = new Scanner(System.in);
        int command = 0;
        while (command != 6) {
            System.out.print("Доступные комманды:\n" +
                    "\t1 - просмотр всех компьютеров\n" +
                    "\t2 - просмотр всех доменов\n" +
                    "\t3 - поиск компьютеров по домену\n" +
                    "\t4 - поиск компьютеров по имени\n" +
                    "\t5 - поиск компьютеров по IP\n" +
                    "\t6 - выход\n" +
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
                    System.out.println("Выходим.");
                }
            }

        }
    }
}