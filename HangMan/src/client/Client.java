package client;

import java.net.*;
import java.io.*;

public class Client {
    private Socket s;
    private int score = 0;

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }

    public void startClient() {
        try {
            s = new Socket("127.0.0.1", 5432);
            System.out.println("Conectado ao servidor.");

            new Client_Read(s);
            new Client_Write(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
    	return s;
    }
    public synchronized void incScore() { 
    	score++; 
    }

    public synchronized void decScore() {
    	score--; 
    }
    
    public int getScore() { 
    	return score; 
    }
    
}