package client;

import java.io.*;
import java.net.*;

import utils.Message;

public class Client {

	private Socket s;
	
	public Client(){
		try {
			s = new Socket("127.0.0.1", 5432);
			Message.receiveMessage(s);
		} catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public Socket getSocket() {
		return s;
	}
	
}
