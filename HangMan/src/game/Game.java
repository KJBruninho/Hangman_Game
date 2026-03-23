package game;

import client.Client;

public class Game {
    
	private final int 	MAX_CAPACITY = 4;
    private final int	UNIQUE_ID;
    
    private Client[] 	players;
    private int 		capacity;      
    private int 		numPlayers;   
    
    public static void main(String[] args) {
    	
    }
    
    public Game(int size, int roomID) {
        this.UNIQUE_ID = roomID;
        if(size>MAX_CAPACITY) {
        	this.capacity = MAX_CAPACITY;  
        	System.out.println("\n Tamanho da sala ultrapassado! Ajustado para 4 jogadores.");
        }
        this.players = new Client[size];
        this.numPlayers = 0;
    }

    private boolean isFull() {
        return numPlayers >= capacity;
    }
    
    public void enterGame(Client player) {
        if (isFull()) {
            System.out.println("Sala " + UNIQUE_ID + " cheia!");
        } else {
            players[numPlayers] = player;
            numPlayers++;
            System.out.println("Jogador adicionado. Total: " + numPlayers + "/" + capacity);
        }
    }
    
    

    public int getUNIQUE_ID() {
    	return UNIQUE_ID; }
    
}