/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human Player client implementation with UDP mode of connection
 * 
 */

public class HumanPlayer_client3 extends PlayerModel_client3{
	public HumanPlayer_client3(char gameChar, String name, Connect4GameModel_client3 board,GameControl_client3 gameControl_client3) {
		super(gameChar, name, board, gameControl_client3);
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
