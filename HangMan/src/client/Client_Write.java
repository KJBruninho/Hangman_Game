package client;

import java.io.IOException;
import java.net.Socket;

import utils.Ler;
import utils.Message;

public class Client_Write extends Thread {
    private Socket s;
    private Message msg;

    public Client_Write(Socket s,Message msg) throws IOException {
        this.s = s;
        this.msg = msg;
        start();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
            	String input = Ler.umaString();
                msg.send(input);
            }
        } catch (Exception e) {
            System.out.println("Escrita terminada: " + e.getMessage());
        } 
    }
}