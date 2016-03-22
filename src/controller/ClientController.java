package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.ClientView;
import view.dialog.ChallengeDialog;
import view.dialog.MessageBox;
import model.ClientModel;
import model.ClientReceiverModel;
import model.ClientSenderModel;
import model.PlayerModel;

public class ClientController extends Controller implements Runnable {
    private ActionListener 	connectAl,		challengeAl, 	aiRadioAl, 			humanRadioAl, 	acceptChallengeAl, 	denyChallengeAl;
	private JButton 		connectBtn,		challengeBtn, 	acceptChallengeBtn, denyChallengeBtn;
	private JRadioButton	aiRadioBtn,		humanRadioBtn;
	private JTextField 	 	iPTextField, 	usernameTextField;
	
	private JList 			onlinePlayerList,	onlineGamesList;
	private JLabel 			connectedLabel,		boardStatsLabel;
	private JMenuItem 		menuItemSubscribe;
	
	private ClientModel 			cm;
	private ClientReceiverModel 	crm;
	private ClientSenderModel 		csm;
	private Thread 					tcrm, 	tcsm;
	
	private ClientView				cv;
	
	private HashMap<Integer, String> pendingChallenges = new HashMap<Integer, String>();
	
	private String selectedPlayerChallenge;
	private String selectedGameChallenge;
	
	private boolean viewingChallenge;

	
	@Override
    public void run () {
		setUIManager();

		connectedLabel 		= new JLabel("Niet verbonden");

    	iPTextField 		= new JTextField("localhost");
//    	portTextField 		= new JTextField("7789");
    	usernameTextField 	= new JTextField();
   
    	connectBtn 			= new JButton("Verbind");
    	challengeBtn 		= new JButton("Challenge");
    	acceptChallengeBtn 	= new JButton("Accept");
    	denyChallengeBtn 	= new JButton("Afwijzen");

    	aiRadioBtn 			= new JRadioButton("Computer");
    	humanRadioBtn 		= new JRadioButton("Mens");
  
    	onlinePlayerList 	= new JList();
    	onlineGamesList 	= new JList();
   
    	cm 		= new ClientModel();
    	crm 	= new ClientReceiverModel(cm, this);
		csm 	= new ClientSenderModel(cm, this);

		tcrm 	= new Thread(crm);
		tcsm 	= new Thread(csm);
		
		tcrm.start();
		tcsm.start();
		
		cv 		= new ClientView(crm, csm, pm, this);
		
		viewingChallenge = false;
		
		/*
		 * ActionListener onlinePlayerList
		 */
		onlinePlayerList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveSelection(e);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				saveSelection(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {		}
			@Override
			public void mouseEntered(MouseEvent e) {	}
			@Override
			public void mouseClicked(MouseEvent e) {	}
			
			/**
			 * Save selected element to prevent losing selection on refresh
			 * @param e
			 */
			public void saveSelection(MouseEvent e) {
				onlinePlayerList.setSelectedIndex(onlinePlayerList.locationToIndex(e.getPoint()));
				String selectedPlayer = (String) onlinePlayerList.getSelectedValue();
				if(selectedPlayer != null) selectedPlayerChallenge = selectedPlayer;
			}
		});
		
