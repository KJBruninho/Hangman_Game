package game;

import java.net.Socket;

import client.Client;

public class Game {
    
	private final int 	MAX_CAPACITY = 4;
    
    private Socket s;
    
    private Client[] 	players;
    private int 		capacity;      
    private int 		numPlayers;   
    
    public static void main(String[] args) {
    	
    }
    
    public Game(int size, Socket s) {
    	if(size>MAX_CAPACITY || size<1) {
    		this.capacity = MAX_CAPACITY;  
    		System.out.println("\n Tamanho da sala ultrapassado ou invalido! Ajustado para 4 jogadores.");
    	}
        this.players = new Client[size];
        this.numPlayers = 0;
        this.setSocket(s);
    }

    private boolean isFull() {
        return numPlayers >= capacity;
    }
    
    public void enterGame(Client player) {
        if (isFull()) {
            System.out.println("Sala " + "cheia!");
        } else {
            players[numPlayers] = player;
            numPlayers++;
            System.out.println("Jogador adicionado. Total: " + numPlayers + "/" + capacity);
        }
    }
    
    
    
	public Socket getSocket() {
		return s;
	}

	public void setSocket(Socket s) {
		this.s = s;
	}
    
}