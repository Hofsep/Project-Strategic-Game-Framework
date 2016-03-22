package games.tictactoe;

import games.AI;
import games.GameRules;

import java.util.HashMap;
import java.util.Map;

import controller.GameController;
import model.BoardModel;

public class AITTT extends AI {
	private Map<int[][], Integer> positions = new HashMap<int[][], Integer>();
	private BoardModel bm;
	private GameRules gameRules;
	private GameController gc;
	
	public AITTT (BoardModel bm, GameRules gameRules, GameController gc) {
		this.bm = bm;
		this.gameRules = gameRules;
		this.gc = gc;
	}
	
	/**
	 * Get game status for AI
	 * @return integer with winning player, draw or unclear
	 */
	public int getGameStatus () {
		if (gameRules.isAWin(bm.PLAYER2))	{
 			return bm.PLAYER2_WIN;
 		} else if (gameRules.isAWin(bm.PLAYER1)) {
 			return bm.PLAYER1_WIN;
 		} else if (bm.boardIsFull()) {
 			return bm.DRAW;
 		} else {
 			return bm.UNCLEAR;
 		}
	}
	
	/**
	 * Generate the best move for TicTacToe
	 */
	public int[] bestMove () {
		positions.clear();
		Best best = bestMove (gc.getMySide(), gc.getOpponentSide(), 0);
		int[] coords = {best.row, best.column};
		
		return coords;
	}
    
	private Best bestMove (int mySide, int opponentSide, int amountCheckedBestMoves) {
		Best reply;           // Opponent's best reply
		int simpleEval;       // Result of an immediate evaluation
		int bestRow = 0;
		int bestColumn = 0;
		int winner;

		if ((simpleEval = getGameStatus()) != bm.UNCLEAR) {
			return new Best(simpleEval);
		}
		
		int[][] tmpBoard = bm.getBoard().clone();
		if (amountCheckedBestMoves >= 3 && amountCheckedBestMoves <= 5) {
			Integer pos = positions.get(tmpBoard);
			if (pos != null) {
				return new Best(pos);
			}
		}

		if (mySide == bm.PLAYER2) {
			winner = bm.PLAYER1_WIN;
		} else {
			winner = bm.PLAYER2_WIN;
		}

		for (int i = 0; i < (bm.getBoard().length * bm.getBoard()[0].length); i++) {
			int row = i / bm.getBoard().length;
			int column = i % bm.getBoard()[0].length;
			
			int[] move = {row, column};

			// First check if the row, column combination is empty
			// If there is already a side on the board.. there's no need to check if it is the best move
			if (bm.squareIsEmpty(move)) {
				// After that place the current side on the board
				bm.placeOnBoard(move, mySide);
				// Check if this is the best move or not 
				reply = bestMove (opponentSide, mySide, amountCheckedBestMoves + 1);
				// Remove the current side from the board again
				// So we can check whether this was the best move or not below
				bm.placeOnBoard(move, bm.EMPTY);

				// Is the current side a human? and was this the best move for the human?
				// Is the current side a computer? and was this the best move for the computer?
				if (mySide == bm.PLAYER1 && reply.val < winner || mySide == bm.PLAYER2 && reply.val > winner) {
					winner = reply.val;
					
					bestRow = row;
					bestColumn = column;
				}
			}
		}
		if (amountCheckedBestMoves <= 5) {
			positions.put(tmpBoard, winner);
		}
		return new Best(winner, bestRow, bestColumn);
	}
	
	private class Best {
       int row;
       int column;
       int val;

       public Best( int v )
         { this( v, 0, 0 ); }
      
       public Best( int v, int r, int c )
        { val = v; row = r; column = c; }
    } 
}
