package client;

import java.io.*;
import java.net.*;

import utils.Menus;
import utils.Message;

public class Client {

	private Socket s;
	private int score;
	
	public static void main(String[] args) {
		System.out.println(Menus.printMenuLobby());
	}
	
	public Client(){
		try {
			s = new Socket("127.0.0.1", 5432);
			score=0;
			Message.receiveMessage(s);
		} catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public int getScore() {
		return score;
	}
	
	public synchronized void incScore() {
		score++;
	}
	
	public synchronized void decScore() {
		score--;
	}
	
	public Socket getSocket() {
		return s;
	}
	
}
