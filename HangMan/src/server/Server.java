package server;

import java.io.*;
import java.net.*;

import game.Lobby;

public class Server {

	private ServerSocket ss;
	private Socket s;
	private Connection c;	
	private Lobby l;
	
	public Server() {
		try {
			ss = new ServerSocket(5432);
			while(true) {
				s  = ss.accept();
				c  = new Connection(s);	
				if(c.isAlive()) {
					System.out.println("Connection made by " + s + " with "+c.getName());
				}
				l = new Lobby(s);
			}
				
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
}
