package by.shobik.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ChatClient {

    private static final String SRV_ADDR = "localhost";

    private Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    public void connect(String addr, int port) throws IOException {
        socket = new Socket(addr, port);
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String get() throws IOException {
        return in.readLine();
    }

    public void send(String msg) throws IOException {
        out.println(msg);
        out.flush();
    }

    public String request(String msg) throws IOException {
        send(msg);
        return get();
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String line = "";
        ChatClient cc = new ChatClient();
        cc.connect(SRV_ADDR, 9999);

        System.out.print("Введите логин: ");
        while (!cc.request(in.next()).equals("OK")) {
            System.out.println("Логин уже занят. Попробуйте ещё раз: ");
        }

        String command = "";
        while (!command.equals("выход")) {
            System.out.println("Доступные команды:\n" +
                    "\tсписок - список клиентов\n" +
                    "\tотправ - отправить сообщение\n" +
                    "\tвходящ - проверить входящие\n" +
                    "\tвыход - выход");
            command = in.next();

            switch (command) {
                case "список" -> {
                    List<String> users = Arrays.stream(cc.request("LIST").split(";")).toList();
                    int i = 0;
                    for (String user : users) {
                        System.out.printf("%d. %s%n", ++i, user);
                    }
                }

                case "отправ" -> {
                    System.out.printf("Введите логин адресата: ");
                    String addr = in.next();
                    System.out.printf("Введите сообщение: ");
                    in.nextLine();
                    String content = in.nextLine();
                    cc.send("SEND");
                    cc.send(addr);
                    cc.send(content);
                }

                case "входящ" -> {
                    List<String> msgs = Arrays.stream(cc.request("IN").split(";")).toList();
                    int i = 0;
                    for (String msg : msgs) {
                        System.out.printf("%d. %s%n", ++i, msg);
                    }
                }

            }
        }

        cc.disconnect();
    }

}
