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

    public Game(Message[] players, int difficulty) {
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

        for (Message player : players) {
            if (player == null) --numPlayers;
        }
    }

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

    private boolean isGameOver(int modo) {
        if (modo == 1) { 
            return livesPart <= 0;
        } else { 
            for (int life : lives) {
                if (life > 0) return false;
            }
            return true;
        }
    }

    private void updateSelfScore(int playerIdx, int points) {
        if (players[playerIdx] == null) return;
        scores[playerIdx] += points;
        try {
            players[playerIdx].send("\n__________________________________________________\n"
            					  + "	|PONTOS DE JOGADA: " + points + "\n	|TOTAL: " + scores[playerIdx]
            					  + "\n__________________________________________________\n");
        } catch (IOException e) {
            players[playerIdx] = null;
            numPlayers--;
        }
    }

    public synchronized void play(int modo) {
        try {
            broadcast("==================================================");
            broadcast("================  O JOGO COMEÇOU!  ===============");
            System.out.println("Solução: " + word); 

            while (!word.isGuessed() && !isGameOver(modo)) {
                int playerIndex = turn % players.length;

                if (players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                if (modo != 1 && lives[playerIndex] <= 0) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];

                try {
                    current_Player.getSocket().setSoTimeout(numPlayers > 1 ? 15000 : 0);

                    broadcast("==================================================");
                    broadcast("				Turno do jogador " + (playerIndex + 1) + "\n");
                    
                    int vidasAtuais = (modo == 1) ? livesPart : lives[playerIndex];
                    current_Player.send(Menus.printLifes(vidasAtuais));
                    broadcast("Palavra:  | " + word.printGuess() + " |");
                    current_Player.send(Menus.printRoomType(triedLetters));
                    current_Player.send("\n >É a tua vez!\n >Insere uma letra ou palavra: ");

                    Object obj = current_Player.receive();
                    current_Player.getSocket().setSoTimeout(0);
                    if (obj == null) throw new IOException(); 
                    
                    String guess = ((String) obj).toLowerCase();

                    if (guess.length() == 1) {
                        if (triedLetters.contains(guess.toUpperCase())) {
                            current_Player.send("Essa letra já foi tentada.");
                            updateSelfScore(playerIndex, -1);
                            turn++;
                            continue;
                        }

                        triedLetters.add(guess.toUpperCase());
                        if (!word.guessLetter(guess)) {
                            if (modo == 1) livesPart--; else lives[playerIndex]--;
                            
                            int sobra = (modo == 1) ? livesPart : lives[playerIndex];
                            broadcast("\n Jogador " + (playerIndex + 1) + " errou! \n Vidas restantes: " + sobra + "\n");
                            updateSelfScore(playerIndex, (modo == 1) ? -2 : -5);

                            if (sobra <= 0 && modo != 1) {
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
                            if (modo == 1) livesPart -= penalizacao; else lives[playerIndex] -= penalizacao;
                            
                            int sobra = (modo == 1) ? livesPart : lives[playerIndex];
                            broadcast("\n Jogador " + (playerIndex + 1) + " falhou a palavra! \n Vidas restantes: " + sobra + "\n");
                            updateSelfScore(playerIndex, (modo == 1) ? -25 : -20);

                            if (sobra <= 0 && modo != 1) {
                                broadcast("	Jogador " + (playerIndex + 1) + " foi eliminado!\n" + Menus.printLifes(0));
                                updateSelfScore(playerIndex, -50);
                            }
                        } else {
                            broadcast("\n Jogador " + (playerIndex + 1) + " adivinhou a palavra!\n Parabéns!\n");
                            updateSelfScore(playerIndex, (modo == 1) ? 70 : 100);
                        }
                    }
                    turn++;

                } catch (java.net.SocketTimeoutException e) {
                    current_Player.getSocket().setSoTimeout(0);
                    broadcast("Tempo do Jogador " + (playerIndex + 1) + " terminou. (15s)\n");
                    updateSelfScore(playerIndex, -2);
                    turn++;
                } catch (IOException e) {
                    System.out.println("Jogador " + (playerIndex + 1) + " caiu durante o turno.");
                    players[playerIndex] = null;
                    numPlayers--;
                    turn++;
                }
            }

            broadcast(Menus.generateScoreboard("RANKING FINAL - FIM DE JOGO", players, scores));
            broadcast(Menus.printEndGame(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Erro crítico no jogo: " + e.getMessage());
        }
    }
}