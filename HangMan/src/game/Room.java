package game;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import utils.Message;

public class Room extends Thread {
    private final Message[] players;
    private final Message msg;
    private int numPlayers = 0;
    private final int capacity;
    private final Semaphore sem;
    
    private final long startTime;
    private final long maxWaitTime = 60000;

    public Room(int size, Message msg) {
        this.capacity = size;
        this.msg = msg;
        this.players = new Message[size];
        this.sem = new Semaphore(size);
        this.startTime = System.currentTimeMillis();
        start();
    }

    public void enterRoom(Message playerMsg) throws IOException {
        try {
            if (!sem.tryAcquire()) {
                playerMsg.send("A sala está cheia!");
                return;
            }

            synchronized (this) {
                players[numPlayers++] = playerMsg;

                playerMsg.send("Entrou na sala (" + numPlayers + "/" + capacity + " jogadores).");
               
                if (numPlayers < capacity) {
                    playerMsg.send("Aguarde o preenchimento da sala ou o fim do tempo de espera.");
                }
                
                notifyAll();
            }

        } catch (Exception e) {
            System.err.println("Erro ao entrar na sala: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                long now = System.currentTimeMillis();
                long elapsed = now - startTime;

                if (numPlayers == capacity) {
                    break;
                }

                if (numPlayers >= 2 && elapsed >= maxWaitTime) {
                    break;
                }

                try {
                    long waitTime = Math.max(1, maxWaitTime - elapsed);
                    wait(waitTime); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return; 
                }
            }
        }
        
        broadcast("O jogo vai começar!\n");
        new Game(players, msg).play();
    }

    private void broadcast(String message) {
        for (int i = 0; i < numPlayers; i++) {
            try {
                players[i].send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getNumPlayers() { return numPlayers; }
    public int getCapacity() { return capacity; }
}