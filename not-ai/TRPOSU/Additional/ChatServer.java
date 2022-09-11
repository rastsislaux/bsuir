package by.shobik.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatServer extends Thread {

    private final int port;

    private List<String> clientList = Collections.synchronizedList(new ArrayList<>());

    List<ChatHandler> handlers = new ArrayList<>();

    public void sendMessageToClient(String from, String addr, String content) {
        for (ChatHandler handler : handlers) {
            if (handler.clientName.equals(addr)) {
                handler.incoming.add("От `" + from + "`: " + content);
            }
        }
    }

    public String getClientString() {
        return String.join(";", clientList);
    }

    public void addClient(String username) {
        clientList.add(username);
    }

    public void removeClient(String username) {
        clientList.remove(username);
    }

    public boolean hasClient(String username) {
        return clientList.contains(username);
    }

    public ChatServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            System.out.print("server > Started at port " + port + "\n > ");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int i = 0;
        while (!interrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ChatHandler newHandler = new ChatHandler(++i, clientSocket, this);
                handlers.add(newHandler);
                newHandler.start();
            }
            catch (SocketTimeoutException ignored) { }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("server > Interrupting handlers...");
        for (ChatHandler chatHandler : handlers) {
            chatHandler.interrupt();
            System.out.println(" > server > Interrupted handler #" + chatHandler.id);
        }
        System.out.println(" > server > Handlers interrupted.");

        try {
            serverSocket.close();
        } catch (IOException ignored) {
            System.out.printf(" > server > Failed to close socket.%n");
        }

        System.out.print(" > server > Stopped.\n > ");

    }

}