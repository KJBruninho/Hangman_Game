package game;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import utils.Menus;
import utils.Message;

public class Lobby extends Thread {
    private Socket s;
    private Message msg;
    private boolean inGame = false;
    private static final List<Room> rooms = new CopyOnWriteArrayList<>();

    public Lobby(Socket s, Message msg) {
        this.s = s;
        this.msg = msg;
        start();
    }

    public void escolhaMenu(int choice) throws Exception {
        switch (choice) {
            case 1:
                msg.send(Menus.printGameLogo());
                Room room = new Room(1, msg);
                rooms.add(room);
                room.enterRoom(this.msg);
                this.inGame = true; 
                break;
            case 2:
                msg.send(listRooms());
                msg.send("Introduza o numero da sala:");
                Object received = msg.receive();
                if (received == null) return;
                int id = Integer.parseInt((String) received);
                if (id >= 0 && id < rooms.size()) {
                    rooms.get(id).enterRoom(this.msg);
                    this.inGame = true; 
                }
                break;
            case 3:
                msg.send("Introduza a capacidade da sala:");
                
                Object capReceived = msg.receive();
                if (capReceived == null) return;

                int capacity; 
                try {
                    capacity = Integer.parseInt((String) capReceived);

                    if (capacity <= 0) {
                        msg.send("Capacidade invalida. Deve ser maior que 0.");
                        break;
                    }

                } catch (NumberFormatException e) {
                    msg.send("Valor invalido.");
                    break;
                }

                msg.send(Menus.printGameLogo());

                Room newRoom = new Room(capacity, msg);
                rooms.add(newRoom);

                newRoom.enterRoom(this.msg);
                this.inGame = true;

                break;
            case 9:
                s.close();
                break;
            default:
            	return;
        }
    }

    private String listRooms() {
        if (rooms.isEmpty()) return "Nao existem salas disponiveis.\n";
        StringBuilder res = new StringBuilder("\nSalas:\n");
        for (int i = 0; i < rooms.size(); i++) {
            res.append("Sala ").append(i).append(" | ").append(rooms.get(i).getNumPlayers()).append("/").append(rooms.get(i).getCapacity()).append("\n");
        }
        return res.toString();
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed() && !inGame) {
                msg.send(Menus.printMenuLobby());
                Object input = msg.receive();
                if (input == null) break;
                escolhaMenu(Integer.parseInt((String) input));
            }
        } catch (Exception e) {
            System.out.println("Lobby transferido para Game ou conexão fechada.");
        }
    }
}