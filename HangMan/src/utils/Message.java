package utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Message {
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;

//Constructors    
    public Message(Socket s) throws IOException {
        this.setSocket(s);
        this.out = new ObjectOutputStream(s.getOutputStream());
        this.out.flush(); 
        this.in = new ObjectInputStream(s.getInputStream());
    }

//Getters ee Seetters
    public Socket getSocket() {
    	return s;
    }
    
    public void setSocket(Socket s) {
    	this.s = s;
    }
    public void send(Object obj) throws IOException {
        out.writeObject(obj); 
        out.flush();
    }
    
//Methods
    public Object receive() throws Exception {
        return in.readObject();
    }

    public static void closeSocket(Socket s) throws IOException {
        if (s != null && !s.isClosed()) {
            s.close();
        }
    }

}