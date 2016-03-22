package view;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;
import model.BoardModel;
import model.Model;

public class BoardView extends JFrame implements View {
	private JComponent[] components;
	private BoardModel bm;
	private GameController gc;
    
	private JPanel content;

	private static final long serialVersionUID = 1L;

	public BoardView(BoardModel bm, GameController gc, int size) {
		this.bm = bm;
		this.gc = gc;
		this.components = new JComponent[size * size];
		this.content = new JPanel(new GridLayout(size,size));
		
		this.add(content);
		this.setSize(400, 400);
    	this.setTitle("Game - you are " + getMyColor());
    	this.setVisible(true);
		
		bm.addView(this);
    	showGrid();
	}
	
	/**
	 * Get playing color
	 * @return String with color
	 */
	public String getMyColor(){
		if(gc.getMySide() == 0){
			return "Blue";
		}else{
			return "Black";
		}
	}
	
	/**
	 * Create board grid
	 */
	public void showGrid() {    	
    	int[][] board = bm.getBoard();
    	
    	int i= 0;
    	for (int x = 0; x < board.length; x++) {
    		for (int y = 0; y < board[x].length; y++) {
    			switch (board[x][y]) {
    				case 0:		components[i] = new OvalComponent(Color.BLUE, bm.getBoardSize());		break;
    				case 1: 	components[i] = new OvalComponent(Color.BLACK, bm.getBoardSize());		break;
    				default: 	components[i] = new JButton("");										break;
    			}
    			i++;
    		}
    	}
    	
    	content.removeAll();
    	
    	for (JComponent c : components) {
    		c.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
    		
    		content.add(c);
    		
    		c.revalidate();
    	}
    	
    	if (gc.isYourTurn()) {
    		gc.control(bm, components);
    	}
	}	
	
	@Override
	public void update(Model model, Object arg) {
		showGrid();
	}
}


