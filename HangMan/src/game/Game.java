package game;

import java.io.IOException;
import utils.Message;

public class Game {
    private int turn = 0;
    private final Message roomCreatorMsg;
    private final Word word = new Word();
    private final Message[] players;

    public Game(Message[] players, Message roomCreatorMsg) {
        this.players = players;
        this.roomCreatorMsg = roomCreatorMsg;
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

    public void play() {
        try {
            broadcast("\n=== O JOGO COMEÇOU ===");
            broadcast("A palavra tem " + word.toString().length() + " letras.");

            while (!word.isGuessed()) {
                Message current = players[turn % players.length];
                
                broadcast("\nTurno do jogador " + ((turn % players.length) + 1));
                broadcast("Palavra atual: " + word.printGuess());

                current.send("É a tua vez! Insere uma letra:");
                
                Object obj = current.receive();
                if (obj instanceof String) {
                    String guess = (String) obj;
                    if (guess.length() == 1) {
                        word.guessLetter(guess);
                    } else {
                        word.guessWord(new Word(guess));
                    }
                }
                turn++;
            }
            broadcast("\nPARABÉNS! A palavra era: " + word.toString());
        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }

	public Message getRoomCreatorMsg() {
		return roomCreatorMsg;
	}
}