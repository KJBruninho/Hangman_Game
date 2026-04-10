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

    private final Message roomCreatorMsg;
    private final Word word = new Word();
    private final Message[] players;

    // ArrayList de letras já tentadas
    private final List<String> triedLetters = new ArrayList<>();

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

    // Método para imprimir letras tentadas
    private String printTriedLetters() {

        if (triedLetters.isEmpty()) {
            return "Letras tentadas: nenhuma";
        }

        StringBuilder sb = new StringBuilder("Letras tentadas: ");

        for (String letter : triedLetters) {
            sb.append(letter).append(" ");
        }

        return sb.toString();
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
                broadcast(printTriedLetters());

                current.send("É a tua vez! Insere uma letra ou palavra:");

                Object obj = current.receive();

                boolean correct = false;

                if (obj instanceof String) {

                    String guess = ((String) obj).toLowerCase();

                    if (guess.length() == 1) {

                        // Verificar se já foi tentada
                        if (triedLetters.contains(guess)) {

                            current.send("Essa letra já foi tentada.");
                            continue;

                        }

                        // Guardar letra
                        triedLetters.add(guess);

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
                        lives[playerIndex]
                    );

                    broadcast(Menus.printVida(lives[playerIndex]));
                    broadcast(printTriedLetters());

                    if (lives[playerIndex] == 0) {

                        broadcast(
                            "Jogador " + (playerIndex + 1) +
                            " foi eliminado!"
                        );
                    }

                } else {

                    broadcast(
                        "Jogador " + (playerIndex + 1) +
                        " acertou!"
                    );

                    broadcast(printTriedLetters());
                }

                turn++;
            }

            broadcast(
                Menus.printFimJogo(
                    word.isGuessed(),
                    word.toString()
                )
            );

        } catch (Exception e) {

            System.out.println(
                "Game error: " + e.getMessage()
            );
        }
    }

    public Message getRoomCreatorMsg() {
        return roomCreatorMsg;
    }
}