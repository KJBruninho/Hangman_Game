package game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class Word {
 
    private String[] palavra;
    private String[] guess;
    private static final String FILE_PATH = "palavras.txt";
    private static final Random RANDOM = new Random();

    public Word() {
        try {
            List<String> linhas = Files.readAllLines(Paths.get(FILE_PATH));
            if (linhas.isEmpty()) {
                throw new IOException("O ficheiro de palavras está vazio.");
            }
            String escolhida = linhas.get(RANDOM.nextInt(linhas.size())).trim();
            this.palavra = escolhida.split("");
        } catch (IOException e) {
            System.err.println("Erro ao carregar palavras: " + e.getMessage());
            this.palavra = new String[]{"j", "a", "v", "a","c"}; // Fallback
        }
        
        this.guess = new String[palavra.length];
        Arrays.fill(this.guess, "_");
    }

    public Word(String guessInput) {
        if (guessInput == null) guessInput = "";
        this.guess = guessInput.toLowerCase().split("");
    }

    public String printGuess() {
        return String.join(" ", this.guess);
    }

    public boolean guessLetter(String letter) {
        boolean found = false;
        if (letter == null || letter.isEmpty()) return false;
        
        String input = letter.toLowerCase();
        for (int i = 0; i < palavra.length; i++) {
            if (palavra[i].equalsIgnoreCase(input)) {
                guess[i] = palavra[i].toLowerCase();
                found = true;
            }
        }
        return found;
    }

    public boolean guessWord(Word anotherWord) {
        if (this.toString().equals(anotherWord.toString())) {
            this.guess = Arrays.copyOf(palavra, palavra.length);
            return true;
        }
        return false;
    }
    

    public boolean isGuessed() {
        return Arrays.equals(palavra, guess);
    }

    @Override
    public String toString() {
        return String.join("", this.palavra).toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Word other = (Word) obj;
        return Arrays.equals(this.palavra, other.palavra);
    }


}