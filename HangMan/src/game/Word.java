package game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Word {

    private String[] palavra;
    private int escolha;

    //Remover
	public static void main(String[] args ) throws ClassNotFoundException, IOException {
		Word newa = new Word("joker");
		newa.toString();
	}
	
	//Constructors
    public Word() {    	
    	escolha = (int) (Math.random() * 100);  
    	
    	try {
        	List<String> linhas = Files.readAllLines(Paths.get("palavras.txt"));
            palavra = linhas.get(escolha).split("");         
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public Word(String guess) {
    	palavra = guess.toLowerCase().split("");
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
            if (!(this.palavra[i].equals(guess.palavra[i]))) {
                return false;
            }
        }      
        return true;
        
    }
}