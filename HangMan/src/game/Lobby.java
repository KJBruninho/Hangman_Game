package game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import utils.Menus;
import utils.Message;

public class Lobby extends Thread {
    private Message msg;
    private static final List<Room> rooms = new CopyOnWriteArrayList<>();
    private static final List<Room> roomsInd = new CopyOnWriteArrayList<>();
    private static final List<Room> roomsPart = new CopyOnWriteArrayList<>();
   
//Constructor
    public Lobby(Message msg) {
        this.msg = msg;
        start(); 
    }
    
//Methods
    public void chooseMenu(int choice) throws Exception {
        switch (choice) {
            case 1:
                msg.send(Menus.printGameLogo());
                Room room = new Room(1,4);
                rooms.add(room); 
                room.enterRoom(this.msg);
                room.join();
                processEndGame();
                break;
            case 2:
                msg.send(listRoomsInd());
                msg.send(" >Introduza o numero (ou pressione Enter para voltar):");
                Object received2 = msg.receive();
                if (received2 == null) return;
                try {
                	int id = Integer.parseInt((String) received2);
                	if (id >= 0 && id < roomsInd.size()) {
                		roomsInd.get(id).enterRoom(this.msg);
                		roomsInd.get(id).join();
                		if(roomsInd.get(id).getinGame())
                			processEndGame();
                	}
                }catch(Exception e){
                	return;
                }
                break;
            case 3:
                msg.send(listRoomsPart());
                msg.send(" >Introduza o numero:");
                Object received3 = msg.receive();
                if (received3 == null) return;
                try {
                	int id = Integer.parseInt((String) received3);
                	if (id >= 0 && id < roomsPart.size()) {
                		roomsPart.get(id).enterRoom(this.msg);
                		roomsPart.get(id).join();
                		if(roomsPart.get(id).getinGame())
                			processEndGame();
                	}
                }catch(Exception e){
                	return;
                }
                break;
            case 4:
            	msg.send("Introduza a capacidade da sala:");
            	
            	Object capReceived = msg.receive();
            	if (capReceived == null) return;
            	
            	int capacity; 
            	try {
            		capacity = Integer.parseInt((String) capReceived);
            	} catch (NumberFormatException e) {
            		msg.send("Valor invalido.");
            		break;
            	}
            	
            	msg.send(Menus.printDifficulty());
            	
            	int option1 = Integer.parseInt((String) msg.receive());
            	
            	msg.send(Menus.printRoomType());
            	
            	int option2 = Integer.parseInt((String) msg.receive());
            	
            	
            	msg.send(Menus.printGameLogo());
            	
            	Room newRoom = new Room(capacity,option1);
            	if(option2 == 0)
            		roomsInd.add(newRoom);
            	else
            		roomsPart.add(newRoom);
            	newRoom.enterRoom(this.msg);
            	newRoom.join();
            	processEndGame();
            	break;
            case 9:
                msg.getSocket().close();
                break;
            default:
            	return;
        } 
    }

    private Object listRoomsPart() {
    	for(Room room : roomsPart) {
    		if(room.getNumPlayers()==0)
    			roomsPart.remove(room);
    	}
        if (roomsPart.isEmpty()) 
        	return " Nao existem salas disponiveis.\n"
        		 + " Precione Enter tecla para voltar.\n";
        
        StringBuilder res = new StringBuilder("\nSalas:\n");
        
        for (int i = 0; i < roomsPart.size(); i++) {
            res.append("Sala ").append(i).append(" | ").append(roomsPart.get(i).getNumPlayers()).append("/").append(roomsPart.get(i).getCapacity()).append(" | ").append(roomsPart.get(i).getPrintDifficulty()).append("\n");
        }
        return res.toString();
	}

	private String listRoomsInd() {
    	for(Room room : roomsInd) {
    		if(room.getNumPlayers()==0)
    			roomsInd.remove(room);
    	}
        if (roomsInd.isEmpty()) 
        	return " Nao existem salas disponiveis.\n"
        		 + " Precione Enter tecla para voltar.\n";
        
        StringBuilder res = new StringBuilder("\nSalas:\n");
        
        for (int i = 0; i < roomsInd.size(); i++) {
            res.append("Sala ").append(i).append(" | ").append(roomsInd.get(i).getNumPlayers()).append("/").append(roomsInd.get(i).getCapacity()).append(" | ").append(roomsInd.get(i).getPrintDifficulty()).append("\n");
        }
        return res.toString();
    }

    private synchronized void processEndGame() throws Exception {
        Object res = msg.receive(); 
        if (res != null && res.toString().equals("9")) {
        	msg.getSocket().close();
        }
    	for(Room room : roomsPart) {
    		if(roomsPart.contains(room));
    			roomsPart.remove(room);
    	}
    	for (Room room : roomsInd) {
    		if(roomsInd.contains(room));
    			roomsInd.remove(room);
    	}
    }
        
//Overrided Methods 
    @Override
    public void run() {
    	try {
            while (!msg.getSocket().isClosed()) {
                msg.send(Menus.printMenuLobby());
                Object input = msg.receive();
                if (input == null) break;
                chooseMenu(Integer.parseInt((String) input));
            }
        } catch (Exception e) {
            System.out.println("Lobby encerrado.");
        }
    }
}