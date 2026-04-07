package client;

import java.net.Socket;
import java.util.Scanner;
import utils.Message;

public class Client_Write extends Thread {
    private Socket s;
    private Scanner sc;

    public Client_Write(Socket s) {
        this.s = s;
        this.sc = new Scanner(System.in);
        start();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
                String input = sc.nextLine(); 
                Message.sendMessage(input, s);
            }
        } catch (Exception e) {
            System.out.println("Escrita terminada: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}