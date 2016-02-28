/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Test class for Server implementation with UDP mode of connection
 * 
 */


import java.io.IOException;
import java.util.Scanner;


public class TestConnect4Game_server {

	private static void printMessage()	{
		System.out.println("-h		---->	help");
		System.out.println("[-symbol 	symbol");
		System.out.println("[-host 		hostName");
		System.out.println(" -port 		port");
		System.out.println(" {-port 		port}");
		System.out.println("or ");
		System.out.println(" no argument");
		System.exit(0);
	   }


	public static void main(String[] args) throws ClassNotFoundException, IOException{
		
		char chosenSymbol='*';
		for (int i = 0; i < args.length; i ++) {
			   	if (args[i].equals("-h")) 
					printMessage();
			   	else if (args[i].equals("-symbol")) {
			   		chosenSymbol = args[++i].charAt(0);
			   	}
			}
		
	GameControl_server gameControl_server = new GameControl_server(args);
	Connect4UserView_server gameView = new Connect4UserView_server();
	gameControl_server.addObserver(gameView);
	Connect4GameModel_server gameBoard = new Connect4GameModel_server();
	System.out.println("Please select\n 1: human vs Computer game\n 2. Human vs Human");
	int input=1;
	Scanner myScanner = new Scanner(System.in);
	if(myScanner.hasNextLine()){
		input= myScanner.nextInt();
	}
	
	if(input == 1){
	PlayerModel_server compPlayer = new ComputerPlayer(chosenSymbol, "Comp_Player",gameBoard,gameControl_server);
	gameControl_server.addObserver(compPlayer);
	gameControl_server.addObserver(gameBoard);
	gameControl_server.init(gameBoard,compPlayer);
	
	}
	else{
		PlayerModel_server humPlayer = new HumanPlayer_server(chosenSymbol,"Human_Player1", gameBoard,gameControl_server);
		gameControl_server.addObserver(gameBoard);
		gameControl_server.init(gameBoard,humPlayer);
		
	}
}
}
