package game;

import java.io.IOException;
import java.net.Socket;

import client.Client;
import utils.Message;

public class Game{
    
	private final int 	MAX_CAPACITY = 4;
    
    private Socket s;
    
    private Client[] 	players;
    private int 		capacity;      
    private int 		numPlayers;   
    
    public static void main(String[] args) {
    	Word word = new Word();
		System.out.println(word.toString());
	}
    
    public Game(int size, Socket s) {
    	if(size>MAX_CAPACITY || size<1) {
    		this.capacity = MAX_CAPACITY;  
    		System.out.println("\n Tamanho da sala ultrapassado ou invalido! Ajustado para 4 jogadores.");
    	}
    	
    	this.capacity = size;
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
    
    public void runSinglePlayerGame() {
    	
    	    Word word = new Word();
    	    System.out.println(word.toString());
    	    
    	    try {
    	        while(!word.isGuessed()) {
    	        	System.out.println(word.getGuess());
    	        	System.out.println(word.getWord());
    	            Message.sendMessage("Tente adivinhar uma letra ou a palavra:", s);
    	            String guess = (String) Message.receiveMessage(s);

    	            if(guess.length() == 1) {
    	            	word.guessLetter(guess);
    	            }
    	            else {
    	            	Word wordGuess = new Word (guess);
    	            	word.guessWord(wordGuess);
    	            }

    	            Message.sendMessage("Palavra atual: " + word.toString(), s);
    	        }

    	        Message.sendMessage("Parabéns! Você adivinhou a palavra!", s);
    	    } catch(IOException | ClassNotFoundException e) {
    	        e.printStackTrace();
    	    }
    	}
    	
    
    
	public Socket getSocket() {
		return s;
	}

	public void setSocket(Socket s) {
		this.s = s;
	}
	
	  
}