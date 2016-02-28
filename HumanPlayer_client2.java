/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human Player client implementation with UDP mode of connection
 * 
 */

public class HumanPlayer_client2 extends PlayerModel_client2{
	public HumanPlayer_client2(char gameChar, String name, Connect4GameModel_client2 board,GameControl_client2 gameControl_client2) {
		super(gameChar, name, board, gameControl_client2);
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
