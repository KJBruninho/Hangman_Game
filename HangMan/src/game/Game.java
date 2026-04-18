package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.Menus;
import utils.Message;

public class Game {

    private int turn = 0;
    private static final int MAX_LIVES = 7;
    private final int[] lives;
    private int livesPart;
    private final Word word;
    private final Message[] players;
    private final List<String> triedLetters;
    private int numPlayers;
    private final int[] scores;

//Constructor
    public Game(Message[] players, int difficulty) {
        this.players = players;
        this.lives = new int[players.length];
        //Initializes lives. 
        //Note: For single-player mode, livesPart will track the remaining lives. 
        //      For multiplayer, each player's lives are tracked in the lives array.
        for (int i = 0; i < players.length; i++) {
            lives[i] = MAX_LIVES;
        }
        this.livesPart = MAX_LIVES;
        this.word = new Word(difficulty);
        this.triedLetters = new ArrayList<>();
        this.scores = new int[players.length];
        this.numPlayers = players.length;

        //Counts the number of non-null players.
        for (Message player : players) {
            if (player == null) 
                --numPlayers;
        }
    }

//Methods
    //Sends a message to all connected players. 
    //Note: If a player has disconnected it skips that player.
    private void broadcast(String text) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                try {
                    players[i].send(text);
                } catch (IOException e) {
                    System.out.println("Jogador " + (i + 1) + " desconectou-se.");
                    players[i] = null;
                    numPlayers--;
                }
            }
        }
    }

    private boolean isGameOver(int mode) {
        if (mode == 1) { 
            return livesPart <= 0;
        } else { 
            for (int life : lives) {
                if (life > 0) return false;
            }
            return true;
        }
    }

    //Updates the score of the current player and sends it to the player. 
    //Note: If the player has disconnected decrements the number of players.
    private void updateSelfScore(int playerIdx, int points) {
        if (players[playerIdx] == null) return;
        scores[playerIdx] += points;
        try {
            players[playerIdx].send("\n__________________________________________________\n"
            					  + "	|POINTS THIS MOVE: " + points + "\n	|TOTAL: " + scores[playerIdx]
            					  + "\n__________________________________________________\n");
        } catch (IOException e) {
            players[playerIdx] = null;
            numPlayers--;
        }
    }

    public synchronized void play(int mode) {
        try {
            broadcast("================================================");
            broadcast("================  GAME STARTED!  ===============");
            //Prints the word to the Server console for debug and testing purposes.
            System.out.println("Game Word: " + word); 

            while (!word.isGuessed() && !isGameOver(mode)) {
                int playerIndex = turn % players.length;

                //If the current player is null skips to the next turn.
                if (players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                //Skips the turn if the player is out of lives in multiplayer mode.
                if (mode != 1 && lives[playerIndex] <= 0) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];

                try {
                    //Sets a timeout for the current player's turn. 
                    //Note: In multiplayer mode, the timeout is 15 seconds. In single-player mode, there is no timeout (0 means infinite).
                    current_Player.getSocket().setSoTimeout(numPlayers > 1 ? 15000 : 0);

                    broadcast("==================================================");
                    broadcast("				     Player " + (playerIndex + 1) + " turn\n");
                    
                    int vidasAtuais = (mode == 1) ? livesPart : lives[playerIndex];
                    current_Player.send(Menus.printLifes(vidasAtuais));
                    broadcast("Palavra:  | " + word.printGuess() + " |");
                    current_Player.send(Menus.printRoomType(triedLetters));
                    current_Player.send(" >Enter a letter or word: ");

                    Object obj = current_Player.receive();
                    //Resets the timeout after receiving input(0 means infinite).
                    current_Player.getSocket().setSoTimeout(0);
                    if (obj == null) throw new IOException(); 
                    
                    String guess = ((String) obj).toLowerCase();

                    //Checks if the guess is a single letter or a full word.
                    if (guess.length() == 1) {
                        if (triedLetters.contains(guess.toUpperCase())) {                      
                            current_Player.send("Letter already tried.");
                            updateSelfScore(playerIndex, -1);
                            turn++;
                            continue;
                        }

                        triedLetters.add(guess.toUpperCase());
                        if (!word.guessLetter(guess)) {
                            if (mode == 1) livesPart--; else lives[playerIndex]--;
                            
                            int sobra = (mode == 1) ? livesPart : lives[playerIndex];
                            broadcast("\n Jogador " + (playerIndex + 1) + " errou! \n Vidas restantes: " + sobra + "\n");
                            updateSelfScore(playerIndex, (mode == 1) ? -2 : -5);

                            if (sobra <= 0 && mode != 1) {
                                broadcast("	Jogador " + (playerIndex + 1) + " foi eliminado!\n" + Menus.printLifes(0));
                                updateSelfScore(playerIndex, -30);
                            }
                        } else {
                            broadcast("\n Jogador " + (playerIndex + 1) + " acertou!");
                            updateSelfScore(playerIndex, 10);
                        }
                    } else {
                        if (!word.guessWord(guess)) {
                            int penalizacao = (vidasAtuais > 2) ? 2 : 1;
                            if (mode == 1) livesPart -= penalizacao; else lives[playerIndex] -= penalizacao;
                            
                            int sobra = (mode == 1) ? livesPart : lives[playerIndex];
                            broadcast("\n Jogador " + (playerIndex + 1) + " falhou a palavra! \n Vidas restantes: " + sobra + "\n");
                            updateSelfScore(playerIndex, (mode == 1) ? -25 : -20);

                            if (sobra <= 0 && mode != 1) {
                                broadcast("	Jogador " + (playerIndex + 1) + " foi eliminado!\n" + Menus.printLifes(0));
                                updateSelfScore(playerIndex, -50);
                            }
                        } else {
                            broadcast("\n Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n Parabéns!\n");
                            updateSelfScore(playerIndex, (mode == 1) ? 70 : 100);
                        }
                    }
                    turn++;

                } catch (java.net.SocketTimeoutException e) {
                    //If the player fails to respond within the time limit, it catches the timeout exception 
                    // and penalizes the player by deducting points.
                    //Note: Resets the timeout to avoid infinite waiting loop.
                    current_Player.getSocket().setSoTimeout(0);
                    broadcast("Tempo do Jogador " + (playerIndex + 1) + " terminou. (15s)\n");
                    updateSelfScore(playerIndex, -2);
                    turn++;
                } catch (IOException e) {
                    //Prints a message to the server console,and removes the player from the game.
                    System.out.println("Jogador " + (playerIndex + 1) + " caiu durante o turno.");
                    players[playerIndex] = null;
                    numPlayers--;
                    turn++;
                }
            }

            broadcast(Menus.generateScoreboard("FINAL RANKING - GAME OVER", players, scores));
            broadcast(Menus.printEndGame(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game Error: " + e.getMessage());
        }
    }
}