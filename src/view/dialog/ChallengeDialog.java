package view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import controller.ClientController;

public class ChallengeDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ChallengeDialog(ClientController cc, String player, String game, int challengeID) {
		setBounds(100, 100, 349, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel uitgedaagdLabel = new JLabel("U bent uitgedaagd door:");
			uitgedaagdLabel.setBounds(10, 11, 132, 14);
			contentPanel.add(uitgedaagdLabel);
		}
		{
			JLabel challengePlayerName = new JLabel(player);
			challengePlayerName.setBounds(152, 11, 171, 14);
			contentPanel.add(challengePlayerName);
		}
		{
			JLabel spelLabel = new JLabel("Spel:");
			spelLabel.setBounds(10, 36, 132, 14);
			contentPanel.add(spelLabel);
		}
		{
			JLabel uitgedaagdeSpelLabel = new JLabel(game);
			uitgedaagdeSpelLabel.setBounds(152, 36, 171, 14);
			contentPanel.add(uitgedaagdeSpelLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = cc.getChallengeAcceptBtn();
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = cc.getChallengeDenyBtn();
				buttonPane.add(cancelButton);
			}
		}
	}

}
