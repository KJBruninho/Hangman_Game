package game;

import java.io.IOException;

import client.Client;
import utils.Ler;
import utils.Menus;

public class Game {
	
	private Lobby room;	
	private Lobby []rooms = new Lobby[1000];
	private int numRooms = 0;


	public static void main(String[] args) {
		
		Client client = new Client();
		
		System.out.println(Menus.printMenuInicial());
		
		boolean gate = true;
		while(gate) {
			int choice = Ler.umInt();
			switch (choice) {
				case 1:
					System.out.println("TODO");
					gate = false;
					break;
				case 2:
					System.out.println("TODO");
					gate = false;
					break;
				case 9:
					System.exit(0);
				default:
					System.out.println(Menus.printMenuInicial());
			}
		}
				
		Game g = new Game(3);
		System.out.println("Sala " + g.room.getUNIQUE_ID());
		
		try {
			client.getSocket().close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Game(int size) {
		room = new Lobby(size,generateUNIQUE_ID());
		rooms[numRooms] = room;
		numRooms++;
	}
	
	private int generateUNIQUE_ID() {
	    int id;
	    boolean existe;
	    
	    do {
	        existe = false;
	        id = (int) (Math.random() * 1000);
	        if(!(numRooms==0)) {	        	
		        for (Lobby room : rooms) {
		            if (room.getUNIQUE_ID() == id) {
		                existe = true;
		                break;
		            }
		        }
	        }
	    } while (existe);
	    
	    return id;
	}
}
