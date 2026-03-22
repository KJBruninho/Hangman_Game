package game;

import java.io.IOException;
import java.net.Socket;

import client.Client;

public class Game {

	public static void main(String[] args) {
		
		Client teste = new Client();
		Socket ClientSocket = teste.getSocket();
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
