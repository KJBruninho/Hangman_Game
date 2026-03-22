package client;

import java.io.*;
import java.net.*;
import utils.Message;

public class Client {

	private Socket s;
	
	
	//Remover mais tarde a main... 
	//Apenas teste.
	public static void main(String[] args ) throws ClassNotFoundException, IOException {
		Client teste = new Client();
		teste.s.close();
	}
	
	public Client(){
		try {
			s = new Socket("127.0.0.1", 5432);
			//Remover mais tarde.Colocar a restante logica.
			Message.receiveMessage(s);
			s.close();
		} catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
}
