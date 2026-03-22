package server;

import java.io.*;
import java.net.*;

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
    		Message.sendMessage("	Server:	Novo jogador! Seja bem-vindo.\n" + "	Server: O jogo comecara em breve.", s);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
