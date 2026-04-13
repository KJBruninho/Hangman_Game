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
    
    private void updateScore(int playerIdx, int points) {
        scores[playerIdx] += points;
        try {
        	players[playerIdx].send("\n__________________________________________________\n");
            players[playerIdx].send("	|PONTOS DE JOGADA: " + points + "\n	|TOTAL: " + scores[playerIdx]);
            players[playerIdx].send("__________________________________________________\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void atualizarPontos(int playerIdx, int pontosGanhos) {
        broadcast(Menus.gerarTabelaPontuacao("PONTUAÇÃO ATUAL", players, scores));
    }

    public synchronized void playInd() {

        try {
        	
        	broadcast("==================================================");
            broadcast("================  O JOGO COMEÇOU!  ===============");

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
	                current_Player.getSocket().setSoTimeout(0);
	                String guess = ((String) obj).toLowerCase();
	                
	                if (guess.length() == 1) {
	
	                    if (triedLetters.contains(guess.toUpperCase())) {
	
	                        current_Player.send("Essa letra já foi tentada.");
	                        updateScore(playerIndex, -1);
	                        turn++;
	                        continue;
	
	                    }
	
	                    triedLetters.add(guess.toUpperCase());
	
	                    correctL = word.guessLetter(guess);
	                    
	                    if (!correctL) {
	                        lives[playerIndex]--;
	
	                        broadcast("\n Jogador " + (playerIndex + 1) + " errou! \n"
	                        		+ " Vidas restantes: " + lives[playerIndex] 
	                        		+ "\n");
	                        updateScore(playerIndex, -5);
	                        
	                        if (lives[playerIndex] == 0) {
	                            broadcast("	Jogador " + (playerIndex + 1) +" foi eliminado!\n" + Menus.printVida(lives[playerIndex]));
	                            updateScore(playerIndex, -30);
	                        }
	                        
	                    } else {
	                        broadcast("\n Jogador " + (playerIndex + 1) + " acertou!");
	                        updateScore(playerIndex, 10);
	                    }
	
	                } else {                    	
	                    correctW = word.guessWord(guess);	                    

		                    if (!correctW) {
		                    	if(lives[playerIndex]>2)
		                    		lives[playerIndex]-= 2;
		                    	else {
		                    		lives[playerIndex]--;
		                    	}
		                        broadcast("\n Jogador " + (playerIndex + 1) + " tentou adivinhar sem sucesso! \n"
		                        		+ " Vidas restantes: " + lives[playerIndex] 
		                        		+ "\n");
		                        updateScore(playerIndex, -20);
		                        
		                        if (lives[playerIndex] == 0) {
		                            broadcast("	Jogador " + (playerIndex + 1) +" foi eliminado!\n" + Menus.printVida(lives[playerIndex]));
		                            updateScore(playerIndex, -50);
		                            atualizarPontos(playerIndex,scores[playerIndex]);
		                        }
		                        
		                    } else {
		                        broadcast("\n Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n"
		                        		+ " Muitos parabéns!\n");
		                        updateScore(playerIndex, 100);
		                        
		                    }	                    
	                }
	                turn++;
                }
                catch (java.net.SocketTimeoutException e) {
                	if(numPlayers>1) {                     		
                		current_Player.getSocket().setSoTimeout(0);
                		turn++;
                		broadcast("Tempo de jogo do Jogador " + (playerIndex + 1) + " terminou. (15s)\n");
                		updateScore(playerIndex, -2);
                	}
                }
            }

            broadcast(Menus.gerarTabelaPontuacao("RANKING FINAL - FIM DE JOGO", players, scores));
            broadcast(Menus.printFimJogo(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
         
    }
    
    public synchronized void playPart() {

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
	            
                try {
                	if (numPlayers > 1) {
                        current_Player.getSocket().setSoTimeout(15000);
                    } else {
                        current_Player.getSocket().setSoTimeout(0);
                    }
                	
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
	                current_Player.getSocket().setSoTimeout(0);
	                String guess = ((String) obj).toLowerCase();
	
	                if (guess.length() == 1) {
	
	                    if (triedLetters.contains(guess.toUpperCase())) {
	                        current_Player.send("Essa letra já foi tentada.");
	                        updateScore(playerIndex, -1);
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
	                        updateScore(playerIndex, -2);
	                    } else {                    	
	                        broadcast(" >Jogador " + (playerIndex + 1) + " acertou!");
	                        updateScore(playerIndex, 10);
	                    }
	
	                } else {                    	
	                    correctW = word.guessWord(guess);
		                    if (!correctW) {
		                    	if(livesPart>2)
		                    		livesPart -= 2;
		                    	else {
		                    		livesPart--;
		                    	}
		
		                        broadcast(" >Jogador " + (playerIndex + 1) + " tentou adivinhar sem sucesso! \n"
		                        		+ " >Vidas restantes: " + livesPart
		                        		+ "\n");
		                        updateScore(playerIndex, -25);
		                    } else {
		                    	
		                        broadcast(" >Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n"
		                        		+ " >Muitos parabéns!\n");
		                        updateScore(playerIndex, 70);
		                    }
	                    }
	                turn++;
	            }
                catch (java.net.SocketTimeoutException e) {
                	if(numPlayers>1) {
                		current_Player.getSocket().setSoTimeout(0);
                		turn++;
                		broadcast("Tempo de jogo do Jogador " + playerIndex + " terminou. (15s)\n");
                		updateScore(playerIndex, -2);
                	}                	
                }
            
            }

            broadcast(Menus.gerarTabelaPontuacao("RANKING FINAL - FIM DE JOGO", players, scores));
            broadcast(Menus.printFimJogo(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
    
    

}