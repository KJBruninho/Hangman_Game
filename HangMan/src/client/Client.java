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
    	System.out.println("Would you like to define a connection? (default: localhost)");
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

//Constructors    
    public Client(String IP) throws UnknownHostException, IOException {
		//Establishes a connection to the server and initializes the Message object for communication on port 5432 (HARDCODED)
    	s = new Socket(IP, 5432);
    	msg = new Message(s);
    	
		//Starts the reading and writing threads for communication with the server. 
		//The Client_Read and Client_Write classes will handle incoming and outgoing messages, respectively.
		//Note: The constructors of Client_Read and Client_Write are expected to start their respective threads upon instantiation.
        new Client_Read(s,msg); 
        new Client_Write(s,msg);
    }
    
//Getters e Setters
    public Socket getSocket() {
    	return s;
    }
    
    public void setSocket(String s) throws UnknownHostException, IOException {
    	this.s = new Socket(s,5432);
    }
}