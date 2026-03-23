package game;

import java.io.IOException;
import java.net.Socket;

import utils.Menus;
import utils.Message;

public class Lobby extends Thread{
	
	private Socket 	s;
	private Game 	room;	
	private Game[] 	rooms 	 = new Game[10000];//Provavelmente extremamente overkill
	private int 	numRooms = 0;

	//Constructors
	public Lobby(Socket s) {
		this.s = s;
	}
	
	public Lobby(int size, Socket s) {
		this.s = s;
		room = new Game(size,generateUNIQUE_ID());
		rooms[numRooms] = room;
		numRooms++;
	}
	
	//Methods
	private int generateUNIQUE_ID() {
	    int id;
	    boolean existe;
	    
	    do {
	        existe = false;
	        id = (int) (Math.random() * 100000000);
	        if(!(numRooms==0)) {	        	
		        for (Game room : rooms) {
		            if (room.getUNIQUE_ID() == id) {
		                existe = true;
		                break;
		            }
		        }
	        }
	    } while (existe);
	    
	    return id;
	}
	
	//TODO: Test and check logic
	public String escolhaMenu(int choice, Socket s) throws IOException, ClassNotFoundException {

			switch (choice) {
				case 1:
					Message.sendMessage(Menus.printGameLogo(), s);
					new Game(1,generateUNIQUE_ID());
					break;
				case 2:
					Message.sendMessage(Menus.printGameLogo(), s);
					Message.sendMessage("Introduza o numero da sala: ", s);
					
					break;
				case 3:
					Message.sendMessage("Escolha o tamanho da sala: ", s);
					int size =(int) Message.receiveMessage(s);
					Game game = new Game(size, generateUNIQUE_ID());
					break;
				case 9:
					System.exit(0);
				default:
					Message.sendMessage(Menus.printMenuLobby(), s);
			}

		return null;	
	}
	
	@Override
	public void run() {
		try {
			Message.sendMessage(Menus.printMenuLobby(), s);
			while(true) {
				int choice = (int) Message.receiveMessage(s);
				escolhaMenu(choice,s);	
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}