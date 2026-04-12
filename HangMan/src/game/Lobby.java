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
    public void escolhaMenu(int choice) throws Exception {
        switch (choice) {
            case 1:
                msg.send(Menus.printGameLogo());
                Room room = new Room(1,4);
                rooms.add(room); 
                room.enterRoom(this.msg);
                room.join();
                processarPosJogo();
                break;
            case 2:
                msg.send(listRoomsInd());
                msg.send(" >Introduza o numero (ou pressione Enter para voltar:");
                Object received2 = msg.receive();
                if (received2 == null) return;
                try {
                	int id = Integer.parseInt((String) received2);
                	if (id >= 0 && id < roomsInd.size()) {
                		roomsInd.get(id).enterRoom(this.msg);
                		roomsInd.get(id).join();
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
            	
            	msg.send(" Escolha a Dificuldade: \n"
            			+ " [1]Facil.	(palavras de 3 a 4 letras)\n"
            			+ " [2]Media.	(palavras de 5 a 7 letras)\n"
            			+ " [3]Dificil.	(palavras de 7 ou mais letras)\n"
            			+ " [4]Aleatorio.");
            	
            	int option1 = Integer.parseInt((String) msg.receive());
            	
            	msg.send(" Quem tipo de sala quer criar?\n"
            			+" [0] Com vidas individuais entre Jogadores.\n"
            			+" [1] Com vida partilhada entre Jogadores.\n ");
            	
            	int option2 = Integer.parseInt((String) msg.receive());
            	
            	
            	msg.send(Menus.printGameLogo());
            	
            	Room newRoom = new Room(capacity,option1);
            	if(option2 == 0)
            		roomsInd.add(newRoom);
            	else
            		roomsPart.add(newRoom);
            	newRoom.enterRoom(this.msg);
            	newRoom.join();
            	
            	break;
            case 9:
                msg.getSocket().close();
                break;
            default:
            	return;
        } 
    }

    private Object listRoomsPart() {
    	for(Room room : rooms) {
    		if(room.getNumPlayers()==0)
    			rooms.remove(room);
    	}
        if (rooms.isEmpty()) 
        	return " Nao existem salas disponiveis.\n"
        		 + " Precione Enter tecla para voltar.\n";
        
        StringBuilder res = new StringBuilder("\nSalas:\n");
        
        for (int i = 0; i < rooms.size(); i++) {
            res.append("Sala ").append(i).append(" | ").append(rooms.get(i).getNumPlayers()).append("/").append(rooms.get(i).getCapacity()).append("\n");
        }
        return res.toString();
	}

	private String listRoomsInd() {
    	for(Room room : rooms) {
    		if(room.getNumPlayers()==0)
    			rooms.remove(room);
    	}
        if (rooms.isEmpty()) 
        	return " Nao existem salas disponiveis.\n"
        		 + " Precione Enter tecla para voltar.\n";
        
        StringBuilder res = new StringBuilder("\nSalas:\n");
        
        for (int i = 0; i < rooms.size(); i++) {
            res.append("Sala ").append(i).append(" | ").append(rooms.get(i).getNumPlayers()).append("/").append(rooms.get(i).getCapacity()).append("\n");
        }
        return res.toString();
    }

    private void processarPosJogo() throws Exception {
        Object res = msg.receive(); 
        if (res != null && res.toString().equals("9")) {
            msg.getSocket().close();
        }
    }
        
    @Override
    public void run() {
    	try {
            while (!msg.getSocket().isClosed()) {
                msg.send(Menus.printMenuLobby());
                Object input = msg.receive();
                if (input == null) break;
                escolhaMenu(Integer.parseInt((String) input));
            }
        } catch (Exception e) {
            System.out.println("Lobby encerrado.");
        }
    }
}