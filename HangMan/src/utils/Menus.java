package utils;

import java.util.ArrayList;
import java.util.List;

public final class Menus {
	
	private Menus(){
		throw new UnsupportedOperationException("Can't make an instance Obj of this class!");
	}
	
//Printable Methods
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
        sb.append(" [1] Single player game.\n");
        sb.append(" [2] Join a game with individual lives.\n"); 
        sb.append(" [3] Join a game with shared lives.\n");
        sb.append(" [4] Create a game (max 4 players).\n");
        sb.append(" [9] Exit.\n");
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
    			+ " [4] Random"
        ); 
        sb.append("\n").append("=".repeat(49)).append("\n");
        return sb.toString();
    }
	public static String printRoomType() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" Which room type do you want?\n"
    			+" [0] Individual lives per player\n"
    			+" [1] Shared lives between players\n"
        );
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

    public static String printRoomType(List<String> triedLetters) {
        StringBuilder str = new StringBuilder();

        if (triedLetters.isEmpty()) {
            str.append("-");
        } else {
            triedLetters.stream()
                .distinct()
                .forEach(letter -> {
                    str.append(" [")
                         .append(letter.toUpperCase())
                         .append("] ");
                });
        }

        return "TRIED LETTERS: " + str.toString();
    }

    public static String printLifes(int vidasRestantes) {
        int maxVidas = 7;
        StringBuilder barra = new StringBuilder();
        for (int i = 0; i < maxVidas; i++) {
            if (i < vidasRestantes) {
                barra.append(" [♥] ");
            } else {
                barra.append(" [♡] ");
            }
        }

        return "LIVES: " + barra.toString() + "\n"+ STAGES[maxVidas-vidasRestantes];
    }
    
    public static String generateScoreboard(String titulo, Message[] players, int[] scores) {
        StringBuilder sb = new StringBuilder();
        
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) indices.add(i);
        }

        indices.sort((a, b) -> Integer.compare(scores[b], scores[a]));

        sb.append("\n\t").append("╔═══════════════════════════════════════════╗").append("\n");
        sb.append("\t║ ").append(String.format("%-41s", titulo)).append(" ║").append("\n");
        sb.append("\t╠═══════════════════════╦═══════════════════╣").append("\n");
        sb.append("\t║       PLAYER          ║       SCORE       ║").append("\n");
        sb.append("\t╠═══════════════════════╬═══════════════════╣").append("\n");

        for (int idx : indices) {
            String nome = "Jogador " + (idx + 1);
            sb.append("\t║ ").append(String.format("%-21s", nome))
              .append(" ║ ").append(String.format("%17d", scores[idx]))
              .append(" ║").append("\n");
        }

        sb.append("\t╚═══════════════════════╩═══════════════════╝").append("\n");
        return sb.toString();
    }

    public static String printEndGame(boolean ganhou, String palavraCorreta) {
        StringBuilder sb = new StringBuilder();

        sb.append("\nThe word was: ").append(palavraCorreta).append("\n");
        sb.append("\n").append("=".repeat(49)).append("\n");

        if (ganhou) {
            sb.append("=================================================\n");
            sb.append("          ★ CONGRATULATIONS! YOU SURVIVED ★      \n");
            sb.append("=================================================\n");
            sb.append("          _____          __  __ ______           \n");
            sb.append("         / ____|   /\\   |  \\/  |  ____|          \n");
            sb.append("        | |  __   /  \\  | \\  / | |__             \n");
            sb.append("        | | |_ | / /\\ \\ | |\\/| |  __|            \n");
            sb.append("        | |__| |/ ____ \\| |  | | |____           \n");
            sb.append("         \\_____/_/    \\_\\_|  |_|______|          \n");
            sb.append("          ____      ________ _____               \n");
            sb.append("         / __ \\    / /  ____|  __   \\            \n");
            sb.append("        | |  | \\ \\  / /| |__  | |__) |           \n");
            sb.append("        | |  | |\\ \\/ / |  __| |  _  /            \n");
            sb.append("        | |__| | \\  /  | |____| | \\ \\            \n");
            sb.append("         \\____/   \\/   |______|_|  \\_\\           \n");
            sb.append("=================================================\n");
        } else {
            sb.append("==================================================\n");
            sb.append(" X Unfortunately, the tie was too tight! X      \n");
            sb.append("==================================================\n");
            sb.append("          _____          __  __ ______           \n");
            sb.append("         / ____|   /\\   |  \\/  |  ____|          \n");
            sb.append("        | |  __   /  \\  | \\  / | |__             \n");
            sb.append("        | | |_ | / /\\ \\ | |\\/| |  __|            \n");
            sb.append("        | |__| |/ ____ \\| |  | | |____           \n");
            sb.append("         \\_____/_/    \\_\\_|  |_|______|          \n");
            sb.append("          ____      ________ _____               \n");
            sb.append("         / __ \\    / /  ____|  __   \\            \n");
            sb.append("        | |  | \\ \\  / /| |__  | |__) |           \n");
            sb.append("        | |  | |\\ \\/ / |  __| |  _  /            \n");
            sb.append("        | |__| | \\  /  | |____| | \\ \\            \n");
            sb.append("         \\____/   \\/   |______|_|  \\_\\           \n");
            sb.append("==================================================\n");
        }
        sb.append("=".repeat(49)).append("\n");
        sb.append(" [1] Main Menu\n");
        sb.append(" [9] Exit\n");
        sb.append("_".repeat(49));
        
        return sb.toString();
    }
}