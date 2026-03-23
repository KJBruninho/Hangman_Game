package client;

import java.io.*;
import java.net.*;
import utils.Ler;
import utils.Message;

public class Client {

    private Socket s;
    private int score;

    public static void main(String[] args) {
        // Inicia a instância do cliente
        new Client();
    }

    public Client() {
        try {
            s = new Socket("127.0.0.1", 5432);
            score = 0;
            System.out.println("Conectado ao servidor.");

            // Thread para RECEBER mensagens do servidor (Background)
            new Thread(() -> {
                try {
                    while (true) {
                        Object msg = Message.receiveMessage(s);
                        if (msg != null) {
                            System.out.println("\n[Servidor]: " + msg);
                            System.out.print("> "); // Prompt visual
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Conexão com o servidor terminada.");
                }
            }).start();

            // Thread Principal: ENVIAR mensagens (Loop de Input)
            while (true) {
                int choice = Ler.umInt(); // Utiliza a tua classe utils.Ler
                Message.sendMessage(String.valueOf(choice), s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters e métodos de Score mantidos...
    public int getScore() { return score; }
    public synchronized void incScore() { score++; }
    public synchronized void decScore() { score--; }
    public Socket getSocket() { return s; }
}