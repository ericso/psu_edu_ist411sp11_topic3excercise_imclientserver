/*
	When running this program, so.eric.im.main.InstantMessageFrame, you need to supply a String
	for the client's username

*/

package so.eric.im.main;

import so.eric.im.*;
import so.eric.im.ui.*;
import so.eric.im.messages.*;

import java.net.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

public class InstantMessageClient implements Runnable {
	
	// This is this client's username
	private String username;
	private java.util.List<String> friendsList;
	
	// HashMap to hold the conversations that are currently being held
	private HashMap<String, InstantMessageDialog> currentConversations;
	
	private JFrame instantMessageFrame;
	
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	
	private ClientMessageGetterSender clientMessageGetterSender;
	
	private boolean listening;
	
	public InstantMessageClient(String username) {
		this.username = username;
		
		this.serverAddress = "localhost";
		this.serverPort = 5555;
		
		// Connect to the server
		System.out.println("Connecting to " + serverAddress + " on port " + serverPort);
		
		// Create a socket and attempt to connect to the server
		try {
			socket = new Socket(serverAddress, serverPort);
		} catch (IOException ioe) {
			System.out.println("Error: Could not connect to socket on port: " + serverPort);
		}
		
		System.out.println("Connected to " + socket.getRemoteSocketAddress() + " on port " + socket.getPort());
		
		// Create a ClientMessageGetterSender object to handle getting and sending messages
		this.clientMessageGetterSender = new ClientMessageGetterSender(socket);
		
		System.out.println("InstantMessageClient: Checkpoint 1"); // TEST
		
		// Create a logon CommandMessage and send it
		CommandMessage logonCommandMessage = new CommandMessage(this.username, "logon");
		clientMessageGetterSender.sendMessage(logonCommandMessage);
		
		System.out.println("InstantMessageClient: Checkpoint 2"); // TEST
		
		this.friendsList = null;
		FriendsListMessage friendsListMessage = null;
		
		// Retrieve list of logged on users
		while (friendsList == null) {
			friendsListMessage = (FriendsListMessage) clientMessageGetterSender.getMessage();
			messageCheck(friendsListMessage);
		}
		
		System.out.println("InstantMessageClient: Checkpoint 3"); // TEST
		
		// Create the friends list window
		this.instantMessageFrame = new InstantMessageFrame(this.username, this.friendsList, this.clientMessageGetterSender);
		this.instantMessageFrame.setSize(220, 450);
		this.instantMessageFrame.setVisible(true);
		
		System.out.println("InstantMessageClient: Checkpoint 4"); // TEST
		
		this.listening = true;
		this.currentConversations = new HashMap<String, InstantMessageDialog>();
	}
	
	public void run() {
		Message messageToGet;
		
		while(listening) {
			messageToGet = clientMessageGetterSender.getMessage();
			messageCheck(messageToGet);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				System.out.println("Sleep interrupted.");
			}
		}
	}
	
	public void messageCheck(Message messageToCheck) {
		
		System.out.println("InstantMessageClient::messageCheck Checkpoint 1"); // TEST
		
		// Get the message command and sender
		String command = messageToCheck.getCommand();
		String messageSender = messageToCheck.getSender();
		
		System.out.println("InstantMessageClient::messageCheck Checkpoint 2"); // TEST
		
		// The message can only be of two different types: friendslist and message
		if (command.equals("friendslist")) {
			
			System.out.println("InstantMessageClient::messageCheck (friendslist) Checkpoint 3"); // TEST
			
			// Cast the Message to a FriendsListMessage
			FriendsListMessage friendsListMessage = (FriendsListMessage) messageToCheck;
			
			this.friendsList = new ArrayList<String>(friendsListMessage.getFriendsList());
			
		} else if (command.equals("message")) {
			
			System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 4"); // TEST
			
			// Cast Message to a TextMessage
			TextMessage textMessage = (TextMessage) messageToCheck;
			
			// Check to see if a conversation with the recipient is already in progress
			boolean hasConversationWithSender = currentConversations.containsKey(messageSender);
			
			System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 5"); // TEST
			
			InstantMessageDialog tempDialog = null;
			String textToAppend = textMessage.getMessageText();
			
			System.out.println("InstantMessageClient::messageCheck (message) Checkpoint textToAppend: " + textToAppend); // TEST
			
			if (hasConversationWithSender) {
				// Find the InstantMessageDialog and send message to it
				tempDialog = (InstantMessageDialog) currentConversations.get(messageSender);
				
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 6"); // TEST
				
				// Update the JTextArea of the InstantMessageDialog with the text
				tempDialog.updateTextArea(textToAppend);
			} else {
			
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 7"); // TEST
				
				// Instantiate a new InstantMessageDialog with the sender of the message
				tempDialog =  new InstantMessageDialog(null, this.username, messageSender, this.clientMessageGetterSender);
				
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 8"); // TEST
				
				// Code blocks here; setVisible() somehow screws things up
				tempDialog.setVisible(true);
			
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 9"); // TEST
				
				// Update the JTextArea of the InstantMessageDialog with the text
				tempDialog.updateTextArea(textToAppend);
				
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 10"); // TEST
				
				// Add the InstantMessageDialog to the currentConversations HashMap
				currentConversations.put(messageSender, tempDialog);
				
				System.out.println("InstantMessageClient::messageCheck (message) Checkpoint 11"); // TEST
			}
		} else {
			System.out.println("Error: Command received is not one of the following commands: friendslist, message."); 
		}
	}
	
	public static void main(String[] args) {
		
		// Instantiate an InstantMessageClient object, a Thread and call start() on it
		InstantMessageClient imClient = new InstantMessageClient(args[0]);
		Thread imClientThread = new Thread(imClient);
		imClientThread.start();
	}
}