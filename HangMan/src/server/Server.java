package server;

import java.io.*;
import java.net.*;

public class Server {

	private ServerSocket ss;
	private Socket s;
	private Connection c;
	
	//Remover mais tarde a main... 
	//Apenas teste.
	public static void main(String[] args ) {
		Server s = new Server();
		s.c.isAlive();
	}
	
	
	public Server() {
		try {
			ss = new ServerSocket(5432);
			if(ss.isBound()) {
				System.out.println("Servidor online!");
			}
			while(true) {
				s  = ss.accept();
				if(s.isConnected()) {
					System.out.println("Nova ligacao ao Servidor!");
				}
				c  = new Connection(s);
				c.start();
				if(c.isAlive()) {
					System.out.println("Thread iniciada!");	
				} 					
			}
				
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
}
