package by.shobik.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatHandler extends Thread {

    int id;

    private Socket socket;

    private ChatServer chatServer;

    String clientName = null;

    List<String> incoming = Collections.synchronizedList(new ArrayList<>());

    ChatHandler(int id, Socket socket, ChatServer chatServer) {
        this.id = id;
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {

        BufferedReader in;
        PrintWriter out;

        try {

            System.out.printf(" > handler #%d > Starting connection...%n > ", id);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.printf(" > handler #%d > Connection established. Waiting for login...%n > ", id);
            while (true) {
                clientName = in.readLine();
                if (clientName == null) {
                    return;
                }
                else if (chatServer.hasClient(clientName)) {
                    out.println("NOT OK");
                    out.flush();
                }
                else {
                    out.println("OK");
                    out.flush();
                    chatServer.addClient(clientName);
                    break;
                }
            }
            System.out.printf(" > handler #%d > New client: `%s`.%n > ", id, clientName);

            listening: while (true) {
                String req = in.readLine();
                if (req == null || req.equals("EXIT")) break;
                switch (req) {
                    case "LIST" -> {
                        System.out.printf(" > handler #%d > Sent list of users to %s%n > ", id, clientName);
                        out.println(chatServer.getClientString());
                        out.flush();
                    }

                    case "SEND" -> {
                        System.out.printf(" > handler #%d > Waiting for message from `%s`%n > ", id, clientName);
                        String addr = in.readLine();
                        System.out.printf(" > handler #%d > Message will be sent to `%s`%n > ", id, addr);
                        String msg = in.readLine();
                        chatServer.sendMessageToClient(clientName, addr, msg);
                        System.out.printf(" > handler #%d > Message sent.%n > ", id);
                    }

                    case "IN" -> {
                        System.out.printf(" > handler #%d > Sent incoming messages to `%s`%n > ", id, clientName);
                        out.println(String.join(";", incoming));
                        incoming.clear();
                        out.flush();
                    }
                    default -> { }
                }
            }

            chatServer.removeClient(clientName);

        } catch (IOException e) {
            System.out.printf(" > handler #%d > Error while working: %s.%n > ", id, e.getMessage());
        }

    }

}
