package so.eric.im;

import so.eric.im.*;
import so.eric.im.main.*;
import so.eric.im.messages.*;

import java.net.*;
import java.io.*;

public class ConnectedUser implements Runnable {
	
	private String username;
	private Socket socket;
	
	private ServerMessageGetterSender serverMessageGetterSender;
	
	private boolean listening;
	
	// This is the instance of the server to which this ConnectedUser object is associated
	// It is used for callbacks to the InstantMessageServer
	private InstantMessageServer server;
	
	
	public ConnectedUser(String username, ServerMessageGetterSender serverMessageGetterSender, InstantMessageServer server) {
		
		this.username = username;
		this.serverMessageGetterSender = serverMessageGetterSender;
		this.server = server;
		
		// Set the listening flag to true
		this.listening = true;
	}
	
	public void run() {
		
		Message message;
		
		// The ConnectedUser object will continously call readObject() on the ObjectInputStream
		while(listening) {
			// Get a new message
			message = serverMessageGetterSender.getMessage();
			
			// Callback to server to check the message and take appropriate action
			server.messageCheck(message, serverMessageGetterSender);
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public ServerMessageGetterSender getServerMessageGetterSender() {
		return serverMessageGetterSender;
	}
	
	public boolean closeConnection() {
		// This method handles logging the ConnectedUser off of the server
		
		// TODO write this method
		return true;
	}
}