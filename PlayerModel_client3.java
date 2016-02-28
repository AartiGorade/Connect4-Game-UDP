/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human Player client Model implementation with UDP mode of connection
 * 
 */

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class PlayerModel_client3 implements Observer,Runnable{
	private char gameChar;
	private String playerName;
	protected Connect4GameModel_client3 connect4Board;
	private GameControl_client3 gameControl_client3;
	
	
	public abstract int makeNextMove();
	
	public PlayerModel_client3(char gameChar, String name, Connect4GameModel_client3 board,GameControl_client3 gameControl_client3){
		this.gameChar=gameChar;
		playerName= name;
		connect4Board=board;
		this.gameControl_client3=gameControl_client3;
	}
	public char getGamePiece(){
		return gameChar;
	}
	public String getName(){
		return playerName;
	}
	public void update(Observable arg0, Object arg1) {
		if( arg1 instanceof Connect4GameModel_client3){
			connect4Board = (Connect4GameModel_client3)arg1;
		}
	}
	
	public void run(){
		try {
			gameControl_client3.playTheGame();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
