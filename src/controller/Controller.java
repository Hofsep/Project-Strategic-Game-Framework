package controller;

import java.awt.event.ActionListener;
import java.util.HashMap;

import model.PlayerModel;

public class Controller {
	public static final int GAME_TICTACTOE = 0;
	public static final int GAME_REVERSI = 1;
	
	protected static ClientController cc;
	protected static GameController gc;
	protected static Thread tgc;
	protected static Thread tcc;
	
	protected static PlayerModel pm;
	
	protected ActionListener al;
	
	public static void main(String[] args) {
		startClient();
    }
	
	/**
	 * Start the client controller
	 */
	public static void startClient () {
		cc = new ClientController();
    	tcc = new Thread(cc);
    	tcc.start();
	}
	
	/**
	 * Start a game (launch GameController)
	 * @param game, game name from server (TicTacToe or Reversi)
	 */
	public void startGame (String game, HashMap<String, Integer> players) {
		cc.toConsole("Match Started: " + game);
		
		
		
		int gameNumber = 0;
		
		if (game.equalsIgnoreCase("Tic tac toe") || game.equalsIgnoreCase("tic-tac-toe") || game.equalsIgnoreCase("tictactoe")) {
			gameNumber = 0;
		} else if (game.equalsIgnoreCase("Reversi") || game.equalsIgnoreCase("Ortello")) {
			gameNumber = 1;
		}
		
		gc = new GameController(gameNumber, players);
    	tgc = new Thread(gc);
    	tgc.start();
	}
	
	
	
	/**
     * Stop the running game
     */
    public void stopGame () {
    	gc.setGameRunning(false);
    }
}
