package games.tictactoe;

import java.util.ArrayList;

import controller.GameController;
import model.BoardModel;
import games.GameRules;

public class GameRulesTTT extends GameRules {
	private BoardModel bm;
	//private GameController gc;
	
	public GameRulesTTT (BoardModel bm, GameController gc) {
		this.bm = bm;
		//this.gc = gc;
	}
	public boolean isLegalMove (int x, int y, BoardModel board, int side) {
		return ((x >= 0 && x <= 3) && (y >= 0 && y <= 3) && (board.getBoard()[x][y] == bm.EMPTY));
	}
	
	/**
	 * Return if side has won
	 * @param side
	 * @return true on win
	 */
 	public boolean isAWin(int side) {
 		int[][][] winning = 			//met 1 van de volgende combo's win je
 			{
 				{{0,0}, {0,1}, {0,2}},	//horizontal
 				{{1,0}, {1,1}, {1,2}},	//h
 				{{2,0}, {2,1}, {2,2}},	//h
 				
 				{{0,0}, {1,0}, {2,0}},	//vertical
 				{{0,1}, {1,1}, {2,1}},	//v
 				{{0,2}, {1,2}, {2,2}},	//v
 				
 				{{0,0}, {1,1}, {2,2}},	//diagonal
 				{{0,2}, {1,1}, {2,0}}	//d
 			};
 		
 		int x = 9;   //0 (null) is used on the board, 9 isn't
 		int y = 9;
 		int win = 0; //we need a row of 3, stored in here.
 		
 		for (int[][] id : winning) {
 			for (int[] foo : id) {
 				for (int value : foo) {
 					if (x == 9)	{ x = value; }
 					else	  	{ y = value; }
 				}
 				if(bm.getBoard()[x][y] == side) {
 					win++;
 				}
 				x=9; //clear x coordinate
 			}
 			if(win == 3) return true;
 			win = 0;
 		}
 		return false;		
    }
 	
 	public ArrayList<int[]> surroundingCoordinates(int x, int y){
 		return null;
 	}

 	/**
	 * Play a move
	 * @param move, int[] with coordinates
	 * @param side, playing side
	 */
 	@Override
	public void playMove(int[] move, int side) {
		playMove(move, side, bm);
	}
	
 	@Override
	public void playMove(int[] move, int side, BoardModel bm) {
		bm.placeOnBoard(move, side);
	}
}
