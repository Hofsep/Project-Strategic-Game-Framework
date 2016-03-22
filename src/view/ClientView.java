package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import controller.ClientController;

import javax.swing.JList;

import model.ClientReceiverModel;
import model.ClientSenderModel;
import model.Model;
import model.PlayerModel;
import view.ClientView;
import view.dialog.ConnectDialog;
import view.dialog.MessageBox;

import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.JRadioButton;

import java.awt.Font;


public class ClientView extends JFrame implements View {
	private static final long serialVersionUID = 1L;
	
	private ClientReceiverModel		crm;
	@SuppressWarnings("unused")
	private ClientSenderModel		csm;
	
	private ClientController		cc;
	@SuppressWarnings("unused")
	private PlayerModel				pm;
	
	private JPanel contentPane;
	private JLabel playerTurn;

	/**
	 * Creates the Client frame
	 */
	public ClientView(final ClientReceiverModel crm, ClientSenderModel csm, PlayerModel pm, final ClientController cc) {
    	this.crm = crm;
    	this.csm = csm;
    	this.cc = cc;
    	this.pm = pm;
		
		crm.addView(this);
    	csm.addView(this);
		
    	// Set frame settings
		setTitle("Project groep 1");
		setResizable(false);
		
		// Set the look and feel
		setUIManager();
		
		// Center the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 500);
		
		// Load view elements
		createMenuBar();
		createLeftSide();
		createMiddleScreen();
	//	createSeperators();
		createIndicators();
		
		this.setVisible(true);
	}
	
	/**
	 * Create the menu bar
	 */
	private void createMenuBar () {
		/*
		 * Menu bar
		 */
		JMenuBar fileMenu = new JMenuBar();
		setJMenuBar(fileMenu);
		
		JMenu fileMenuConnect = new JMenu("Menu");
		fileMenu.add(fileMenuConnect);
		
		/*
		 * Connect
		 */
		JMenuItem mntmVerbind = new JMenuItem("Verbind");
		mntmVerbind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectDialog connect = new ConnectDialog(cc);
				connect.setVisible(true);
			}
		});
		fileMenuConnect.add(mntmVerbind);
		
		
		/*
		 * Disconnect
		 */
		JMenuItem menuItemDisconnect = new JMenuItem("Verbinding afsluiten");
		menuItemDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!crm.getClientModel().getConnected()) {
					MessageBox mb =new MessageBox("U bent niet verbonden met een server.");
					mb.setVisible(true);
				} else {
					cc.disconnect();
				}
			}
		});
		
		/*
		 * Separator
		 */
		JSeparator separator = new JSeparator();
		fileMenuConnect.add(separator);
		
		fileMenuConnect.add(menuItemDisconnect);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	/**
	 * Create the left side
	 */
	private void createLeftSide () {
		/*
		 * Online playerList
		 */
		JLabel lblOnlinePlayers = new JLabel("Online spelers");
		lblOnlinePlayers.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOnlinePlayers.setBounds(10, 11, 160, 14);
		contentPane.add(lblOnlinePlayers);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 29, 160, 170);
		contentPane.add(scrollPane);
		
		JList onlinePlayerList = cc.getCurrentPlayerList();
		scrollPane.setViewportView(onlinePlayerList);
		
		/*
		 * Current gamesList
		 */
		JLabel lblCurrentGames = new JLabel("Beschikbare spellen");
		lblCurrentGames.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCurrentGames.setBounds(10, 210, 160, 14);
		contentPane.add(lblCurrentGames);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 235, 160, 170);
		contentPane.add(scrollPane_1);
		
		JList currentGamesList = cc.getCurrentGamesList();
		scrollPane_1.setViewportView(currentGamesList);
		
		/*
		 * Refresh button
		 */
		JButton refreshBtn = new JButton("Vernieuw");
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!crm.getClientModel().getConnected()) {
					MessageBox mb =new MessageBox("U bent niet verbonden met een server.");
					mb.setVisible(true);
				}else {
					cc.getPlayerList();
				}
			}
		});
		refreshBtn.setBounds(10, 418, 77, 22);
		contentPane.add(refreshBtn);
		
		/*
		 * Challenge button
		 */
		JButton challengeBtn = cc.getChallengeBtn();
		challengeBtn.setBounds(90, 418, 80, 22);
		contentPane.add(challengeBtn);
	}
	
	/**
	 * Create middle screen
	 */
	private void createMiddleScreen () {
		/*
		 * AI/Human
		 */
		JLabel lblSpeelAls = new JLabel("Speel als:");
		lblSpeelAls.setBounds(188, 35, 46, 14);
		contentPane.add(lblSpeelAls);
		
	    ButtonGroup playAsGroup = new ButtonGroup();
		JRadioButton humanRadioBtn = cc.getHumanRadioBtn();
		humanRadioBtn.setSelected(true);
		humanRadioBtn.setEnabled(false);
		humanRadioBtn.setBounds(240, 31, 64, 23);
		contentPane.add(humanRadioBtn);
		playAsGroup.add(humanRadioBtn);
		
		JRadioButton aiRadioBtn = cc.getAiRadioBtn();
		aiRadioBtn.setEnabled(false);
		aiRadioBtn.setBounds(240, 56, 89, 23);
		contentPane.add(aiRadioBtn);
		playAsGroup.add(aiRadioBtn);
	
		JLabel lblAanZet_1 = new JLabel("Aan zet:");
		lblAanZet_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAanZet_1.setBounds(188, 162, 116, 14);
		contentPane.add(lblAanZet_1);
	
	}
	
	/**
	 * Create seperators
	 */
