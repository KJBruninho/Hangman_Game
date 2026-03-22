package game;

import client.Client;
import utils.Ler;
import utils.Menus;

public class Game {

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
				

		
	}
}
