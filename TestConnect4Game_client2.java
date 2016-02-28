import java.io.IOException;
import java.util.Scanner;


public class TestConnect4Game_client2 {

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
	String playerName="Human Player2";
	
	for (int i = 0; i < args.length; i ++) {
		   	if (args[i].equals("-h")) 
				printMessage();
		   	else if (args[i].equals("-symbol")) {
		   		chosenSymbol = args[++i].charAt(0);}
		   	else if (args[i].equals("-playerName")) {
		   		playerName = args[++i];}
		}
	
	GameControl_client2 gameControl_client2 = new GameControl_client2(args);
	Connect4UserView_client2 gameView = new Connect4UserView_client2();
	gameControl_client2.addObserver(gameView);
	
	Connect4GameModel_client2 gameBoard = new Connect4GameModel_client2();
	
	PlayerModel_client2 humPlayer = new HumanPlayer_client2(chosenSymbol,playerName, gameBoard,gameControl_client2);
	gameControl_client2.addObserver(gameBoard);
	try {
		gameControl_client2.init(gameBoard,humPlayer);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
