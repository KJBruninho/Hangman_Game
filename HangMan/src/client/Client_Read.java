package client;

import java.net.Socket;
import utils.Message;

public class Client_Read extends Thread {
    private Socket s;
    private Message msg;

    public Client_Read(Socket s, Message msg) {
        this.s = s;
        this.msg = msg;
        start();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) { 
                Object received = msg.receive();
                if (received != null) {
                    System.out.println(received.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Conexão encerrada.");
        }
    }
}