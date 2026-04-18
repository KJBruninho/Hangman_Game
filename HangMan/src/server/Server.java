package server;

import java.io.*;
import java.net.*;

public class Server {

	private ServerSocket ss;
	private Socket s;
	private Connection c;	

	public Server() {
		try {
			// Listen for connections on port 5432
			ss = new ServerSocket(5432); 
			
			while(true) {
				// Accept incoming client
				s  = ss.accept();
				ss.setReuseAddress(true);
				
				// Handle client connection
				c  = new Connection(s);	
				
				if(c.isAlive()) {
					System.out.println("Connection made by " + s + " with " + c.getName());
				}
			}
				
		} catch(IOException e) {
			// Handle server errors
			System.out.println("Server: " + e.getMessage());
		} 
	}
	
}