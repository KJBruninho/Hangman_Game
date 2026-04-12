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
    
    private long startTime;
    private final long maxWaitTime = 60000;

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
        start();
    }

    public void enterRoom(Message playerMsg) throws IOException {
        try {
            if (!sem.tryAcquire()) {
                playerMsg.send("A sala está cheia!");
                return;
            }
         
            synchronized (this) { 
            	players[numPlayers++] = playerMsg;

                playerMsg.send(" Entrou na sala (" + numPlayers + "/" + capacity + " jogadores).");
               
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

    public int getNumPlayers() { return numPlayers; }
    public int getCapacity() { return capacity; }
    
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
    	

    	broadcast("O jogo vai começar!\n");
    	switch(difficulty) {
    		case 1:
    			new Game(players,difficulty).playInd();
    			break;
    		case 2:
    			new Game(players,difficulty).playInd();
    			break;
    		case 3:
    			new Game(players,difficulty).playPart();
    			break;
    		case 4:
    			new Game(players,difficulty).playPart();
    			break;
    	}
    }
}