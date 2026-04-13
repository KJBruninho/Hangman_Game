package client;

import java.net.*;

import utils.Message;

import java.io.*;

public class Client {
    private Socket s;
    private Message msg;

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client();
    }

//Constructors    
    public Client() throws UnknownHostException, IOException {
    	s = new Socket("127.0.0.1", 5432);
    	msg = new Message(s);
    	
        new Client_Read(s,msg); 
        new Client_Write(s,msg);
    }
    
//Getters e Setters
    public Socket getSocket() {
    	return s;
    }
    
}