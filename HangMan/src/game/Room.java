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
		//Ensures that the room capacity is between 1 and MAX_CAPACITY.
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
    		return "Difficulty: Easy";
    	case 2:
    		return "Difficulty: Medium";
    	case 3:
    		return "Difficulty: Hard";
    	default:
    		return "Difficulty: Random";
    				
    	}
    }

//Methods
    public void enterRoom(Message playerMsg) throws IOException {
        try {
            if (!sem.tryAcquire()) {
                playerMsg.send("The room is full!");
                return;
            }
         
            synchronized (this) { 
            	players[numPlayers++] = playerMsg;

                broadcast("Player"+ numPlayers + "  in the room (" + numPlayers + "/" + capacity + " players).");
               
                if (numPlayers < capacity) {
                    playerMsg.send(" Wait for the room to be filled or for the wait time to expire.\n");
                }
                
                notifyAll();
            }

        } catch (Exception e) {
            System.err.println("Room error: " + e.getMessage());
        }
    }
    
	//Removes a player from the room and updates the number of players.
	//Note: This mehod only equals the numPlayers to 0, since the LobbyThread only 
	// 		checks if numPlayers is 0 to remove the room from the list of rooms.
	// 		The Garbage Collector will handle the rest of the cleanup after the room is removed.
    public synchronized void exitRoom(Message player) {
        numPlayers=0;      
    }

    private void broadcast(String text) {
        for (Message p : players) {
            if (p != null) { 
                try {
                    p.send(text);
                } catch (IOException e) {
                    System.out.println("Room broadcast error: " + e.getMessage());
                }
            }
        }
    }

//Overridden Methods   
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