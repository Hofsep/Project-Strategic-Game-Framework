package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.ClientController;

public class ClientReceiverModel extends Model implements Runnable {
	private BufferedReader in;
	private String line;
	private ClientModel cm;
	private ClientController cc;
	
	public ClientReceiverModel (ClientModel cm, ClientController cc) {
		this.cm = cm;
		this.cc = cc;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {	}
			
			if (cm.getConnected()) {
				try {
					in = new BufferedReader(new InputStreamReader(cm.getSocket().getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		getCommands();
	}
	
	/**
	 * Get incoming commands
	 */
	private void getCommands () {
		try {
			Thread.sleep(30);
		} catch (InterruptedException ignore) {	}
		
		if (cm.getConnected()) {
			try {
				// Remove first two responses
				System.out.println(in.readLine());
				System.out.println(in.readLine());
				
				while ((line = in.readLine()) != null){
					if(!(line.equalsIgnoreCase("ok") || line.contains("LIST"))){
						System.out.println("  new response: " + line);
					}
						if ( ! cc.parseResponse(line)) {
						cm.responses.add(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Getters
	 */
	public BufferedReader getBufferedReader () {
		return in;
	}
	
	public ClientModel getClientModel() {
		return cm;
	}
}