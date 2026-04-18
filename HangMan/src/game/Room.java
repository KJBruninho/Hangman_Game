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

// Constructors    
    public Room(int size,int difficulty) {
    	// Ensure capacity is within valid limits
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
    
// Getters
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
    		return "Difficulty: Easy";
    	case 2:
    		return "Difficulty: Medium";
    	case 3:
    		return "Difficulty: Hard";
    	default:
    		return "Difficulty: Random";
    	}
    }

// Methods
    public void enterRoom(Message playerMsg) throws IOException {
        try {
            // Control room capacity using semaphore
            if (!sem.tryAcquire()) {
                playerMsg.send("Room is full!");
                return;
            }
         
            synchronized (this) { 
            	players[numPlayers++] = playerMsg;

                broadcast("Player " + numPlayers + " joined the room (" + numPlayers + "/" + capacity + ").");
               
                if (numPlayers < capacity) {
                    playerMsg.send(" Waiting for players or timeout...\n");
                }
                
                notifyAll();
            }

        } catch (Exception e) {
            System.err.println("Error entering room: " + e.getMessage());
        }
    }
    
    public synchronized void exitRoom(Message player) {
        numPlayers = 0;      
    }

// Send message to all players in room
    private void broadcast(String text) {
        for (Message p : players) {
            if (p != null) { 
                try {
                    p.send(text);
                } catch (IOException e) {
                    System.out.println("Broadcast error: " + e.getMessage());
                }
            }
        }
    }

// Thread execution (room life cycle)
    @Override
    public void run() {
    	synchronized (this) {
    		while (true) {
    			long now = System.currentTimeMillis();
    			long elapsed = now - startTime;
    			
    			// Start game if room is full
    			if (numPlayers == capacity) {
    				break;
    			}
    			
    			// Start if enough players and timeout reached
    			if (numPlayers >= 2 && elapsed >= maxWaitTime) {
    				break;
    			}
    			
    			// Cancel room if not enough players after timeout
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

    	// Start game
    	broadcast("Game is starting!\n");

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