package client;

import java.net.*;

import utils.Ler;
import utils.Message;

import java.io.*;

public class Client {
	private static String DEFAULT_IP = "127.0.0.1";
	
    private Socket s;
    private Message msg;

    public static void main(String[] args) throws UnknownHostException, IOException {
    	// Ask user if they want to define a custom IP
    	System.out.println("Do you want to define a connection? (default: localhost)");
    	System.out.println("1. Yes");
    	System.out.println("2. No");
    	int choice = Ler.umInt();
    	
    	if (choice == 1) {
    		System.out.print("Enter IP: ");
    		String IP = Ler.umaString();
    		new Client(IP);
    	}
    	else
    		new Client(DEFAULT_IP);
    }

// Constructors    
    public Client(String IP) throws UnknownHostException, IOException {
    	// Connects to server on port 5432
    	s = new Socket(IP, 5432);
    	msg = new Message(s);
    	
    	// Starts read and write threads
        new Client_Read(s,msg); 
        new Client_Write(s,msg);
    }
    
// Getters and Setters
    public Socket getSocket() {
    	return s;
    }
    
    public void setSocket(String s) throws UnknownHostException, IOException {
    	this.s = new Socket(s,5432);
    }
}