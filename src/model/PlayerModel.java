package model;

public class PlayerModel extends Model {
	private String name;
	
	private int win = 1;
	private int loose = 1;
	private int draw = 1;
	
	private boolean isHumanPlayer;
	
	public PlayerModel (String name) {
		this.name = name;
		this.isHumanPlayer = true;
	}
	
	/**
	 * Set AI playing
	 */
	public void setAI () {
		this.isHumanPlayer = false;
	}
	
	/**
	 * Set human playing
	 */
	public void setHuman () {
		this.isHumanPlayer = true;
	}
	
	/*
	 * Setters
	 */	
	public void addWin () {
		win++;
	}
	
	public void addLoose () {
		loose++;
	}
	
	public void addDraw () {
		draw++;
	}
	
	/*
	 * Getters
	 */
	public int getWins () {
		return win;
	}
	
	public int getLooses () {
		return loose;
	}
	
	public int getDraw () {
		return draw;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isHumanPlayer () {
		return isHumanPlayer;
	}
}