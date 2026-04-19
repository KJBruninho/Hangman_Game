package server;

import java.io.*;
import java.net.*;


public class Server {

	private ServerSocket ss;
	private Socket s;
	private Connection c;	

//Constructors	
	public Server() {
		try {
			ss = new ServerSocket(5432); 
			while(true) {
				s  = ss.accept();
				//allows the server to bind to the same port. 
				//Note: Alows to quick restart the server avoiding "Address already in use" errors.
				ss.setReuseAddress(true);
				c  = new Connection(s);	
				if(c.isAlive()) {
					System.out.println("Connection made by " + s + " with "+c.getName());
				}
			}
				
		} catch(IOException e) {
			System.out.println("Server: " + e.getMessage());
		} 
	}
	
}