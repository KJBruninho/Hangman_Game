package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Menus;
import utils.Message;

public class Game {

    private int turn = 0;

    private static final int MAX_LIVES = 7;

    private final int[] 		lives;
    private int 				livesPart;
    private final Word 			word;
    private final Message[] 	players;
    private final List<String> 	triedLetters;
    private int					numPlayers;		
    private final int[]			scores;

//Constructor
    public Game(Message[] players , int difficulty) {
        this.players = players;

        this.lives = new int[players.length];

        for (int i = 0; i < players.length; i++) {
            lives[i] = MAX_LIVES;
        }
        
        this.livesPart = MAX_LIVES;
        this.word = new Word(difficulty);
        this.triedLetters = new ArrayList<>();
        this.scores = new int[players.length];
        this.numPlayers = players.length;
        
        for(Message player : players) {
        	if(player==null)
        		--numPlayers;
        }
    }

//Methods
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

    private boolean allPlayersDead() {
        for (int life : lives) {
            if (life > 0) {
                return false;
            }
        }
        return true;
    }
    
    private void updatePlayerScore(int playerIdx, int points) {
        scores[playerIdx] += points;
        try {
        	players[playerIdx].send("\n__________________________________________________\n");
            players[playerIdx].send("	|PLAY POINTS: " + points + "\n	|TOTAL: " + scores[playerIdx]);
            players[playerIdx].send("__________________________________________________\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void printUpdateScores(int playerIdx, int points) {
        broadcast(Menus.generateScoreboard("CURRENT SCORE", players, scores));
    }

    public synchronized void playInd() {

        try {
        	
        	broadcast("==================================================");
            broadcast("================  THE GAME HAS BEGUN!  ===============");

            System.out.println(word);

            while (!word.isGuessed() && !allPlayersDead()) {

                int playerIndex = turn % players.length;
                
                if(players[playerIndex] == null) {
                	turn++;
                }

                if (lives[playerIndex] <= 0) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];
                try {
                	if (numPlayers > 1) {
                        current_Player.getSocket().setSoTimeout(15000);
                    } else {
                        current_Player.getSocket().setSoTimeout(0);
                    }
	                
	                broadcast("==================================================");
	                broadcast("				Player " + (playerIndex + 1) + " turn" + "\n");
	                current_Player.send(Menus.printLives(lives[playerIndex]));
	                broadcast("Word:  | " + word.printGuess() + " |");
	                current_Player.send(Menus.printTriedLetters(triedLetters));
	                current_Player.send("\n"
	                		   + " >It's your turn!\n"
	                		   + " >Enter a letter or a word: ");
	
	                boolean correctL = false;
	                boolean correctW = false;
	                
	                Object obj = current_Player.receive();
	                current_Player.getSocket().setSoTimeout(0);
	                String guess = ((String) obj).toLowerCase();
	                
	                if (guess.length() == 1) {
	
	                    if (triedLetters.contains(guess.toUpperCase())) {
	
	                        current_Player.send("This letter has already been tried.");
	                        updatePlayerScore(playerIndex, -1);
	                        turn++;
	                        continue;
	
	                    }
	
	                    triedLetters.add(guess.toUpperCase());
	
	                    correctL = word.guessLetter(guess);
	                    
	                    if (!correctL) {
	                        lives[playerIndex]--;
	
	                        broadcast("\n Player " + (playerIndex + 1) + " failed! \n"
	                        		+ " Remaining lives: " + lives[playerIndex] 
	                        		+ "\n");
	                        updatePlayerScore(playerIndex, -5);
	                        
	                        if (lives[playerIndex] == 0) {
	                            broadcast("	Player " + (playerIndex + 1) +" was eliminated!\n" + Menus.printLives(lives[playerIndex]));
	                            updatePlayerScore(playerIndex, -30);
	                        }
	                        
	                    } else {
	                        broadcast("\n Player " + (playerIndex + 1) + " got it right!");
	                        updatePlayerScore(playerIndex, 10);
	                    }
	
	                } else {                    	
	                    correctW = word.guessWord(guess);	                    

		                    if (!correctW) {
		                    	if(lives[playerIndex]>2)
		                    		lives[playerIndex]-= 2;
		                    	else {
		                    		lives[playerIndex]--;
		                    	}
		                        broadcast("\n Player " + (playerIndex + 1) + " tried to guess but was unsuccessful! \n"
		                        		+ " Remaining lives: " + lives[playerIndex] 
		                        		+ "\n");
		                        updatePlayerScore(playerIndex, -20);
		                        
		                        if (lives[playerIndex] == 0) {
		                            broadcast("	Player " + (playerIndex + 1) +" was eliminated!\n" + Menus.printLives(lives[playerIndex]));
		                            updatePlayerScore(playerIndex, -50);
		                            printUpdateScores(playerIndex,scores[playerIndex]);
		                        }
		                        
		                    } else {
		                        broadcast("\n Player " + (playerIndex + 1) + " guessed the word!\n"
		                        		+ " Congratulations!\n");
		                        updatePlayerScore(playerIndex, 100);
		                        
		                    }	                    
	                }
	                turn++;
                }
                catch (java.net.SocketTimeoutException e) {
                	if(numPlayers>1) {                     		
                		current_Player.getSocket().setSoTimeout(0);
                		turn++;
                		broadcast("Player " + playerIndex + " game time has ended. (15s)\n");
                		updatePlayerScore(playerIndex, -2);
                	}
                }
            }

            broadcast(Menus.generateScoreboard("FINAL RANKING - GAME ENDED", players, scores));
            broadcast(Menus.printEndGame(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
         
    }
    
    public synchronized void playPart() {

        try {
        	broadcast("==================================================");
            broadcast("================  THE GAME HAS BEGUN!  ===============");

            System.out.println(word);

            while (!word.isGuessed() && livesPart != 0) {

                int playerIndex = turn % players.length;

                if (players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];
	            
                try {
                	if (numPlayers > 1) {
                        current_Player.getSocket().setSoTimeout(15000);
                    } else {
                        current_Player.getSocket().setSoTimeout(0);
                    }
                	
	                broadcast("==================================================");
	                broadcast("				Player " + (playerIndex + 1) + " turn" + "\n");
	                current_Player.send(Menus.printLives(livesPart));
	                broadcast("Word:  | " + word.printGuess() + " |");
	                current_Player.send(Menus.printTriedLetters(triedLetters));
	                current_Player.send("\n"
	                		   + " >It's your turn!\n"
	                		   + " >Enter a letter or a word: ");
	
	                boolean correctL = false;
	                boolean correctW = false;
	                
	                Object obj = current_Player.receive();
	                current_Player.getSocket().setSoTimeout(0);
	                String guess = ((String) obj).toLowerCase();
	
	                if (guess.length() == 1) {
	
	                    if (triedLetters.contains(guess.toUpperCase())) {
	                        current_Player.send("This letter has already been tried.");
	                        updatePlayerScore(playerIndex, -1);
	                        turn++;
	                        continue;
	
	                    }
	
	                    triedLetters.add(guess.toUpperCase());
	
	                    correctL = word.guessLetter(guess);
	                    
	                    if (!correctL) {
	                    	livesPart--;
	                    	
	                        broadcast(" >Player " + (playerIndex + 1) + " failed! \n"
	                        		+ " >Remaining lives: " + livesPart
	                        		+ "\n");
	                        updatePlayerScore(playerIndex, -2);
	                    } else {                    	
	                        broadcast(" >Player " + (playerIndex + 1) + " got it right!");
	                        updatePlayerScore(playerIndex, 10);
	                    }
	
	                } else {                    	
	                    correctW = word.guessWord(guess);
		                    if (!correctW) {
		                    	if(livesPart>2)
		                    		livesPart -= 2;
		                    	else {
		                    		livesPart--;
		                    	}
		
		                        broadcast(" >Player " + (playerIndex + 1) + " tried to guess but was unsuccessful! \n"
		                        		+ " >Remaining lives: " + livesPart
		                        		+ "\n");
		                        updatePlayerScore(playerIndex, -25);
		                    } else {
		                    	
		                        broadcast(" >Player " + (playerIndex + 1) + " guessed the word!\n"
		                        		+ " >Congratulations!\n");
		                        updatePlayerScore(playerIndex, 70);
		                    }
	                    }
	                turn++;
	            }
                catch (java.net.SocketTimeoutException e) {
                	if(numPlayers>1) {
                		current_Player.getSocket().setSoTimeout(0);
                		turn++;
                		broadcast("Player " + playerIndex + " game time has ended. (15s)\n");
                		updatePlayerScore(playerIndex, -2);
                	}                	
                }
            
            }

            broadcast(Menus.generateScoreboard("FINAL RANKING - GAME ENDED", players, scores));
            broadcast(Menus.printEndGame(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
    
    

}