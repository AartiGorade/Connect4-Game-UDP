/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human Player client implementation with UDP mode of connection
 * 
 */

public class HumanPlayer_client1 extends PlayerModel_client1{
	public HumanPlayer_client1(char gameChar, String name, Connect4GameModel_client1 board,GameControl_client1 gameControl_client1) {
		super(gameChar, name, board, gameControl_client1);
	}
	private boolean needUserNextMove;
	private int userMove;
	
	public int makeNextMove(char[][] boardMatrix) {
		return userMove;
	}
	
	public void setUserMove(int i){
		userMove = i;
	}
	
	public boolean needUserMove(){
		return needUserNextMove;
	}
	public int makeNextMove() {
		return userMove;
	}



}
