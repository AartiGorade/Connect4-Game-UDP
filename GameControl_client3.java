/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Client Control implementation with UDP mode of connection
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
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;



public class GameControl_client3 extends Observable{

	private int currentUserMove;
	private boolean isGameTie;
	private boolean isGameWin;
	private Connect4GameModel_client3 connect4Board;
	private PlayerModel_client3 myPlayers[];
	private PlayerModel_client3 currentPlayer;
	private char currentPlayerGamePiece;
	private char opponetGamePiece;
	private String gameMessage;
	private static int index=0;
	private ServerSocket aServerSocket;
	private DatagramSocket client;
	String hostName = "192.168.0.6";
	private static int	port = 1800;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private final int myNumber=3;
	private InetAddress ip;
	
	public GameControl_client3(String args[]){
		if ( args.length != 0 )
			parseArgs(args);
		try { 
            connectToServer();
            
        } catch(Exception e) {
            System.out.println(e);
        }	
	}
	
	
	private void printMessage()	{
		System.out.println("-h		---->	help");
		System.out.println("[-host 		hostName");
		System.out.println(" -port 		port");
		System.out.println(" {-port 		port}");
		System.out.println("or ");
		System.out.println(" no argument");
	   }
	
	public void parseArgs(String args[]) {

		for (int i = 0; i < args.length; i ++) {
			
		   	if (args[i].equals("-h")) 
				printMessage();
		   	else if (args[i].equals("-host")) {
		   		hostName = args[++i];
		   		try {
					ip = InetAddress.getByName(hostName);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		   	}
		   	else if (args[i].equals("-port")) {
				port = new Integer(args[++i]).intValue();
		   	}
		}
	   }
	
	private String getData() throws ClassNotFoundException, IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			client.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String string = new String(packet.getData()).trim();
		return string;
	}
	
	public void sendData(String message) throws IOException{
		
		byte[] data=message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connectToServer() throws IOException {
		System.out.println("I m trying to connect to server");
		client = new DatagramSocket();
		System.out.println("Connected");
		
	}
	
	
	public void closeConnections() throws IOException{
		client.close();
	}

	/**
	 * Plays the actual game Player-1 and 2 makes their moves
	 * If the player is Human Player, notifies View for input move.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	
		
	public void playTheGame() throws ClassNotFoundException, IOException {
		int whosTurn=3;
		try{
		do {
					 String data=getData();
					 String[] tokens=data.split("\\s+");
					 if(tokens[0].equals("Game")){
						 System.out.println(data);
						 System.out.println(getData());
						 break;
					 }
					 currentUserMove=Integer.parseInt(tokens[0]);
					 currentPlayerGamePiece=tokens[1].charAt(0);
					 whosTurn=Integer.parseInt(tokens[2]);
					 
					 if (!((whosTurn-1)%4==myNumber) && !(whosTurn==0)) { 					
						setChanged();
						notifyObservers(index);
						
						setChanged();
						notifyObservers(this);
						
					 }
					 
					if (isGameTie || isGameWin) {
						if ((whosTurn-1)==myNumber){
							gameMessage = "Game Won by Player "
									+ myPlayers[index].getName();
							}
							else{
								gameMessage = "Game Won by another Player ";
							}
						setChanged();
						notifyObservers(this);
						setChanged();
						notifyObservers(gameMessage);
						String currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;
						sendData(currentStatus);
						sendData(gameMessage);
						break;
					}
					if (gameMessage != null) {
						setChanged();
						notifyObservers(gameMessage);
					}
		
				
					if(whosTurn==myNumber) {
						// Set the Player's move and gamePiece
						System.out.println("It's MY TURN");
						gameMessage = null;
						
						currentPlayerGamePiece = myPlayers[index].getGamePiece();
						currentPlayer = myPlayers[index];
						setChanged();
						notifyObservers(myPlayers[index]);
						
						// Set the Player's move and gamePiece
						currentUserMove = myPlayers[index].makeNextMove();
						whosTurn=(whosTurn+1)%4;
						String currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;
						
						sendData(currentStatus);
						setChanged();
						notifyObservers(index);
						
						setChanged();
						notifyObservers(this);
						
						if (isGameTie || isGameWin) {
							if (((whosTurn-1)==myNumber)||(whosTurn==0)){
								gameMessage = "Game Won by Player "
										+ myPlayers[index].getName();
								}
								else{
									gameMessage = "Game Won by another Player ";
								}
							setChanged();
							notifyObservers(this);
							setChanged();
							notifyObservers(gameMessage);
							currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;
							sendData(currentStatus);
							sendData(gameMessage);
							break;
						}
						if (gameMessage != null) {
							setChanged();
							notifyObservers(gameMessage);
						}
					 }

		} while (!(isGameTie || isGameWin));
	}catch (Exception e){
	 try{
			if (isGameTie || isGameWin) {
				if (((whosTurn-1)==myNumber)||(whosTurn==0)){
					gameMessage = "Game Won by Player "
							+ myPlayers[index].getName();
					}
					else{
						gameMessage = "Game Won by another Player ";
					}
				sendData(gameMessage);
			
			}
		}catch(Exception e1){
			
		}
	 
	 finally{
		 System.out.println("Game won by your opponent");	
	 }
	}
		finally{
			System.out.println("Thank you for playing,GoodBye!!!");
			closeConnections();
		}
	}

	

	public PlayerModel_client3 getCurrentPlayer() {
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

	public void init(Connect4GameModel_client3 gameBoard, PlayerModel_client3 p2) throws ClassNotFoundException, IOException {
		connect4Board = gameBoard;
		myPlayers = new PlayerModel_client3[2];
		myPlayers[0] = p2;
		String r="1";
		sendData(r);
		String ready=getData();
		
		if(ready.equals("1")){
			
			new Thread(myPlayers[0]).start();
			System.out.println("Client player started");
		}
		
	}

	public Connect4GameModel_client3 getGameBoard() {
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
