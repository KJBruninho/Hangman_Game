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
    		Message.sendMessage("	Novo jogador! Seja bem-vindo.", s);
    		Message.sendMessage("	O jogo comecara em breve.", s);
    		Message.sendMessage("	A entrar num Lobby...", s);
    		setLobby(new Lobby(s));
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
