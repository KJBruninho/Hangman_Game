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
        sb.append("\n").append("=".repeat(49)).append(		   "\n");
        sb.append(" [1] Jogo individual. 		 		 		\n");
        sb.append(" [2] Juntar-me a um jogo com vida individual.\n"); 
        sb.append(" [3] Juntar-me a um jogo com vida partilhada.\n");
        sb.append(" [4] Criar um jogo. (max 4 jogadores) 		\n");
        sb.append(" [9] Sair.							 		\n");
        sb.append("_".repeat(49));

        return sb.toString();
    }
		
	public static String printDifficulty() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" Escolha o tamanho: \n"
    			+ " [1]Pequeno.	(palavras de 3 a 4 letras)\n"
    			+ " [2]Medio.	(palavras de 5 a 7 letras)\n"
    			+ " [3]Grande.	(palavras de 7 ou mais letras)\n"
    			+ " [4]Aleatorio."
        ); 
        sb.append("\n").append("=".repeat(49)).append(		   "\n");
        return sb.toString();
    }
	public static String printRoomType() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(49)).append("\n");
        sb.append(" Quem tipo de sala quer criar?\n"
    			+" [0] Com vidas individuais entre Jogadores.\n"
    			+" [1] Com vida partilhada entre Jogadores.\n "
        ); 
        sb.append("\n").append("=".repeat(49)).append(		   "\n");
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
        StringBuilder barra = new StringBuilder();

        if (triedLetters.isEmpty()) {
            barra.append("-");
        } else {
            triedLetters.stream()
                .distinct()
                .forEach(letra -> {
                    barra.append(" [")
                         .append(letra.toUpperCase())
                         .append("] ");
                });
        }

        return "LETRAS TENTADAS: " + barra.toString();
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

        return "VIDAS: " + barra.toString() + "\n"+ STAGES[maxVidas-vidasRestantes];
    }
    
    public static String generateScoreboard(String titulo, Message[] players, int[] scores) {
        StringBuilder sb = new StringBuilder();
        
        // Criar uma lista de índices para ordenar sem perder a referência ao jogador original
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) indices.add(i);
        }

        // Ordenar os índices com base nos scores (Descendente)
        indices.sort((a, b) -> Integer.compare(scores[b], scores[a]));

        sb.append("\n\t").append("╔═══════════════════════════════════════════╗").append("\n");
        sb.append("\t║ ").append(String.format("%-41s", titulo)).append(" ║").append("\n");
        sb.append("\t╠═══════════════════════╦═══════════════════╣").append("\n");
        sb.append("\t║       JOGADOR         ║      PONTOS       ║").append("\n");
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

        sb.append("\nA palavra era: ").append(palavraCorreta).append("\n");
        sb.append("\n").append("=".repeat(49)).append("\n");

        if (ganhou) {
            sb.append("=================================================\n");
            sb.append("          ★ PARABÉNS! VOCÊ SOBREVIVEU ★         \n");
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
            sb.append(" X Infelizmente a gravata estava muito apertada! X\n");
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
        sb.append(" [1] Menu Inicial\n");
        sb.append(" [9] Sair		\n");
        sb.append("_".repeat(49));

        return sb.toString();
    }
}