package model;

import java.util.HashMap;

public class BoardModel extends Model {
	public final int PLAYER1        = 0; 
	public final int PLAYER2     	= 1; 
	public final int EMPTY        	= 2;

	public final int PLAYER1_WIN    = 0;
	public final int DRAW         	= 1;
	public final int UNCLEAR      	= 2;
	public final int PLAYER2_WIN 	= 3;

	private int[][] board;
	private int boardSize;
	
	private HashMap<String, Integer> players;
	
	public BoardModel (int size, HashMap<String, Integer> players) {
		this.boardSize = size;
		this.players = players;
		
		board =	new int[boardSize][boardSize];
		
		clearBoard();
	}
    
    /**
     * Place move on board from player
     * @param move, coordinates
     * @param player, String player name
     */
    public void placeOnBoardPlayer (int[] move, String player) {
		board[move[0]][move[1]] = players.get(player);
		
	}
    
    /**
     * Place move on board
     * @param move, coordinates
     * @param val, value to fill with
     */
    public void placeOnBoard (int[] move, int val) {
		board[move[0]][move[1]] = val;
	}

	/**
	 * Reset board
	 */
	public void clearBoard() {
		for (int x = 0; x < this.board.length; x++) {
			for (int y = 0; y < this.board[x].length; y++) {
				board[x][y] = EMPTY;
			}	
		}
	}

	/**
	 * Check if board is full
	 * @return true on full
	 */
	public boolean boardIsFull() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				int [] move = {x, y};
				if (squareIsEmpty(move)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Return if square is empty
	 * @param move
	 * @return True on empty
	 */
	public boolean squareIsEmpty(int[] move) {
		return board[move[0]][move[1]] == EMPTY;
	}
	
    /*
     * Getters
     */
	public int[][] getBoard () {
		return board;
	}
	
	public int getBoardSize () {
		return boardSize;
	}
	
	public int getBoardValue(int row, int col) {
		return board[row][col];
	}

	public void setBoard(int[][] temp) {
		this.board = temp;
	}
	
	public void print(){
		for(int x=0; x < 8; x++){
			System.out.println("[" + board[x][0] + "]-[" + board[x][1] + "]-[" + board[x][2] + "]-[" + board[x][3] + "]-[" + board[x][4] + "]-[" + board[x][5] + "]-[" + board[x][6] + "]-[" + board[x][7] + "]");
		}
		System.out.println("-----");
	}
}