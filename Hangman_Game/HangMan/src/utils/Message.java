package utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Message {
	
	private Message() {
		throw new UnsupportedOperationException("Can't make an instance Obj of this class!");
	}
	
	public static void sendMessage(String message, Socket s) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(message);
		os.flush();
	}
	
	public static Object receiveMessage(Socket s) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		return is.readObject();
	}
	
	public static void closeSocket(Socket s) throws IOException {
		s.close();
	}

}
