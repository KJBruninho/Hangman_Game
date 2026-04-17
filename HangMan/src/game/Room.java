package game;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import utils.Message;

public class Room extends Thread {
	int MAX_CAPACITY = 4;
	
    private final Message[] players;
    private int numPlayers = 0;
    private final int capacity;
    private final Semaphore sem;
    private final int difficulty;
    private boolean inGame;
    
    private long startTime;
    private final long maxWaitTime = 60000;

//Constructors    
    public Room(int size,int difficulty) {
    	if(size>MAX_CAPACITY || size<1) {
    		this.capacity = MAX_CAPACITY;  
    	}else {
    		this.capacity = size;
    	}
        this.difficulty = difficulty;
        this.players = new Message[size];
        this.sem = new Semaphore(size);
        this.startTime = System.currentTimeMillis();
        this.inGame = false;
        start();
    }
    
//Getters and Setters    
    public int getDifficulty() { 
    	return difficulty; 
    }
    
    public int getNumPlayers() { 
    	return numPlayers; 
    }
    
    public int getCapacity() { 
    	return capacity; 
    }
    
    public boolean getinGame() {
    	return inGame;
    }

    public String getPrintDifficulty() {
    	switch (this.getDifficulty()) {
    	case 1:
    		return "Dificuldade: Facil";
    	case 2:
    		return "Dificuldade: Media";
    	case 3:
    		return "Dificuldade: Dificil";
    	default:
    		return "Dificuldade: Aleatorio";
    				
    	}
    }

//Methods
    public void enterRoom(Message playerMsg) throws IOException {
        try {
            if (!sem.tryAcquire()) {
                playerMsg.send("A sala está cheia!");
                return;
            }
         
            synchronized (this) { 
            	players[numPlayers++] = playerMsg;

                broadcast("Player"+ numPlayers + "  na sala (" + numPlayers + "/" + capacity + " jogadores).");
               
                if (numPlayers < capacity) {
                    playerMsg.send(" Aguarde o preenchimento da sala ou o fim do tempo de espera.\n");
                }
                
                notifyAll();
            }

        } catch (Exception e) {
            System.err.println("Erro ao entrar na sala: " + e.getMessage());
        }
    }
    
    public synchronized void exitRoom(Message player) {
        numPlayers=0;      
    }

    private void broadcast(String text) {
        for (Message p : players) {
            if (p != null) { 
                try {
                    p.send(text);
                } catch (IOException e) {
                    System.out.println("Erro no broadcast: " + e.getMessage());
                }
            }
        }
    }

//Overrided Methods    
    @Override
    public void run() {
    	synchronized (this) {
    		while (true) {
    			long now = System.currentTimeMillis();
    			long elapsed = now - startTime;
    			
    			if (numPlayers == capacity) {
    				break;
    			}
    			
    			if (numPlayers >= 2 && elapsed >= maxWaitTime) {
    				break;
    			}
    			
    	    	if(numPlayers < 2 && elapsed >= maxWaitTime) {
    	    		this.exitRoom(players[0]);
    	    		return;
    	    	}
    	    	
    			try {
    				long waitTime = Math.max(1, maxWaitTime - elapsed);
    				wait(waitTime); 
    			} catch (InterruptedException e) {
    				Thread.currentThread().interrupt();
    				return; 
    			}
    		}
    	}
    	
    	inGame = true;

    	broadcast("O jogo vai começar!\n");
    	switch(difficulty) {
    		case 1:
    			new Game(players,difficulty).play(0);
    			break;
    		case 2:
    			new Game(players,difficulty).play(0);
    			this.exitRoom(players[0]);
    			break;
    		case 3:
    			new Game(players,difficulty).play(1);
    			this.exitRoom(players[0]);
    			break;
    		case 4:
    			new Game(players,difficulty).play(1);
    			break;
    	}
    }
}