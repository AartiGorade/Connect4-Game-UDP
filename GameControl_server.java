/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Server Control implementation with UDP mode of connection
 * 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

class ClientServerData {

	public int name;
	public InetAddress address;
	public int port;
	private final int ID;
	public int attempt = 0;
	

	public ClientServerData(int name, InetAddress address, int port,final int ID) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setport(int port){
		this.port=port;
	}
	
}



public class GameControl_server extends Observable{

	private int currentUserMove;
	private boolean isGameTie;
	private boolean isGameWin;
	private Connect4GameModel_server connect4Board;
	private PlayerModel_server myPlayers[];
	private PlayerModel_server currentPlayer;
	private char currentPlayerGamePiece;
	private char opponetGamePiece;
	private String gameMessage;
	private static int index=0;
	private DatagramSocket aServerSocket;
	private static String hostName; 
	private static int	port = 1800;
	private static int numPLayers=4;
	private final int myNumber=0;
	private static int clientNumber=1;
	private InetAddress ip;
	private List<ClientServerData> clients = new ArrayList<ClientServerData>();
	
	public GameControl_server(String args[]){

		if ( args.length != 0 ){
			parseArgs(args);
			}
			
		try { 
			System.out.println("Trying to bind on to port: "+port);
            aServerSocket = new DatagramSocket(port);
            
            System.out.println ("Listening on port: " + aServerSocket.getLocalPort());
            waitForConnection();
            System.out.println("WaitForConnection is over");
            
        } catch(Exception e) {
            System.out.println(e);
        }
		
	}
	
	private void waitForConnection() {
		System.out.println("Waiting for all Players to connect...");
		int i=0;
		while(i<(numPLayers-1)){
			String ready2="0";
			try {
				ready2 = getConnectionData();
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ready2.equals("1")){	
				System.out.println("Server Player started");
			}
			else{
				System.out.println("Client not created, try again");
			}
			i++;
		}
		
		

		String ready="1";
		int count=0;
		
		try {
			sendToAll(ready);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Number of Client Players connected = "+clients.size());
	}

	private void printMessage()	{
		System.out.println("-h		---->	help");
		System.out.println("[-host 		hostName");
		System.out.println(" -port 		port");
		System.out.println(" {-port 		port}");
		System.out.println("or ");
		System.out.println(" no argument");
		System.exit(0);
	   }
	
	public void parseArgs(String args[]) {
		for (int i = 0; i < args.length; i ++) {
		   	if (args[i].equals("-h")) {
				printMessage();
		   	}
		   	else if (args[i].equals("-host")) {
		   		hostName = args[++i];
		   		}
		   	else if (args[i].equals("-port")) {
		   		port = new Integer(args[++i]).intValue();
		   	}
		}
	   }
	
	
	public void sendData(final byte[] data, final InetAddress address, final int port) throws IOException{
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			aServerSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendToAll(String message) throws IOException{
		byte[] data=message.getBytes();
		
		for (int i = 0; i < clients.size(); i++) {
			ClientServerData client = clients.get(i);
			sendData(data, client.address, client.port);
		}
	}
	
	
	public String getConnectionData() throws ClassNotFoundException, IOException{
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			aServerSocket.receive(packet);
			clients.add(new ClientServerData(clientNumber, packet.getAddress(), packet.getPort(),(clientNumber+clientNumber) ));
			clientNumber+=1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		String string = new String(packet.getData()).trim();
		return string;
	}
	
	public String getData() throws ClassNotFoundException, IOException{
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			aServerSocket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String string = new String(packet.getData()).trim();
		return string;
	}
	
	public void closeConnections() throws IOException{
		aServerSocket.close();
	}

	/**
	 * Plays the actual game Player-1 and 2 makes their moves
	 * If the player is Human Player, notifies View for input move.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 * 
	 */
	
		
	public void playTheGame() throws ClassNotFoundException, IOException, InterruptedException {
		int count=0;
		int whosTurn=0;
		String currentStatus = null;	
		try{
			do {	
				// Set the Player's move and gamePiece
				gameMessage = null;
				index=index%2;
				currentPlayerGamePiece = myPlayers[index].getGamePiece();
				currentPlayer = myPlayers[index];
				setChanged();
				notifyObservers(myPlayers[index]);
				
				// Set the Player's move and gamePiece
				currentUserMove = myPlayers[index].makeNextMove();
				
			
			for(int turn=1;turn<numPLayers;turn++){
				whosTurn=turn;
				setChanged();
				notifyObservers(index);
				setChanged();
				notifyObservers(this);
				if (isGameTie || isGameWin) {
					if ((whosTurn-1)==myNumber){
					gameMessage = "Game Won by Player "
							+ myPlayers[index].getName();
					}
					else{
						gameMessage = "Game Won by another player ";
						System.out.println(gameMessage);
					}
					setChanged();
					notifyObservers(this);
					setChanged();
					notifyObservers(gameMessage);
					currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;;
					sendToAll(currentStatus);
					sendToAll(gameMessage);
					break;
				}
				if (gameMessage != null) {
					setChanged();
					notifyObservers(gameMessage);
				}
				
				
				currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;
				//send current move to all other players
				sendToAll(currentStatus);
				
				// wait for data from particular player
				
				currentStatus=getData();
				
				String[] tokens=currentStatus.split("\\s+");
				if(tokens[0].equals("Game")){
					System.out.println(currentStatus);
					System.out.println(getData());
					break;
				}
				 currentUserMove=Integer.parseInt(tokens[0]);
				 currentPlayerGamePiece=tokens[1].charAt(0);
				 whosTurn=Integer.parseInt(tokens[2]);
				 
			}
			setChanged();
			notifyObservers(index);
			setChanged();
			notifyObservers(this);
			sendToAll(currentStatus);
			if (isGameTie || isGameWin) {
				if ((whosTurn-1)==myNumber){
				gameMessage = "Game Won by Player "
						+ myPlayers[index].getName();
				}
				else{
					gameMessage = "Game Won by another player ";
					System.out.println(gameMessage);
				}
			}
		} while (!(isGameTie || isGameWin));
		}catch(Exception e){
				sendToAll(currentStatus);
				
		}
		finally{
			System.out.println("Thank you for playing!!!");
			closeConnections();
			
		}
		
	
	}

	public PlayerModel_server getCurrentPlayer() {
		return currentPlayer;
	}

	public char getCurrentGamePiece() {
		return currentPlayerGamePiece;
	}

	public char getOpponentGamePiece() {
		return opponetGamePiece;
	}

	public int getCurrentgameMove() {
		return currentUserMove;
	}

	public void init(Connect4GameModel_server gameBoard, PlayerModel_server p1) throws IOException, ClassNotFoundException {
		
		connect4Board = gameBoard;
		myPlayers = new PlayerModel_server[2];
		myPlayers[0] = p1;
		
		new Thread(myPlayers[0]).start();
		
	}

	public Connect4GameModel_server getGameBoard() {
		return connect4Board;
	}

	public void setGameTie() {
		isGameTie = true;
	}

	public void setGameError(String error) {
		gameMessage = error;
	}

	public void setGameWin() {
		isGameWin = true;
	}
	
}
