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
    	System.out.println("Gostaria de definir uma ligacao? (default: localhost)");
    	System.out.println("1. Sim");
    	System.out.println("2. Nao");
    	int choice = Ler.umInt();
    	
    	if (choice == 1) {
    		System.out.print("Introduza o IP: ");
    		String IP = Ler.umaString();
    		new Client(IP);
    	}
    	else
    		new Client(DEFAULT_IP);
    }

//Constructors    
    public Client(String IP) throws UnknownHostException, IOException {
    	s = new Socket(IP, 5432);
    	msg = new Message(s);
    	
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