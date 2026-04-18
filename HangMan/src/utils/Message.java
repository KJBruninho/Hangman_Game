package utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Message {
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;

// Constructors    
    public Message(Socket s) throws IOException {
        this.setSocket(s);
        this.out = new ObjectOutputStream(s.getOutputStream());
        this.out.flush(); // Ensures stream header is sent
        this.in = new ObjectInputStream(s.getInputStream());
    }

// Getters and Setters
    public Socket getSocket() {
    	return s;
    }
    
    public void setSocket(Socket s) {
    	this.s = s;
    }

    public void send(Object obj) throws IOException {
        // Sends an object through the socket
        out.writeObject(obj); 
        out.flush();
    }
    
// Methods
    public Object receive() throws Exception {
        // Receives an object from the socket
        return in.readObject();
    }

    public static void closeSocket(Socket s) throws IOException {
        // Safely closes the socket
        if (s != null && !s.isClosed()) {
            s.close();
        }
    }
}