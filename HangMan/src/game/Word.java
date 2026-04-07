package game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

public class Word {

    private String[] palavra;
    private String[] guess = new String[5];
    private int escolha;

    //Remover
	public static void main(String[] args ) throws ClassNotFoundException, IOException {
		Word palavra = new Word();
		palavra.guessLetter("o");
		System.out.println(palavra.printGuess());
	}
	

	//Constructors
    public Word() {    	
    	escolha = (int) (Math.random() * 100); 

    	for (int i = 0; i < 5 ; i++) {
    	    guess[i] = "_";
    	}
    	
    	try {
        	List<String> linhas = Files.readAllLines(Paths.get("palavras.txt"));
        	if (!linhas.isEmpty()) {
                escolha = (int) (Math.random() * linhas.size());
                palavra = linhas.get(escolha).trim().split("");
            }     
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public Word(String guess) {
    	this.guess = guess.toLowerCase().split("");
    }
    
    public String printGuess() {
    	return String.join(" ", this.guess).toLowerCase();
    }
    
    
    public boolean guessWord(Word guess) {
    	if(this.equals(guess)) {
    		return true;
    	}
    	return false;
    }
    
    public String[] guessLetter(String letter) {

        for (int i = 0; i < palavra.length; i++) {
            if (palavra[i].equalsIgnoreCase(letter)) {
                guess[i] = letter;
            }
        }

        return guess;
    }
    
    public boolean isGuessed() {
        for (int i = 0; i < palavra.length; i++) {
            if (!palavra[i].equalsIgnoreCase(guess[i])) {
                return false; 
            }
        }
        return true;
    }
    
    public String getWord() {
    	return Arrays.toString(palavra);
    }
    
    public String getGuess() {
    	return Arrays.toString(guess);
    }
    

    @Override
    public String toString() {
    	return String.join("", this.palavra).toLowerCase();
    }
    
    @Override
    public boolean equals(Object obj) {
        
    	if (obj == null || this.getClass() != obj.getClass()) 
    		return false;
    	        
    	Word guess = (Word) obj;
        for (int i = 0; i < this.palavra.length; i++) {
            if (!(this.palavra[i].equals(guess.guess[i]))) {
                return false;
            }
        }      
        return true;
        
    }
}