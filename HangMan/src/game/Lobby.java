package game;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import utils.Menus;
import utils.Message;

public class Lobby extends Thread{
	
	private Socket s;
	private static final List<Socket> rooms = new CopyOnWriteArrayList<>();

	//Constructors
	public Lobby(Socket s) {
		this.s = s;
		rooms.add(s);
		start();
	}
	

	//Methods
	//TODO: Test and check logic
	public void escolhaMenu(int choice, Socket s) throws IOException, ClassNotFoundException {

			switch (choice) {
				case 1:
					Message.sendMessage(Menus.printGameLogo(), s);
					Game game = new Game(1,s);
					game.runSinglePlayerGame();
					break;
					
				case 2:
					Message.sendMessage(Menus.printGameLogo(), s);
					Message.sendMessage("Introduza o numero da sala: ", s);
					//
					//Game game = new Game();
					break;
				case 3:
					Message.sendMessage("Escolha o tamanho da sala: ", s);
					int size =(int) Message.receiveMessage(s);
					new Game(size, s);
					break;
				case 9:
					Message.closeSocket(s);
					System.exit(0);
				default:
					Message.sendMessage(Menus.printMenuLobby(), s);
			}
	
	}
	
	@Override
	public void run() {
		try {
			Message.sendMessage(Menus.printMenuLobby(), s);
			while(true) {
                int choice = Integer.parseInt((String) Message.receiveMessage(s));
                escolhaMenu(choice, s);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			rooms.remove(s);
		}
	}
}