package view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import controller.ClientController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ConnectDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField iPTextField;
	private JTextField portTextField;

	private JTextField usernameTextField;
	
	@SuppressWarnings("unused")
	private ClientController cc;

	/**
	 * Create the dialog.
	 */
	public ConnectDialog(ClientController cc) {
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
		setTitle("Connect");
		setBounds(100, 100, 426, 95);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(10, 14, 27, 14);
		contentPanel.add(lblIp);

		
		JLabel nameJLabel = new JLabel("Name");
		nameJLabel.setBounds(250, 14, 46, 14);
		contentPanel.add(nameJLabel);
		
		usernameTextField = cc.getUsernameTextField();
		usernameTextField.setBounds(289, 11, 120, 20);
		contentPanel.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		iPTextField = cc.getIPTextField();
		iPTextField.setBounds(31, 11, 210, 20);
		contentPanel.add(iPTextField);
		iPTextField.setColumns(10);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = cc.getConnectBtn();
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
		}
	}
	
}
