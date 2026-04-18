package server;

import java.io.*;
import java.net.*;

import game.Lobby;
import utils.Message; 

public class Connection extends Thread {
	
	private Socket s;
	private Lobby l;
	
// Constructors
	public Connection(){
		super();
		start(); // Starts the thread
	}
	 
	public Connection(Socket s){
		super();
		this.s = s;
		start(); // Starts the thread with client socket
	}

// Getters and Setters
	public Lobby getLobby() {
		return l;
	}

	public void setLobby(Lobby l) {
		this.l = l;
	}

// Overridden Methods 	
	@Override
    public void run () {
    	try {
    		// Handles communication with the client
    		Message msg = new Message(s);
    		
    		msg.send(" > New player! Welcome.");
    		msg.send(" > Joining a lobby..."); 
    		
    		// Client creates a lobby
    		setLobby(new Lobby(msg));
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}