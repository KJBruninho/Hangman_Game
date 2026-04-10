package game;

import java.util.concurrent.Semaphore;
import utils.Message;

public class Room extends Thread {
    private final Message[] players;
    private final Message msg;
    private int numPlayers = 0;
    private final int capacity;
    private final Semaphore sem;

    public Room(int size, Message msg) {
        this.capacity = size;
        this.msg = msg;
        this.players = new Message[size];
        this.sem = new Semaphore(size);
        start();
    }

    public void enterRoom(Message playerMsg) {
        try {
            sem.acquire();
            synchronized (this) {
                players[numPlayers++] = playerMsg;
                if (numPlayers == capacity) notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (numPlayers < capacity) {
                try { wait(); } catch (InterruptedException e) { return; }
            }
        }
        new Game(players, msg).play();
    }

    public int getNumPlayers() { return numPlayers; }
    public int getCapacity() { return capacity; }
}