		/*
		 * ActionListener onlineGameList
		 */
		onlineGamesList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				event(e);
				saveSelection(e);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				event(e);
				saveSelection(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {		}
			@Override
			public void mouseEntered(MouseEvent e) {	}
			@Override
			public void mouseClicked(MouseEvent e) {	}
			
			/**
			 * Run event
			 * @param e
			 */
			public void event(MouseEvent e) {
				onlineGamesList.setSelectedIndex(onlineGamesList.locationToIndex(e.getPoint()));
				final String selectedGame = (String) onlineGamesList.getSelectedValue();
			}
			
			/**
			 * Save selected element to prevent losing selection on refresh
			 * @param e
			 */
			public void saveSelection(MouseEvent e) {
				onlineGamesList.setSelectedIndex(onlineGamesList.locationToIndex(e.getPoint()));
				String selectedGame = (String) onlineGamesList.getSelectedValue();
				if(selectedGame != null) selectedGameChallenge = selectedGame;
			}
		});
		
		/*
		 * ActionListener connecting
		 */
    	connectAl = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(iPTextField.getText() != null /*&& !portTextField.getText().equals("")*/){
					cm.setIP(iPTextField.getText());
					//cm.setPort(Integer.parseInt(portTextField.getText()));
					if(connect()) {
						connectedLabel.setText("Verbonden");
						connectedLabel.setForeground(Color.BLUE);
						
						Component component = (Component) ae.getSource();
					    Window win = SwingUtilities.getWindowAncestor(component);
					    win.dispose();
					} else {
						MessageBox mb = new MessageBox("Verbinding met: "+iPTextField.getText() + " is niet geaccepteerd.");
						mb.setVisible(true);
					}
				}
				else {
					MessageBox mb = new MessageBox("U heeft het IP niet goed ingevuld.");
					mb.setVisible(true);
				}
				if(usernameTextField.getText().equals("")){
					MessageBox mb = new MessageBox("Voer een gebruikersnaam in.");
					mb.setVisible(true);
				} else {
					if(login(usernameTextField.getText())) {
						connectedLabel.setText("Verbonden als: "+usernameTextField.getText());
						Component component = (Component) ae.getSource();
					    Window win = SwingUtilities.getWindowAncestor(component);
					    win.dispose();
					    
					    cc.enableSettings();
					} else {
						MessageBox mb = new MessageBox("Kan niet inloggen op de server");
						mb.setVisible(true);
					}
				}
				getPlayerList();
				getGameList();
			}
		};
	
		/*
		 * ActionListener challenging
		 */
		challengeAl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cm.getConnected()) {
					MessageBox mb = new MessageBox("U bent niet verbonden met een server.");
					mb.setVisible(true);
				} else if (pm == null) {
					MessageBox mb = new MessageBox("U bent niet ingelogd.");
					mb.setVisible(true);
				} else {
					if(selectedGameChallenge == null) {
						MessageBox mb = new MessageBox("U heeft geen spel geselecteerd.");
						mb.setVisible(true);
						return;
					}
					
					if(selectedPlayerChallenge == null) {	
						MessageBox mb = new MessageBox("U heeft geen speler geselecteerd.");
						mb.setVisible(true);
						return;
					}
					
					challenge(selectedPlayerChallenge, selectedGameChallenge);
				}
			}
		};
		
		/*
		 * ActionListener accept challenge
		 */
		acceptChallengeAl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Entry<Integer, String> challenge = pendingChallenges.entrySet().iterator().next();
				
				pendingChallenges.remove(challenge.getKey());
				
				if (challengeAccept(challenge.getKey())) {
					toConsole("Accepted challenge " + challenge.getValue());
				}
				
				Component component = (Component) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(component);
				win.dispose();
				
				viewingChallenge = false;
				pendingChallenges.clear();
			}
		};
		
		/*
		 * ActionListener cancel challenge
		 */
		denyChallengeAl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Entry<Integer, String> challenge = pendingChallenges.entrySet().iterator().next();
				
				pendingChallenges.remove(challenge.getKey());
				
				toConsole("Denied challenge " + challenge.getValue());
				
				Component component = (Component) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(component);
				win.dispose();
				
				viewingChallenge = false;
				viewChallengeDialog();
			}
		};
		
		/*
		 * ActionListener aiRadio
		 */
		aiRadioAl = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!crm.getClientModel().getConnected()) {
					MessageBox mb = new MessageBox("U bent niet verbonden met een server.");
					mb.setVisible(true);
				} else if(pm == null) {
					MessageBox mb = new MessageBox("U bent niet ingelogd.");
					mb.setVisible(true);
				} else {
					pm.setAI();
				}				
			}
		};
		
		/*
		 * ActionListener aiRadio
		 */
		humanRadioAl = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!crm.getClientModel().getConnected()) {
					MessageBox mb = new MessageBox("U bent niet verbonden met een server.");
					mb.setVisible(true);
				}else if(pm == null) {
					MessageBox mb = new MessageBox("U bent niet ingelogd.");
					mb.setVisible(true);
				} else {
					pm.setHuman();
				}			
			}
		};
		
		
		connectBtn.addActionListener(connectAl);
		challengeBtn.addActionListener(challengeAl);
		acceptChallengeBtn.addActionListener(acceptChallengeAl);
		denyChallengeBtn.addActionListener(denyChallengeAl);
		aiRadioBtn.addActionListener(aiRadioAl);
		humanRadioBtn.addActionListener(humanRadioAl);
    }
	
	/**
	 * Connect to server, create socket
	 * @return true on success
	 */
    private boolean connect () {
		if (cm.getIP() != null) {
			try {
				InetAddress address = InetAddress.getByName(cm.getIP());
				SocketAddress socket_address = new InetSocketAddress(address, cm.getPort());
				
				Socket s = new Socket();
				s.connect(socket_address, 5000);
				
				cm.setSocket(s);
				cm.setConnected(true);
				
				toConsole("Connected to \'" + iPTextField.getText() +"\'");
		
				return true;
			} catch (UnknownHostException e) {
				toConsole("Can't connect to \'" + iPTextField.getText() + "\'");
				return false;
			} catch (IOException e) {
				toConsole("Can't connect to \'" + iPTextField.getText() +  "\'");
				return false;
			}
		}
		
		return false;
	}
     
    /**
     * Login on the Server
     * @param name, player name
     * @return true on success
     */
	private boolean login(String name) {
		csm.sendCommand("login " + name);
		
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
			pm = new PlayerModel(name);
			toConsole(name + " is now signed in!");
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		return result;
	}
	
	/**
	 * Send a Player Move to the server
	 * @param x, coordinate
	 * @param y, coordinate
	 * @return true on success
	 */
	public boolean sendPlayerMove (int x, int y) {
		gc.setYourTurn(false);
		System.out.println("\t \t sendPlayerMove sending : move " + x + "," + y);

		int pos = 0;
		pos = (x*8)+y;

		csm.sendCommand("move " + pos);
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
			
			toConsole("You did move x: " + x + " y: " + y + " and the server responded OK");
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		System.out.println(result);
		return result;
	}
	
	/**
	 * Challenge a user
	 * @param user, String user
	 * @param game, String game
	 * @return true on success
	 */
	private boolean challenge (String user, String game) {
		csm.sendCommand("challenge \"" + user + "\" \"" + game + "\"");
		
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
			toConsole("You challenged a user");
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		return result;
	}
	
	/**
	 * Challenge a user
	 * @param user, String user
	 * @param game, String game
	 * @return true on success
	 */
	private boolean challengeAccept (int challengeID) {
		csm.sendCommand("challenge accept " + challengeID);
		
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
			toConsole("You accepted the challenge.");
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		return result;
	}
	
	/**
	 * Disconnect from the server
	 */
	@SuppressWarnings("deprecation")
	public void disconnect () {
		csm.sendCommand("logout");
		cm.setConnected(false);
		
		pm = null;
		
		cv.dispose();
		cv = null;
		
		tcrm.stop();
		tcsm.stop();
		
		tcrm = null;
		tcsm = null;
		
		crm = null;
		csm = null;
		
		cm = null;

		toConsole("Signed out.");
		
		startClient();
	}
	
	/**
	 * Quit the program
	 * @return true on success
	 */
	@SuppressWarnings("deprecation")
	public boolean quit () {
		csm.sendCommand("logout");
		
		toConsole("Signed out.");
		
		tcsm.stop();
		tcrm.stop();
		
		csm.getPrintWriter().close();
		try {
			crm.getBufferedReader().close();
			cm.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(1);
		
		return true;
	}
	
	/**
	 * Get playerlist
	 * @return true on success
	 */
	public boolean getPlayerList () {	
		csm.sendCommand("get playerlist");
		
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
			toConsole("Playerlist loaded");
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		return result;
	}
	
	/**
	 * Get gamelist
	 * @return true on success
	 */
	public boolean getGameList () {	
		csm.sendCommand("get gamelist");
		
		// Wait till server response
		waitTillResponse();
		
		// Show result in console
		boolean result = isResponseOK();
		
		if (result) {
		} else {
			toConsole("    " + cm.responses.get(0));
			cm.responses.remove(0);
		}
		
		// Return result
		return result;
	}
	
	/**
	 * Wait till response from the server
	 */
	private void waitTillResponse () {
		while (cm.responses.size() < 1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {	}
		}
	}
	
	/**
	 * Check if response is OK
	 * @return true on OK
	 */
	private boolean isResponseOK () {
		for (int i = 0; i < cm.responses.size(); i++) {
			if (cm.responses.get(0).equals("OK")) {
				cm.responses.remove(0);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Parse a server response, when SVR GAME response do specific action
	 * @param response
	 * @return true on SVR GAME response found
	 */
	public boolean parseResponse (String response) {
		String[] responseWords = response.split(" ");
		
		if (responseWords[0].equals("SVR") && responseWords[1].equals("GAME")) {
			String[] parameters = splitSVRGameResponse (response);
			
			if (responseWords[2].equals("CHALLENGE")) {
				if (responseWords[3].equals("CANCELLED")) {
					String message = pendingChallenges.get(Integer.parseInt(parameters[0]));
					toConsole("Challenge " + message + " is cancelled");
					
					pendingChallenges.remove(parameters[0]);
				} else {
					String message = "by: " + parameters[0] + " for: " + parameters[2];
					toConsole("You are challenged by: " + parameters[0] + " for: " + parameters[2]);
					
					pendingChallenges.put(Integer.parseInt(parameters[1]), message);
					
					viewChallengeDialog();
				}
				
			} else if (responseWords[2].equals("MATCH")) {
				HashMap<String, Integer> players = new HashMap<String, Integer>();
				players.put(parameters[0], 0);
				if (parameters[0].equals(pm.getName())) {
					players.put(parameters[2], 1);
				} else {
					players.put(pm.getName(), 1);
				}

				this.startGame(parameters[1], players);
				
				return true;
			} else if (responseWords[2].equals("YOURTURN")) {
				gc.setYourTurn(true);
				
				toConsole("Your Turn: " + parameters[0]);
				
				return true;
			} else if (responseWords[2].equals("MOVE")) {
				if (parameters[2].equals("Illegal move")) {
					toConsole("Move failed by: " + parameters[0] + " Reaction: " + parameters[2]);
					gc.setYourTurn(true);
				} else {
					if ( ! parameters[0].equals(pm.getName())) {
						toConsole("Move done by opponent: " + parameters[0] + " Details: " + parameters[2]);
						
						int[] xy = new int[2];
						xy[0] = Integer.parseInt(parameters[1])/8;
						xy[1] = Integer.parseInt(parameters[1])%8;
						
						gc.getGameRules().playMove(xy, gc.getOpponentSide());
					} else {
						toConsole("You did a move, Details: " + parameters[2]);
					}
				}
			} else if (responseWords[2].equals("WIN")) {
				pm.addWin();
				toConsole("Match Won: " + parameters[0]);
				gc.stopGame();
				MessageBox mb = new MessageBox("U heeft gewonnen!");
				mb.setVisible(true);
				
				return true;
			} else if (responseWords[2].equals("LOSS")) {
				pm.addLoose();
				toConsole("Match Lost: " + parameters[0]);
				gc.stopGame();
				MessageBox mb = new MessageBox("U heeft verloren!");
				mb.setVisible(true);
				
				return true;
			} else if (responseWords[2].equals("DRAW")) {
				pm.addDraw();
				toConsole("Match Draw: " + response);
				gc.stopGame();
				MessageBox mb = new MessageBox("U heeft gelijk gespeeld!");
				mb.setVisible(true);
				
				return true;
			}
		} else if (responseWords[0].equals("SVR")) {
			if (responseWords[1].equals("PLAYERLIST")) {
				toConsole("Player List: " + response);
				onlinePlayerList.setListData(splitListResponse("PLAYERLIST", response));
				
				return true;
			} else if (responseWords[1].equals("GAMELIST")) {
				toConsole("Game List: " + response);
				onlineGamesList.setListData(splitListResponse("GAMELIST", response));
				
				return true;
			}	
		}
		return false;
	}
	/**
	 * Split a Gamelist and Playerlist
	 * @param type, GAMELIST or PLAYERLIST
	 * @param list, String raw list
	 * @return String[] with list elements
	 */
	private String[] splitListResponse (String type, String list) {
		String list_stripped = list.replace("SVR " + type + " [", "").replace("]", "").replace("\"", "");
		String[] items = list_stripped.split(", ");
		
		if (type == "PLAYERLIST") {
			String[] used_items = new String[items.length - 1];
			
			if (items.length > 0) {
				int i = 0;
				for (String item : items) {
					if (! item.equals(pm.getName())) {
						used_items[i] = item;
						i++;
					}
				}
			} else {
				used_items[0] = "";
			}
			
			return used_items;
		}
		
		return items;
	}
	
	/**
	 * Split parameters from SVR GAME response
	 * @param response, RAW server response
	 * @return String[] with parameters
	 */
	public String[] splitSVRGameResponse (String response) {
		String[] splitting;
		String parameters;
		
		splitting = response.split("\\{");
		
		parameters = splitting[1];
		parameters = parameters.replace("}", "");
		
		if (parameters.contains("\", ")) {
			String[] parameter = parameters.split("\", ");
			String[] result = new String[parameter.length];
			
			for (int i = 0; i < parameter.length; i++) {
				String[] result_raw_to_split = parameter[i].split(" \"");
				String result_raw =  result_raw_to_split[1].replace("\"", "");
				
				result[i] = result_raw;
			}
			
			return result;
		} else {
			String[] result = new String[1];
			
			String[] result_raw_to_split = parameters.split(" \"");
			String result_raw =  result_raw_to_split[1].replace("\"", "");
			
			result[0] = result_raw;
			
			return result;
		}
	}
	
	/**
	 * View a challenge dialog
	 * @param user, user
	 * @param game
	 * @param challengeID
	 */
	public void viewChallengeDialog () {
		Entry<Integer, String> challenge = pendingChallenges.entrySet().iterator().next();
		
		if (pendingChallenges.size() > 0) {
			String[] challengeParameters = challenge.getValue().split(" for: ");
			String challengedBy = challengeParameters[0].replace("by: ", "");
			String challengedGame = challengeParameters[1];
			
			if (! viewingChallenge) {
				viewingChallenge = true;
				new ChallengeDialog(this, challengedBy, challengedGame, challenge.getKey()).setVisible(true);
			}
		}
	}
	
	/**
	 * Enable AI/Human settings
	 */
	public void enableSettings() {
		cc.getAiRadioBtn().setEnabled(true);
		cc.getHumanRadioBtn().setEnabled(true);
	}
	
	/**
	 * Write to console
	 * @param val, string to write to console
	 */
	public void toConsole (String val) {
		//console.append(val + "\n");
		System.out.println(val);
	}
	
	/**
	 * Set the GUI Look and Feel
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
	
	public PlayerModel getPlayerModel () {
		return pm;
	}
	
	public ClientView getClientView () {
		return cv;
	}
	
	/*
	 * JButton getters
	 */
	public JButton getConnectBtn () {
		return connectBtn;
	}
	public JButton getChallengeBtn() {
		return challengeBtn;
	}
	public JButton getChallengeAcceptBtn() {
		return acceptChallengeBtn;
	}
	public JButton getChallengeDenyBtn() {
		return denyChallengeBtn;
	}
	
	/*
	 * JTextField getters
	 */
	public JTextField getIPTextField () {
		return iPTextField;
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}
	
	/*
	 * JRadioButton getters
	 */
	public JRadioButton getHumanRadioBtn() {
		return humanRadioBtn;
	}
	public JRadioButton getAiRadioBtn() {
		return aiRadioBtn;
	}
	
	/*
	 * JList getters
	 */
	public JList getCurrentGamesList() {
		return onlineGamesList;
	}
	public JList getCurrentPlayerList() {
		return onlinePlayerList;
	}
	
	/*
	 * JLabel getters
	 */
	public JLabel getConnectedLabel() {
		return connectedLabel;
	}
	
	public JLabel getBoardStatsLabel () {
		return boardStatsLabel;
	}
	
	/*
	 * JMenuItem getters
	 */
	public JMenuItem getMenuItemSubcribe() {
		return menuItemSubscribe;
	}


}