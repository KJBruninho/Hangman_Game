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
    private final List<String> triedLetters;

    public Game(Message[] players , int difficulty) {
        this.players = players;

        lives = new int[players.length];

        for (int i = 0; i < players.length; i++) {
            lives[i] = MAX_LIVES;
        }
        
        livesPart = MAX_LIVES;
        word = new Word(difficulty);
        triedLetters = new ArrayList<>();
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

    private boolean allPlayersDead() {
        for (int life : lives) {
            if (life > 0) {
                return false;
            }
        }
        return true;
    }

    public void playInd() {

        try {
        	broadcast("==================================================");
            broadcast("================  O JOGO COMEÇOU!  ===============");

            System.out.println(word);

            while (!word.isGuessed() && !allPlayersDead()) {

                int playerIndex = turn % players.length;

                if (lives[playerIndex] <= 0 || players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];
                
                broadcast("==================================================");
                broadcast("				Turno do jogador " + (playerIndex + 1) + "\n");
                current_Player.send(Menus.printVida(lives[playerIndex]));
                broadcast("Palavra:  | " + word.printGuess() + " |");
                current_Player.send(Menus.printLetrasTentadas(triedLetters));
                current_Player.send("\n"
                		   + " >É a tua vez!\n"
                		   + " >Insere uma letra ou palavra: ");

                boolean correctL = false;
                boolean correctW = false;
                
                Object obj = current_Player.receive();
                String guess = ((String) obj).toLowerCase();

                if (guess.length() == 1) {

                    if (triedLetters.contains(guess.toUpperCase())) {

                        current_Player.send("Essa letra já foi tentada.");
                        turn++;
                        continue;

                    }

                    triedLetters.add(guess.toUpperCase());

                    correctL = word.guessLetter(guess);
                    
                    if (!correctL) {
                        lives[playerIndex]--;

                        broadcast(" >Jogador " + (playerIndex + 1) + " errou! \n"
                        		+ " >Vidas restantes: " + lives[playerIndex] 
                        		+ "\n");
                        
                        if (lives[playerIndex] == 0) {
                            broadcast("	>Jogador " + (playerIndex + 1) +" foi eliminado!\n" + Menus.printVida(lives[playerIndex]));
                        }
                        
                    } else {
                    	
                        broadcast(" >Jogador " + (playerIndex + 1) + " acertou!");
                    }

                } else {                    	
                    correctW = word.guessWord(guess);
                    
                    synchronized(this) {
	                    if (!correctW) {
	                    	if(lives[playerIndex]>2)
	                    		lives[playerIndex]-= 2;
	                    	else {
	                    		lives[playerIndex]--;
	                    	}
	                    	
	                        broadcast(" >Jogador " + (playerIndex + 1) + " tentou adivinhar sem sucesso! \n"
	                        		+ " >Vidas restantes: " + lives[playerIndex] 
	                        		+ "\n");
	                        
	                        if (lives[playerIndex] == 0) {
	                            broadcast("	>Jogador " + (playerIndex + 1) +" foi eliminado!\n" + Menus.printVida(lives[playerIndex]));
	                        }
	                        
	                    } else {
	                    	
	                        broadcast(" >Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n"
	                        		+ " >Muitos parabéns!\n");
	                        
	                    }
                    }
                }
            

                turn++; 
            }

            broadcast(Menus.printFimJogo(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
    
    public void playPart() {

        try {
        	broadcast("==================================================");
            broadcast("================  O JOGO COMEÇOU!  ===============");

            System.out.println(word);

            while (!word.isGuessed() && livesPart != 0) {

                int playerIndex = turn % players.length;

                if (players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];
                
                broadcast("==================================================");
                broadcast("				Turno do jogador " + (playerIndex + 1) + "\n");
                current_Player.send(Menus.printVida(livesPart));
                broadcast("Palavra:  | " + word.printGuess() + " |");
                current_Player.send(Menus.printLetrasTentadas(triedLetters));
                current_Player.send("\n"
                		   + " >É a tua vez!\n"
                		   + " >Insere uma letra ou palavra: ");

                boolean correctL = false;
                boolean correctW = false;
                
                Object obj = current_Player.receive();
                String guess = ((String) obj).toLowerCase();

                if (guess.length() == 1) {

                    if (triedLetters.contains(guess.toUpperCase())) {

                        current_Player.send("Essa letra já foi tentada.");
                        turn++;
                        continue;

                    }

                    triedLetters.add(guess.toUpperCase());

                    correctL = word.guessLetter(guess);
                    
                    if (!correctL) {
                    	livesPart--;
                        broadcast(" >Jogador " + (playerIndex + 1) + " errou! \n"
                        		+ " >Vidas restantes: " + livesPart
                        		+ "\n");
                        
                    } else {                    	
                        broadcast(" >Jogador " + (playerIndex + 1) + " acertou!");
                    }

                } else {                    	
                    correctW = word.guessWord(guess);
                    synchronized(this) {
	                    if (!correctW) {
	                    	if(livesPart>2)
	                    		livesPart -= 2;
	                    	else {
	                    		livesPart--;
	                    	}
	
	                        broadcast(" >Jogador " + (playerIndex + 1) + " tentou adivinhar sem sucesso! \n"
	                        		+ " >Vidas restantes: " + livesPart
	                        		+ "\n");
	                        
	                    } else {
	                    	
	                        broadcast(" >Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n"
	                        		+ " >Muitos parabéns!\n");
	                        
	                    }
                    }
                }
            

                turn++; 
            }

            broadcast(Menus.printFimJogo(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
    
    

}