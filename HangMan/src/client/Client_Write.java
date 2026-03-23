package client;

import java.net.Socket;
import utils.Ler;
import utils.Message;

public class Client_Write extends Thread {
    private Socket s;

    public Client_Write(Socket s) {
    	super();
        this.s = s;
        start();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
                int choice = Ler.umInt();
                Message.sendMessage(String.valueOf(choice), s);
            }
        } catch (Exception e) {
            System.out.println("Escrita terminada: " + e.getMessage());
        }
    }
}