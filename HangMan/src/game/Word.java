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
    private static final String[] FILE_PATH = {"palavrasF.txt","palavrasM.txt","palavrasD.txt"};
    private static final Random RANDOM = new Random();

// Constructors    
    public Word(int difficulty) {
        List<String> linhas = null;
        try {
            // Load word list based on difficulty
            switch (difficulty) {
                case 1:
                    linhas = Files.readAllLines(Paths.get(FILE_PATH[0]));
                    break;
                case 2:
                    linhas = Files.readAllLines(Paths.get(FILE_PATH[1]));
                    break;
                case 3:
                    linhas = Files.readAllLines(Paths.get(FILE_PATH[2]));
                    break;
                case 4:
                    linhas = Files.readAllLines(Paths.get(FILE_PATH[RANDOM.nextInt(3)]));
                    break;
                default:
                    linhas = Files.readAllLines(Paths.get(FILE_PATH[0]));
                    break;
            }

            if (linhas == null || linhas.isEmpty()) {
                throw new IOException("Word file is empty or not loaded.");
            }

            // Pick random word
            String chosen = linhas.get(RANDOM.nextInt(linhas.size())).trim();
            this.palavra = chosen.split("");

        } catch (IOException e) {
            System.err.println("Error loading words: " + e.getMessage());
            // Fallback word
            this.palavra = new String[]{"j", "a", "v", "a"};
        }

        this.guess = new String[palavra.length];
        Arrays.fill(this.guess, "_");
    }

// Methods    
    public String printGuess() {
        return String.join(" ", this.guess);
    }
    
 // Check if letter exists in word
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
    
 // Compare a full word with the guess
    public boolean guessWord(String word) {
        if (word == null) return false;
        
        boolean correct = word.equalsIgnoreCase(this.toString());
        
        if (correct) {
            this.guess = word.toLowerCase().split("");
        }
        
        return correct;
    }
    
 // Check if the entire word has been guessed
    public boolean isGuessed() {
        return Arrays.equals(palavra, guess);
    }
    
// Overridden Methods
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