package game;

import java.io.IOException;

import utils.Menus;
import utils.Message;

public class Game {

    private int turn = 0;

    private static final int MAX_LIVES = 7;

    private final int[] lives;

    private final Message roomCreatorMsg;
    private final Word word = new Word();
    private final Message[] players;

    public Game(Message[] players, Message roomCreatorMsg) {
        this.players = players;
        this.roomCreatorMsg = roomCreatorMsg;

        lives = new int[players.length];

        for (int i = 0; i < players.length; i++) {
            lives[i] = MAX_LIVES;
        }
    }

    private void broadcast(String text) {
        for (Message p : players) {
            try {
                p.send(text);
            } catch (IOException e) {
                System.out.println("Erro no broadcast: " + e.getMessage());
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

    public void play() {
        try {

            broadcast("\n=== O JOGO COMEÇOU ===");
            broadcast("A palavra tem " + word.toString().length() + " letras.");

            System.out.println(word);

            while (!word.isGuessed() && !allPlayersDead()) {

                int playerIndex = turn % players.length;

                if (lives[playerIndex] <= 0) {
                    turn++;
                    continue;
                }

                Message current = players[playerIndex];

                broadcast("\nTurno do jogador " + (playerIndex + 1));
                broadcast("Palavra atual: " + word.printGuess());                
                broadcast(Menus.printVida(lives[playerIndex]));


                current.send("É a tua vez! Insere uma letra ou palavra:");

                Object obj = current.receive();

                boolean correct = false;

                if (obj instanceof String) {

                    String guess = (String) obj;

                    if (guess.length() == 1) {

                        correct = word.guessLetter(guess);

                    } else {

                        correct = word.guessWord(new Word(guess));
                    }
                }

                if (!correct) {

                    lives[playerIndex]--;

                    broadcast(
                        "Jogador " + (playerIndex + 1) +
                        " errou! Vidas restantes: " + 
                        lives[playerIndex] + "\n" + Menus.printVida(lives[playerIndex])
                    );

                    if (lives[playerIndex] == 0) {
                        broadcast(
                            "Jogador " + (playerIndex + 1) +
                            " foi eliminado!\n"
                        );
                    }
                }

                turn++;
            }

            broadcast(Menus.printFimJogo(word.isGuessed(), word.toString()));
   
        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }

    public Message getRoomCreatorMsg() {
        return roomCreatorMsg;
    }
}