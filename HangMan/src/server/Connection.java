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
	
	public Lobby getLobby() {
		return l;
	}

	public void setLobby(Lobby l) {
		this.l = l;
	}
	
	@Override
    public void run () {
    	try {
    		Message msg = new Message(s);
    		msg.send("	Novo jogador! Seja bem-vindo.");
    		msg.send("	O jogo comecara em breve.");
    		msg.send("	A entrar num Lobby...");
    		setLobby(new Lobby(s,msg));
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
} 