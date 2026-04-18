package server;

public final class Origin {

	public static void main(String[] args ) {
		 //Application starts the server
		new Server();
		}
	 
	private Origin(){
		throw new UnsupportedOperationException("Can't make an instance Obj of this class!");
	}
}