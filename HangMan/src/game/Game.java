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

        // Initialize lives for each player
        for (int i = 0; i < players.length; i++) {
            lives[i] = MAX_LIVES;
        }

        this.livesPart = MAX_LIVES;
        this.word = new Word(difficulty);
        this.triedLetters = new ArrayList<>();
        this.scores = new int[players.length];
        this.numPlayers = players.length;

        // Count active players
        for (Message player : players) {
            if (player == null) --numPlayers;
        }
    }

// Send message to all connected players
    private void broadcast(String text) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                try {
                    players[i].send(text);
                } catch (IOException e) {
                    // Remove disconnected player
                    System.out.println("Player " + (i + 1) + " disconnected.");
                    players[i] = null;
                    numPlayers--;
                }
            }
        }
    }

    private boolean isGameOver(int mode) {
        // Mode 1: shared lives, otherwise individual lives
        if (mode == 1) { 
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
            // Send score update only to current player
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
            broadcast("GAME STARTED!");

            while (!word.isGuessed() && !isGameOver(mode)) {

                int playerIndex = turn % players.length;

                if (players[playerIndex] == null) {
                    turn++;
                    continue;
                }

                // Skip eliminated players in individual mode
                if (mode != 1 && lives[playerIndex] <= 0) {
                    turn++;
                    continue;
                }

                Message current_Player = players[playerIndex];

                try {
                    // Timeout only when multiple players are active
                    current_Player.getSocket().setSoTimeout(numPlayers > 1 ? 15000 : 0);

                    broadcast("Player " + (playerIndex + 1) + "'s turn");

                    int currentLives = (mode == 1) ? livesPart : lives[playerIndex];
                    current_Player.send(Menus.printLifes(currentLives));

                    broadcast("Word: | " + word.printGuess() + " |");

                    current_Player.send(Menus.printRoomType(triedLetters));
                    current_Player.send("Enter a letter or word:");

                    Object obj = current_Player.receive();
                    current_Player.getSocket().setSoTimeout(0);

                    if (obj == null) throw new IOException();

                    String guess = ((String) obj).toLowerCase();

                    // Letter guess
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

                            int remaining = (mode == 1) ? livesPart : lives[playerIndex];
                            broadcast("Player " + (playerIndex + 1) + " missed! Lives: " + remaining);

                            updateSelfScore(playerIndex, (mode == 1) ? -2 : -5);

                            if (remaining <= 0 && mode != 1) {
                                broadcast("Player " + (playerIndex + 1) + " eliminated!");
                                updateSelfScore(playerIndex, -30);
                            }

                        } else {
                            broadcast("Player " + (playerIndex + 1) + " hit!");
                            updateSelfScore(playerIndex, 10);
                        }

                    } 
                    // Word guess
                    else {
                        if (!word.guessWord(guess)) {

                            int penalty = (currentLives > 2) ? 2 : 1;
                            if (mode == 1) livesPart -= penalty; else lives[playerIndex] -= penalty;

                            int remaining = (mode == 1) ? livesPart : lives[playerIndex];
                            broadcast("Player " + (playerIndex + 1) + " failed word! Lives: " + remaining);

                            updateSelfScore(playerIndex, (mode == 1) ? -25 : -20);

                            if (remaining <= 0 && mode != 1) {
                                broadcast("Player " + (playerIndex + 1) + " eliminated!");
                                updateSelfScore(playerIndex, -50);
                            }

                        } else {
                            broadcast("Player " + (playerIndex + 1) + " guessed the word!");
                            updateSelfScore(playerIndex, (mode == 1) ? 70 : 100);
                        }
                    }

                    turn++;

                } catch (java.net.SocketTimeoutException e) {
                    // Player took too long
                    current_Player.getSocket().setSoTimeout(0);
                    broadcast("Player " + (playerIndex + 1) + " timed out.");
                    updateSelfScore(playerIndex, -2);
                    turn++;

                } catch (IOException e) {
                    // Handle player disconnection during turn
                    System.out.println("Player " + (playerIndex + 1) + " disconnected.");
                    players[playerIndex] = null;
                    numPlayers--;
                    turn++;
                }
            }

            // End game output
            broadcast(Menus.generateScoreboard("FINAL RANKING - GAME OVER", players, scores));
            broadcast(Menus.printEndGame(word.isGuessed(), word.toString()));

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
}