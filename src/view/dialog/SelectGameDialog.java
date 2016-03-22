package view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

import controller.ClientController;


public class SelectGameDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("unused")
	private ClientController cc;

	/**
	 * Create the dialog.
	 */
	public SelectGameDialog(ClientController cc) {
		this.cc = cc;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Selecteer Spel en AI");
		setBounds(100, 100, 307, 146);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblGame = new JLabel("Game");
		lblGame.setBounds(10, 11, 46, 14);
		contentPanel.add(lblGame);
		
		JLabel lblAgainst = new JLabel("Play as");
		lblAgainst.setBounds(158, 11, 46, 14);
		contentPanel.add(lblAgainst);
		
		//Group the radio buttons.
	    ButtonGroup game = new ButtonGroup();
	    ButtonGroup ai = new ButtonGroup();
	    
		JRadioButton rdbtnTictactoe = new JRadioButton("TicTacToe");
		rdbtnTictactoe.setBounds(6, 32, 109, 23);
		game.add(rdbtnTictactoe);
		contentPanel.add(rdbtnTictactoe);
		
		JRadioButton rdbtnReversi = new JRadioButton("Reversi");
		rdbtnReversi.setBounds(6, 58, 109, 23);
		game.add(rdbtnReversi);
		contentPanel.add(rdbtnReversi);
		
		JRadioButton rdbtnAi = new JRadioButton("Computer");
		rdbtnAi.setBounds(158, 32, 109, 23);
		ai.add(rdbtnAi);
		contentPanel.add(rdbtnAi);
		
		JRadioButton rdbtnPlayer = new JRadioButton("Player");
		rdbtnPlayer.setBounds(158, 58, 109, 23);
		ai.add(rdbtnPlayer);
		contentPanel.add(rdbtnPlayer);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Connect");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
		}
	}
}
