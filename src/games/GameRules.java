package games;

import java.util.ArrayList;

import model.BoardModel;

public abstract class GameRules {

	/**
	 * Check if move can be done
	 * @param x, coordinate
	 * @param y, coordinate
	 * @param bm, BoardModel
	 * @param side, playing side
	 * @return true on legal
	 */
	public abstract boolean isLegalMove(int x, int y, BoardModel bm, int side);

	/**
	 * Play a move
	 * @param move, coordinates
	 * @param side, playing side
	 */
	public abstract void playMove(int[] move, int side);
	
	/**
	 * Play a move with boardmodel for AI
	 * @param move, coordinates
	 * @param side, playing side
	 * @param bm, BoardModel
	 */
	public abstract void playMove(int[] move, int side, BoardModel bm);
	
	/**
	 * Check if is a Win for side
	 * @param side, side to check
	 * @return true on win
	 */
	public abstract boolean isAWin (int side);
	
	/**
	 * Gives all coordinates surrounding a position
	 * @param x
	 * @param y
	 * @return ArrayList with int[] coordinates
	 */
	public abstract ArrayList<int[]> surroundingCoordinates(int x, int y);

}
