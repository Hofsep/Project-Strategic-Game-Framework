package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

class OvalComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	private Color c;
	private int boardSize;
	
	/**
	 * Draw an OvalComponent
	 * @param c, color in Color
	 * @param boardSize, boardSize in int
	 */
	public OvalComponent (Color c, int boardSize) {
		this.c = c;
		this.boardSize = boardSize;
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int item_size = (400 / boardSize) - 20;
        
        g.setColor(c);
        g.fillOval(10, 5, item_size, item_size);
    }
}