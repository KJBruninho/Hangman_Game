package utils;

import java.util.ArrayList;
import java.util.List;

public final class Menus {
	
	private Menus(){
		throw new UnsupportedOperationException("Can't instantiate this class!");
	}
	
// Printable Methods
	public static String printMenuLobby() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(
        	    " 	 _       _     _            	\n" +
        	    "	| |     | |   | |           	\n" +
        	    "	| | ___ | |__ | |__  _   _  	\n" +
        	    "	| |/ _ \\| '_ \\| '_ \\| | | |  \n" +
        	    "	| | (_) | |_) | |_) | |_| | 	\n" +
        	    "	|_|\\___/|_.__/|_.__/ \\__, | 	\n" +
        	    " 	                     __/  | 	\n" +
        	    "	                     |___/  	\n"
        ); 
        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" [1] Single player game\n");
        sb.append(" [2] Join game (individual lives)\n"); 
        sb.append(" [3] Join game (shared lives)\n");
        sb.append(" [4] Create game (max 4 players)\n");
        sb.append(" [9] Exit\n");
        sb.append("_".repeat(49));

        return sb.toString();
    }
		
	public static String printDifficulty() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" Choose size:\n"
    			+ " [1] Small (3 to 4 letter words)\n"
    			+ " [2] Medium (5 to 7 letter words)\n"
    			+ " [3] Large (7+ letter words)\n"
    			+ " [4] Random");
        sb.append("\n").append("=".repeat(49)).append("\n");
        return sb.toString();
    }

	public static String printRoomType() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" What type of room do you want to create?\n"
    			+ " [0] Individual lives per player\n"
    			+ " [1] Shared lives between players\n");
        sb.append("\n").append("=".repeat(49)).append("\n");
        return sb.toString();
    }
	
    public static String printGameLogo() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(
                " _                                            \n" +
                "| |                                           \n" +
                "| |__   __ _ _ __   __ _ _ __ ___   __ _ _ __\n" +
                "| '_ \\ / _` | '_ \\ / _` | '_ ` _ \\ / _` | '_ \\\n" +
                "| | | | (_| | | | | (_| | | | | | | (_| | | | |\n" +
                "|_| |_|\\__,_|_| |_|\\__, |_| |_| |_|\\__,_|_| |_|\n" +
                "                    __/ |\n" +
                "                   |___/\n"
        );
        sb.append("\n").append("=".repeat(49)).append("\n");

        return sb.toString();
    }

    public static final String[] STAGES = {
        """
          ____
         |/   |
         |    
         |    
         |    
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |    
         |    
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |    |
         |    
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |    |
         |    |
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |   /|
         |    |
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |   /|\\
         |    |
         |    
        _|_____
        """,
        """
          ____
         |/   |
         |   (_)
         |   /|\\
         |    |
         |   / 
        _|_____
        """,
        """
          ____
         |/   |
         |  (X_X)
         |   /|\\
         |    |
         |   / \\
        _|_____
        """
    };

    public static String printTriedLetters(List<String> triedLetters) {
        StringBuilder bar = new StringBuilder();

        if (triedLetters.isEmpty()) {
            bar.append("-");
        } else {
            triedLetters.stream()
                .distinct()
                .forEach(letter -> {
                    bar.append(" [")
                       .append(letter.toUpperCase())
                       .append("] ");
                });
        }

        return "TRIED LETTERS: " + bar.toString();
    }

    public static String printLives(int remainingLives) {
        int maxLives = 7;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < maxLives; i++) {
            if (i < remainingLives) {
                bar.append(" [♥] ");
            } else {
                bar.append(" [♡] ");
            }
        }

        return "LIVES: " + bar.toString() + "\n" + STAGES[maxLives - remainingLives];
    }
    
    public static String generateScoreboard(String title, Message[] players, int[] scores) {
        StringBuilder sb = new StringBuilder();
        
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) indices.add(i);
        }

        indices.sort((a, b) -> Integer.compare(scores[b], scores[a]));

        sb.append("\n\t").append("╔═══════════════════════════════════════════╗").append("\n");
        sb.append("\t║ ").append(String.format("%-41s", title)).append(" ║").append("\n");
        sb.append("\t╠═══════════════════════╦═══════════════════╣").append("\n");
        sb.append("\t║       PLAYER          ║      SCORE        ║").append("\n");
        sb.append("\t╠═══════════════════════╬═══════════════════╣").append("\n");

        for (int idx : indices) {
            String name = "Player " + (idx + 1);
            sb.append("\t║ ").append(String.format("%-21s", name))
              .append(" ║ ").append(String.format("%17d", scores[idx]))
              .append(" ║").append("\n");
        }

        sb.append("\t╚═══════════════════════╩═══════════════════╝").append("\n");
        return sb.toString();
    }

    public static String printEndGame(boolean won, String correctWord) {
        StringBuilder sb = new StringBuilder();

        sb.append("\nThe word was: ").append(correctWord).append("\n");
        sb.append("\n").append("=".repeat(49)).append("\n");

        if (won) {
            sb.append("=========== ★ CONGRATULATIONS! YOU SURVIVED ★ ===========\n");
        } else {
            sb.append("=========== X You lost! The rope was too tight! X =========\n");
        }

        sb.append("=".repeat(49)).append("\n");
        sb.append(" [1] Main Menu\n");
        sb.append(" [9] Exit\n");
        sb.append("_".repeat(49));

        return sb.toString();
    }
}