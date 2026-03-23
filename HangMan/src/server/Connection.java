package server;

import java.io.*;
import java.net.*;

import game.Lobby;
import utils.Message; 

public class Connection extends Thread {
	
	private Socket s;
	
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
	
	@Override
    public void run () {
    	try {
    		Message.sendMessage("	Server:	Novo jogador! Seja bem-vindo.\n" + 
    							"	Server: O jogo comecara em breve."       +
    							"	Server: A entrar num Lobby..."			 +
    							"	Server: ", s);
    		   		
    		Lobby l = new Lobby(s);
    		
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
