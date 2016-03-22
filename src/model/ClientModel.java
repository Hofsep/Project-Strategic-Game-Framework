package model;

import java.net.Socket;
import java.util.LinkedList;

public class ClientModel extends Model {
	private int port = 7789;
	private String ip = null;
	private boolean isConnected = false;
	private Socket cmSocket;
	public LinkedList<String> responses = new LinkedList<String>();
    

    
    /**
     * Set connection IP
     * @param ip
     */
    public void setIP (String ip) {
    	this.ip = ip;
    }
    
    /**
     * Set connected
     * @param val, true on connected
     */
    public void setConnected (boolean val) {
    	isConnected = val;
    }
    
    /**
     * Set socket
     * @param val, socket to set for connection
     */
    public void setSocket (Socket val) {
    	cmSocket = val;
    }
 
    /* 
     * Getters
     */
    public int getPort () {
    	return port;
    }
    
    public String getIP () {
    	return ip;
    }
    
    public boolean getConnected () {
    	return isConnected;
    }
    
    public Socket getSocket () {
    	return cmSocket;
    }
}
