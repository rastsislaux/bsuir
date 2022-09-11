package by.shobik.chat;

import java.util.Scanner;

public class ChatServerControls {

    public static int SRV_PORT = 9999;

    public static void main(String[] args) {
        ChatServer chatServer = null;

        Scanner in = new Scanner(System.in);
        String command = "";

        while (!command.equals("exit")) {
            System.out.print(" > ");
            command = in.next();

            switch (command) {

                case "start" -> {
                    chatServer = new ChatServer(in.nextInt());
                    chatServer.start();
                }

                case "stop", "exit" -> {
                    if (chatServer == null) {
                        System.out.println(" > Server's stopped already.");
                        continue;
                    }
                    chatServer.interrupt();
                    chatServer = null;
                    try { Thread.sleep(1); } catch (InterruptedException e) { }
                }

            }
        }
    }
}
