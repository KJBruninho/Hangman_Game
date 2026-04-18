package client;

import java.net.Socket;
import utils.Message;

public class Client_Read extends Thread {
    private Socket s;
    private Message msg;

//Constructors    
    public Client_Read(Socket s, Message msg) {
        this.s = s;
        this.msg = msg;
        start();
    }
    
//Overridden Methods
    @Override
    public void run() {
        try {
            while (!s.isClosed()) { 
                //Receives messages using the Message class's receive method and prints them to the console after converting them to strings. 
                //If the received object is null, it simply continues without printing.
                Object received = msg.receive();
                if (received != null) {
                    System.out.println(received.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Connection closed.");
        }
    }
}