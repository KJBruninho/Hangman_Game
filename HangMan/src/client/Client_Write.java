package client;

import java.io.IOException;
import java.net.Socket;

import utils.Ler;
import utils.Message;

public class Client_Write extends Thread {
    private Socket s;
    private Message msg;
    
// Constructors  
    public Client_Write(Socket s, Message msg) throws IOException {
        this.s = s;
        this.msg = msg;
        start(); // Starts write thread
    }
    
// Overridden Methods
    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
            	// Reads user input and sends it to the server
            	String input = Ler.umaString();
                msg.send(input);
            }
        } catch (Exception e) {
            // Indicates writing thread ended
            System.out.println("Write terminated: " + e.getMessage());
        } 
    }
}