package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;

import games.tictactoe.AITTT;
import games.tictactoe.GameRulesTTT;
import games.reversi.AIRVS;
import games.reversi.GameRulesRVS;
import view.BoardView;
import model.BoardModel;
import games.AI;
import games.GameRules;

public class GameController extends Controller implements Runnable {
    private BoardModel 		bm;
    private BoardView 		bv;
    
    private GameRules 		gameRules;
    private AI 				ai;
    
    private int 			activeGame;
    private int 			boardSize;
    
    private boolean 		gameRunning;
    private boolean 		yourTurn;
    private HashMap<String, Integer> players;
    
    
    public GameController (int activeGame, HashMap<String, Integer> players) {
    	this.activeGame = activeGame;
    	this.players = players;
    	this.yourTurn = false;
    	this.gameRunning = true;
    	
    	setBoardSize(activeGame);
    	
    	// Create Board
    	bm = new BoardModel(getBoardSize(), players);
    	bv = new BoardView(bm, this, getBoardSize());
    	
    	// Set rules and AI
    	setGameRules(activeGame, bm);
    	setAI(activeGame, bm, getGameRules());
    	
    	bv.showGrid();
    }
    
    @Override
    public void run () {
        System.out.println("Start Game: " + activeGame);
        
        if (pm.isHumanPlayer())	 	System.out.println(" Human playing");
        else                   		System.out.println(" AI playing");
        
        while (gameRunning) {
        	try {
    			Thread.sleep(10);
    		} catch (InterruptedException ignore) {	}
        	
        	// Play as AI
        	if (!pm.isHumanPlayer() && isYourTurn() && gameRunning) {
        		try {
        			Thread.sleep(1000);
        		} catch (InterruptedException ignore) {	}
        		
        		System.out.println("do AI move");
				int[] bestMove = getAI().bestMove();
				
				gameRules.playMove(bestMove, getMySide());
	       
				cc.sendPlayerMove(bestMove[0], bestMove[1]);
			
				bm.setChanged();
	            bm.notifyViews();
        	}
        }
        
        bm.setChanged();
        bm.notifyViews();
    }
	
    /**
     * Set the control buttons for the board
     * @param bm, BoardModel
     * @param components, The buttons
     */
    public void control (final BoardModel bm, final JComponent[] components) {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < components.length; i++) {
					if (components[i] == ae.getSource()) {
						if (pm.isHumanPlayer()) {
							int[] move = {i/boardSize, i%boardSize};
							if(gameRules.isLegalMove(move[0], move[1], bm, getMySide())){
								gameRules.playMove(move, getMySide());
								cc.sendPlayerMove(move[0], move[1]);
							}
						}
					}
				}
			}
		};
		
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == JButton.class) {
				((JButton) components[i]).addActionListener(al);
			}
		}
	}
    
    /**
     * Set BoardSize for specific game
     * @param game, game number
     */
    private void setBoardSize (int game) {
    	switch (game) {
    	case GAME_TICTACTOE:
    		this.boardSize = 3;
    		break;
    	case GAME_REVERSI:
    		this.boardSize = 8;
    		break;
    	}
    }
    
    /**
     * Set GameRules for specific game
     * @param game, game number
     * @param bm
     */
    private void setGameRules (int game, BoardModel bm) {
    	switch (game) {
    	case GAME_TICTACTOE:
    		this.gameRules = new GameRulesTTT(bm, this);
    		break;
    	case GAME_REVERSI: 
    		this.gameRules = new GameRulesRVS(bm, this);
    	}
    }
    
    /**
     * Set AI for specific game
     * @param game, game number
     * @param bm
     * @param gameRules
     */
    private void setAI (int game, BoardModel bm, GameRules gameRules) {
    	switch (game) {
    	case GAME_TICTACTOE:
    		this.ai = new AITTT(bm, gameRules, this);
    		break;
    	case GAME_REVERSI: 
    		this.ai = new AIRVS(bm, gameRules, this);
    	}
    }
    
    /**
     * Set your turn
     * @param val, your turn on true
     */
    public void setYourTurn (boolean val) {
    	yourTurn = val;
    	bv.showGrid();
    	
//    	setBoardStats();
//    	cc.getClientView().updateTurn(yourTurn);
    }
    
    /**
     * Set game running
     * @param val, true on running
     */
    public void setGameRunning (boolean val) {
    	gameRunning = val;
    }
   
    /**
     * Set board stats and fill
     */
    private void setBoardStats () {
    	int yourCounter = 0;
    	int theirCounter = 0;
    	
    	for (int x =  0; x < bm.getBoardSize(); x++) {
    		for (int y = 0; y < bm.getBoardSize(); y++) {
    			if (bm.getBoardValue(x, y) == getMySide()) {
    				yourCounter++;
    			} else if (bm.getBoardValue(x, y) == getOpponentSide()) {
    				theirCounter++;
    			}
    		}
    	}
    	
    	cc.getBoardStatsLabel().setText(yourCounter + " / " + theirCounter);
    }
    
    /**
     * @return int with my side
     */
    public int getMySide () {
    	return players.get(pm.getName());
    }
    
    /**
     * Get opponent side
     * @return int side
     */
    public int getOpponentSide () {
    	int side = 0;   	
    	for ( String player : players.keySet() ) {
    		if (! player.equals(pm.getName())) {
    			side = players.get(player);
    		}
    	}
    	return side;
    }
    
    /*
     * Getters
     */
    public boolean isYourTurn () {
    	return yourTurn;
    }
    
    public BoardModel getBoardModel () {
    	return bm;
    }
    
    private int getBoardSize () {
    	return boardSize;
    }
   
    public GameRules getGameRules () {
    	return this.gameRules;
    }
    
    private AI getAI () {
    	return this.ai;
    }
    
    public HashMap<String, Integer> getPlayers(){
    	return players;
    }
}