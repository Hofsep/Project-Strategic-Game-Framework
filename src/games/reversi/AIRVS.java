package games.reversi;

import java.util.ArrayList;

import controller.GameController;
import model.BoardModel;
import games.AI;
import games.GameRules;

/*
 * The AI for reverse is point based of the position on the board.
 * Checks first on what moves are available and then looks at the points of 
 * available positions
 */
public class AIRVS extends AI {
	private BoardModel bm;
	private GameRules gameRules;
	private GameController gc;

	private final int[][] pointBoardFinal = {
			{ 1000, 0, 	50, 50, 50, 50, 0, 	1000 },
			{ 0, 	0, 	30, 30, 30, 30, 0, 	0 	 }, 
			{ 50, 	30, 20, 20, 20, 20, 30, 50 	 },
			{ 50, 	30, 20, 0, 	0, 	20, 30, 50   }, 
			{ 50, 	30, 20, 0, 	0, 	20, 30, 50   },
			{ 50, 	30, 20, 20, 20, 20, 30, 50   }, 
			{ 0, 	0, 	30, 30, 30, 30, 0, 	0    },
			{ 1000, 0, 	50, 50, 50, 50, 0, 	1000 }  };

	private int[][] pointBoard = pointBoardFinal.clone();

	public AIRVS(BoardModel bm, GameRules gameRules, GameController gc) {
		this.bm = bm;
		this.gameRules = gameRules;
		this.gc = gc;

		// Testdata
		possibleMoves(bm, gc.getMySide());
		System.out.println("");
	}

	public void resetPointBoard() {
		pointBoard = pointBoardFinal.clone();
	}

	/**
	 * Side = X of O
	 * 
	 * @return
	 */
	@Override
	public int[] bestMove() {
		int bestPoints = Integer.MIN_VALUE;
		int[] bestMove = null;
		ArrayList<int[]> pmoves = possibleMoves(bm, gc.getMySide());
		// check the value for each move you can make
		for (int[] currentMove : pmoves) {
			int tempPoints = getPoints(currentMove[0], currentMove[1]);
			System.out.println("x=" + currentMove[0] + " | y=" + currentMove[1]
					+ " | points=" + tempPoints);
			if (tempPoints > bestPoints) {
				bestMove = currentMove;
				bestPoints = tempPoints;
			}
			resetPointBoard();
		}

		return bestMove;
	}

	/**
	 * For every possible move, do getPoints() To validate the value.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getPoints(int x, int y) {
		int val = 0;

		// SET VALUE
		val = pointBoardFinal[x][y];

		BoardModel tempBoardCriteria = new BoardModel(8, gc.getPlayers());
		int[][] temp = new int[8][8];
		for (int xs = 0; xs < tempBoardCriteria.getBoard().length; xs++) {
			for (int ys = 0; ys < tempBoardCriteria.getBoard().length; ys++) {
				temp[xs][ys] = tempBoardCriteria.getBoard()[xs][ys];
			}
		}

		return val;
	}

	/**
	 * returns an arraylist with {x,y} coordinates which are possible to play
	 * 
	 * @param board
	 * @param side
	 * @return
	 */
	public ArrayList<int[]> possibleMoves(BoardModel board, int side) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		for (int row = 0; row < board.getBoardSize(); row++) {
			for (int col = 0; col < board.getBoardSize(); col++) {

				if (gameRules.isLegalMove(row, col, board, side)) {
					System.out.println("valid coordinates for side " + side
							+ ": row=" + row + ", col=" + col);
					moves.add(new int[] { row, col });
				}

			}
		}
		return moves;
	}
}
