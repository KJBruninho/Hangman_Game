package client;

import java.io.*;
import java.net.*;

import utils.Menus;
import utils.Message;

public class Client2 {

	private Socket s;
	
	
	//Remover mais tarde a main... 
	//Apenas teste.
	public static void main(String[] args ) throws ClassNotFoundException, IOException {
		Client2 teste = new Client2();
		teste.equals(teste);
	}
	
	public Client2(){
		try {
			s = new Socket("127.0.0.1", 5432);
			Message.receiveMessage(s);
			System.out.println(Menus.printMenuInicial());
		} catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    } finally {
	    	try {
	    		s.close();
	    	} catch(IOException e) {System.out.println(e);}
	    }
	}
	
}
