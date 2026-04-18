package client;

import java.io.IOException;
import java.net.Socket;

import utils.Ler;
import utils.Message;

public class Client_Write extends Thread {
    private Socket s;
    private Message msg;
    
//Constructors  
    public Client_Write(Socket s,Message msg) throws IOException {
        this.s = s;
        this.msg = msg;
        start(); 
    }
    
//Overridden Methods
    @Override
    public void run() {
        try {
            //Continuously reads user input from the console and sends it to the server using the Message class's send method.
            //The loop continues until the socket is closed. 
            //Note: If an exception occurs (e.g., if the connection is lost), it catches the exception and prints a termination message.
            while (!s.isClosed()) {
            	String input = Ler.umaString();
                msg.send(input);
            }
        } catch (Exception e) {
            System.out.println("Write terminated: " + e.getMessage());
        } 
    }
}