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

public abstract class PlayerModel_client2 implements Observer,Runnable{
	private char gameChar;
	private String playerName;
	protected Connect4GameModel_client2 connect4Board;
	private GameControl_client2 gameControl_client2;
	
	
	public abstract int makeNextMove();
	
	public PlayerModel_client2(char gameChar, String name, Connect4GameModel_client2 board,GameControl_client2 gameControl_client2){
		this.gameChar=gameChar;
		playerName= name;
		connect4Board=board;
		this.gameControl_client2=gameControl_client2;
	}
	public char getGamePiece(){
		return gameChar;
	}
	public String getName(){
		return playerName;
	}
	public void update(Observable arg0, Object arg1) {
		if( arg1 instanceof Connect4GameModel_client2){
			connect4Board = (Connect4GameModel_client2)arg1;
		}
	}
	
	public void run(){
		try {
			gameControl_client2.playTheGame();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
