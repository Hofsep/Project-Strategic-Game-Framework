package model;

import java.io.IOException;
import java.io.PrintWriter;

import controller.ClientController;

public class ClientSenderModel extends Model implements Runnable {
	private ClientModel cm;
	private ClientController cc;
	private PrintWriter out;
	private boolean printwriterSet;
	
	public ClientSenderModel (ClientModel cm, ClientController cc) {
		this.cm = cm;
		this.cc = cc;
		this.printwriterSet = false;
	}
	
	public void run () {
		
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {	}
			
			if (cm.getConnected()) {
				try {
					out = new PrintWriter(cm.getSocket().getOutputStream(), true);
		
					printwriterSet = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				break;
			}
		}

	}
	
	/**
	 * Send command to SERVER
	 * @param c, String command
	 */
	public void sendCommand (String c) {
		if (cm.getConnected()) {
			while (true) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignore) {	}
				
				if (printwriterSet) {
					if(!(c.contains("list")))
						System.out.println("send command: " + c);
					out.println(c);
					
					break;
				}
			}
		}
	}
	
	/*
	 * Getters
	 */
	public PrintWriter getPrintWriter () {
		return out;
	}
	
	public ClientModel getClientModel() {
		return cm;
	}
}