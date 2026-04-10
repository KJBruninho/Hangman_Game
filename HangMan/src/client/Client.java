package client;

import java.net.*;

import utils.Message;

import java.io.*;

public class Client {
    private Socket s;
    private int score = 0;
    private Message msg;

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client();
    }
    
    public Client() throws UnknownHostException, IOException {
    	s = new Socket("127.0.0.1", 5432);
    	msg = new Message(s);
    	
        new Client_Read(s,msg);
        new Client_Write(s,msg);
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