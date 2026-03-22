package utils;

import java.util.ArrayList;

public class Menus {

	//REMOVER
    public static void main(String[] args) {
    	printMenuInicial();
    	ArrayList<String> letras = new ArrayList<String>();
    	letras.add("a");
    	printLetrasTentadas(letras);
    	System.out.println(STAGES[0]);
    }

    public static void printMenuInicial() {
        System.out.println("\n" + "=".repeat(49));
        System.out.println(" _                                            		\r\n"
        				 + "| |                                          		\r\n"
        				 + "| |__   __ _ _ __   __ _ _ __ ___   __ _ _ __  	  	\r\n"
        				 + "| '_ \\ / _` | '_ \\ / _` | '_ ` _ \\ / _` | '_ \\ 	\r\n"
        				 + "| | | | (_| | | | | (_| | | | | | | (_| | | | |		\r\n"
        				 + "|_| |_|\\__,_|_| |_|\\__, |_| |_| |_|\\__,_|_| |_|	\r\n"
        				 + "                    __/ |                      		\r\n"
        				 + "                   |___/                  				");
        System.out.println("=".repeat(49));
        System.out.println(" [1] Entrar no Lobby.");
        System.out.println(" [2] HighScores.");
        System.out.println(" [0] Sair.");
        System.out.println("_".repeat(49));
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
    
    public static void printLetrasTentadas(ArrayList<String> letras) {
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

        System.out.println(" LETRAS TENTADAS: " + barra.toString());
    }
  
    public static void printVida(int vidasRestantes) {
    	int maxVidas = 7;
    	StringBuilder barra = new StringBuilder();
            
    	for (int i = 0; i < maxVidas; i++) {
    		if (i < vidasRestantes) {
    			barra.append(" [♥] "); 
    		} else {
    			barra.append(" [♡] "); 
    		}
    	}

        System.out.println(" VIDAS: " + barra.toString());
    }



    public static void printFimJogo(boolean ganhou, String palavraCorreta) {
    	System.out.println("   A palavra era: " + palavraCorreta);
    	System.out.println("\n" + "=".repeat(49));
    	System.out.println("\n" + "=".repeat(49));
        if (ganhou) {
            System.out.println("   ★ PARABÉNS! VOCÊ SOBREVIVEU  ★");
        } else {
            System.out.println("   		  GAME OVER!!! ");
        }
        System.out.println("=".repeat(49));
        System.out.println(" [1] Jogar Novamente");
        System.out.println(" [0] Menu Inicial");
        System.out.println("_".repeat(49));
    }
}