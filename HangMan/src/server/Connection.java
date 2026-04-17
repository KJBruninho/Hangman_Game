package server;

import java.io.*;
import java.net.*;

import game.Lobby;
import utils.Message; 

public class Connection extends Thread {
	
	private Socket s;
	private Lobby l;
	
//Constructors
	public Connection(){
		super();
		start();
	}
	 
	public Connection(Socket s){
		super();
		this.s = s;
		start();
	}

//Getters e Setters
	public Lobby getLobby() {
		return l;
	}

	public void setLobby(Lobby l) {
		this.l = l;
	}

//Overrided Methods 	
	@Override
    public void run () {
    	try {
    		Message msg = new Message(s);
    		msg.send("	> New Player! Welcome.");
    		msg.send("	> Entering a Lobby..."); 
    		setLobby(new Lobby(msg));
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
} 