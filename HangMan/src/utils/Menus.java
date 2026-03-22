package utils;

import java.util.ArrayList;

public class Menus {

    // REMOVER
    public static void main(String[] args) {
        System.out.println(printMenuInicial());

        ArrayList<String> letras = new ArrayList<>();
        letras.add("a");

        System.out.println(printLetrasTentadas(letras));
        System.out.println(STAGES[0]);
        System.out.println(printVida(5));
        System.out.println(printFimJogo(true, "JAVA"));
    }

    public static String printMenuInicial() {
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
        sb.append(" [1] Entrar no Lobby.\n");
        sb.append(" [2] HighScores.\n");
        sb.append(" [0] Sair.\n");
        sb.append("_".repeat(49));

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

    public static String printLetrasTentadas(ArrayList<String> letras) {
        StringBuilder barra = new StringBuilder();

        if (letras.isEmpty()) {
            barra.append("-");
        } else {
            for (String letra : letras) {
                barra.append(" [")
                     .append(letra.toUpperCase())
                     .append("] ");
            }
        }

        return " LETRAS TENTADAS: " + barra.toString();
    }

    public static String printVida(int vidasRestantes) {
        int maxVidas = 7;
        StringBuilder barra = new StringBuilder();

        for (int i = 0; i < maxVidas; i++) {
            if (i < vidasRestantes) {
                barra.append(" [♥] ");
            } else {
                barra.append(" [♡] ");
            }
        }

        return " VIDAS: " + barra.toString();
    }

    public static String printFimJogo(boolean ganhou, String palavraCorreta) {
        StringBuilder sb = new StringBuilder();

        sb.append("\nA palavra era: ").append(palavraCorreta).append("\n");
        sb.append("\n").append("=".repeat(49)).append("\n");

        if (ganhou) {
            sb.append("  	  ★ PARABÉNS! VOCÊ SOBREVIVEU  ★\n");
        } else {
            sb.append("            	  GAME OVER!!!			\n");
        }

        sb.append("=".repeat(49)).append("\n");
        sb.append(" [1] Jogar Novamente\n");
        sb.append(" [0] Menu Inicial\n");
        sb.append("_".repeat(49));

        return sb.toString();
    }
}