//	private void createSeperators() {
//		JSeparator separator_1 = new JSeparator();
//		separator_1.setOrientation(SwingConstants.VERTICAL);
//		separator_1.setBounds(180, 30, 2, 57);
//		contentPane.add(separator_1);
//		
//		JSeparator separator_2 = new JSeparator();
//		separator_2.setBounds(180, 29, 204, 2);
//		contentPane.add(separator_2);
//		
//		JSeparator separator_3 = new JSeparator();
//		separator_3.setOrientation(SwingConstants.VERTICAL);
//		separator_3.setBounds(382, 29, 2, 58);
//		contentPane.add(separator_3);
//		
//		JSeparator separator_4 = new JSeparator();
//		separator_4.setBounds(180, 404, 204, 2);
//		contentPane.add(separator_4);
//		
//		JSeparator separator_8 = new JSeparator();
//		separator_8.setOrientation(SwingConstants.VERTICAL);
//		separator_8.setBounds(382, 124, 2, 152);
//		contentPane.add(separator_8);
//		
//		JSeparator separator_9 = new JSeparator();
//		separator_9.setBounds(180, 124, 204, 2);
//		contentPane.add(separator_9);
//		
//		JSeparator separator_10 = new JSeparator();
//		separator_10.setBounds(180, 278, 204, 2);
//		contentPane.add(separator_10);
//		
//		JSeparator separator_11 = new JSeparator();
//		separator_11.setOrientation(SwingConstants.VERTICAL);
//		separator_11.setBounds(180, 125, 2, 155);
//		contentPane.add(separator_11);
//
//		
//		JSeparator separator = new JSeparator();
//		separator.setBounds(180, 86, 204, 2);
//		contentPane.add(separator);
//		
//		JSeparator separator_5 = new JSeparator();
//		separator_5.setBounds(180, 316, 204, 2);
//		contentPane.add(separator_5);
//		
//		JSeparator separator_6 = new JSeparator();
//		separator_6.setOrientation(SwingConstants.VERTICAL);
//		separator_6.setBounds(180, 316, 2, 89);
//		contentPane.add(separator_6);
//		
//		JSeparator separator_7 = new JSeparator();
//		separator_7.setOrientation(SwingConstants.VERTICAL);
//		separator_7.setBounds(382, 315, 2, 89);
//		contentPane.add(separator_7);
//	}
	
	/**
	 * Create the indicators
	 */
	private void createIndicators () {
		/*
		 * Connect label
		 */
		JLabel connectedLabel = cc.getConnectedLabel();
		connectedLabel.setForeground(Color.RED);
		connectedLabel.setHorizontalAlignment(SwingConstants.LEFT);
		connectedLabel.setBounds(180, 410, 204, 14);
		contentPane.add(connectedLabel);

	}
	
//	public void updateTurn(boolean isYourTurn) {
//		if(isYourTurn) {
//			playerTurn.setText("Jij bent aan zet.");
//			playerTurn.setForeground(new Color(34, 139, 34));
//		} else {
//			playerTurn.setText("Niet aan zet.");
//			playerTurn.setForeground(new Color(128, 0, 0));
//		}
//	}

	/**
	 * Set the GUI
	 */
	private void setUIManager() {
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
	}

	@Override
	public void update(Model model, Object arg) {
		
	}
}
