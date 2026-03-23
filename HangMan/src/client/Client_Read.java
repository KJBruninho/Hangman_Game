package client;

import java.net.Socket;
import utils.Message;

public class Client_Read extends Thread {
	
    private Socket s;

    public Client_Read(Socket s) {
    	super();
        this.s = s;
        start();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
                Object msg = Message.receiveMessage(s);
                if (msg != null) {
                    System.out.println("\n[Servidor]: " + msg);
                    System.out.print("> ");
                }
            }
        } catch (Exception e) {
            System.out.println("Leitura terminada: " + e.getMessage());
        }
    }
}