/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Test class for client implementation with UDP mode of connection
 * 
 */


import java.io.IOException;
import java.util.Scanner;


public class TestConnect4Game_client1 {

	private static void printMessage()	{
		System.out.println("-h		---->	help");
		System.out.println("[-symbol 	symbol");
		System.out.println("[-playerName 		playerName");
		System.out.println("[-host 		hostName");
		System.out.println(" -port 		port");
		System.out.println(" {-port 		port}");
		System.out.println("or ");
		System.out.println(" no argument");
		System.exit(0);
	   }
	
	
	
	public static void main(String[] args){
	
	char chosenSymbol='+';
	String playerName="Human Player1";
	
	for (int i = 0; i < args.length; i ++) {
		   	if (args[i].equals("-h")) 
				printMessage();
		   	else if (args[i].equals("-symbol")) {
		   		chosenSymbol = args[++i].charAt(0);}
		   	else if (args[i].equals("-playerName")) {
		   		playerName = args[++i];}
		}
	
	GameControl_client1 gameControl_client1 = new GameControl_client1(args);
	Connect4UserView_client1 gameView = new Connect4UserView_client1();
	gameControl_client1.addObserver(gameView);
	
	Connect4GameModel_client1 gameBoard = new Connect4GameModel_client1();
	
	PlayerModel_client1 humPlayer = new HumanPlayer_client1(chosenSymbol,playerName, gameBoard,gameControl_client1);
	gameControl_client1.addObserver(gameBoard);
	try {
		gameControl_client1.init(gameBoard,humPlayer);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
