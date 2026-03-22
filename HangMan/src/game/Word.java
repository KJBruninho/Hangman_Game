package game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            palavra = linhas.get(escolha).split("");         
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