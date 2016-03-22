package games.reversi;

import java.util.ArrayList;

import controller.GameController;
import model.BoardModel;
import games.GameRules;

/*
 * Game rules are set for the game Reverse. 
 */
public class GameRulesRVS extends GameRules {
	private BoardModel bm;
	private GameController gc;
	private ArrayList<int[]> flipMoves = new ArrayList<int[]>();
	
	public GameRulesRVS (BoardModel bm, GameController gc) {
		this.bm = bm;
		this.gc = gc;
		
		//first 4 boardvalues are pre-set
		bm.placeOnBoard(new int[]{3, 3}, 1);
		bm.placeOnBoard(new int[]{4, 4}, 1);
		bm.placeOnBoard(new int[]{3, 4}, 0);
		bm.placeOnBoard(new int[]{4, 3}, 0);
	}
	
	public boolean isLegalMove(int row, int col){
		return isLegalMove(row, col, bm, gc.getMySide());
	}
	
	//returns true if the move is valid, returns false if the move isn't.
	public boolean isLegalMove (int row, int col, BoardModel board, int side) {
		
		boolean valid = false;
		//look if the position is valid
		if(board.getBoardValue(row, col) == 2) {
			//look for adjacent position
			for(int[] surround : surroundingCoordinates(row, col)) {
				//the adjacent poision has to be of the opponent
				if(board.getBoardValue(surround[0], surround[1]) == getOpponent(side)){
					//now check if we come across with a color of ourselves
					int row_diff = surround[0] - row;
					int col_diff = surround[1] - col;					
					if(!valid && checkNext((row+row_diff), (col+col_diff), row_diff, col_diff, side, board) ){
						valid = true;
					}
				}
			}
		}
		return valid;
	}
	
	//creates a list with all surrounding board values
	public ArrayList<int[]> surroundingCoordinates(int row, int col){
			
		ArrayList<int[]> coordinates = new ArrayList<int[]>();
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				if(		
						(row+x >= 0) && 
						(col+y >= 0) &&
						(row+x < bm.getBoardSize()) &&
						(col+y < bm.getBoardSize()) &&
						(x != 0 || y != 0)
				){
					coordinates.add(new int[]{row+x, col+y});
				}
			}
		}
		return coordinates;
	}
		
	//kijk of er verderop ergens eentje van onze side is -> true
	//indien hij buiten het board valt -> false
	//indien hij een empty tegenkomt -> false
	//vult bij iedere stap de lijst ValidMoves, zodat we later de juiste shizzle in een lijst terug krijgen.
	public boolean checkNext(int row, int col, int row_diff, int col_diff, int side, BoardModel board) {
		if(
				(row < 0) || 
				(row >= board.getBoardSize()) || 
				(col < 0) || 
				(col >= board.getBoardSize())
		) {
			flipMoves.clear();
			return false;
		} else if(board.getBoardValue(row, col) == 2) {
			flipMoves.clear();
			return false;
		} else if(board.getBoardValue(row, col) == side) {
			return true;
		} else {
			flipMoves.add(new int[]{row, col});
			return checkNext((row+row_diff), (col+col_diff), row_diff, col_diff, side, board);
		}
	}

	//we kunnen niet altijd getOpponentSide gebruiken, omdat sommmige methodes ook
	//voor de opponent worden uitgevoerd (voor de opponent is de opponent: myside...)
	public int getOpponent(int MySide){
		int opponent = 0;
		if(MySide == 0) opponent = 1;
		return opponent;
	}
	
	public void playMove(int[] move, int side) {
		playMove(move, side, bm);
	}
	
	public void playMove(int[] move, int side, BoardModel board) {
		//eerst, plaats de daadwerkelijke zet
		board.placeOnBoard(move, side);
		
		//we kunnen niet getOpponentSide gebruiken, omdat deze ook gebruikt wordt 
		//voor de opponent. Daarom ff zo :)
		int opponent = 0;
		if(side == 0) opponent = 1;
		
		//nu gaan we alle shit omflippen...
		for(int[] surround : surroundingCoordinates(move[0], move[1])) {
			//zo'n aanliggende positie moet van de tegenstander zijn
			if(board.getBoardValue(surround[0], surround[1]) == opponent){
				//nu moeten we gaan kijken of we verderop w�l onzelf tegenkomen!
				//wat is de relatieve waarde van de aanliggende positie ten opzichte van de geteste positie?
				int row_diff = surround[0] - move[0];
				int col_diff = surround[1] - move[1];
				flipMoves.clear();
				if(checkNext((move[0]+row_diff), (move[1]+col_diff), row_diff, col_diff, side, board)){
					for(int[] flipMove : flipMoves){
						board.placeOnBoard(flipMove, side);
					}
					flipMoves.clear();
				}
			}
		}
	}

	@Override
	public boolean isAWin(int side) {
		// TODO Auto-generated method stub
		return false;
	}

}
