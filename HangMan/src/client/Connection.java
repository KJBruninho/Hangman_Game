package client;

import java.io.*;
import java.net.*;

import utils.Message; 

public class Connection extends Thread {
	
	private Socket s;
	
	//Constructors
	public Connection(){
		super();
	}
	
	public Connection(Socket s){
		super();
		this.s = s;
	}
	
	@Override
    public void run () {
    	try {
    		Message.sendMessage("	Novo jogador! Seja bem-vindo.\n" + "	O jogo comecara em breve.", s);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
