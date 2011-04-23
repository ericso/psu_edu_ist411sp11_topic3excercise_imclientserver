package so.eric.im.main;

import so.eric.im.*;
import so.eric.im.messages.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class InstantMessageServer implements Runnable {
	
	private boolean listening;
	
	private int serverPort;
	private ServerSocket serverSocket;
	
	private Socket clientSocket;
	
	private List<String> registeredUsersList;
	private HashMap<String, ConnectedUser> loggedOnUsers;
	
	public InstantMessageServer() {
		this.listening = true;
		this.serverPort = 5555;
		
		try {
			serverSocket = new ServerSocket(serverPort);
//			serverSocket.setSoTimeout(60000); // Waits 60 seconds for connection on port
		} catch (IOException ioe) {
			System.out.println("Error: Could not listen on port: " + serverPort);
			System.exit(-1);
//		} catch (SocketTimeoutException s) {
//			System.out.println("Socket timed out");
//			System.exit(-1);
		}
		
		// The following code instantiates an ArrayList to hold the list of registered users
		// This is done here for simplicity.
		this.registeredUsersList = new ArrayList<String>();
		this.registeredUsersList.add("eric");
		this.registeredUsersList.add("brian");
		this.registeredUsersList.add("ollie");
		this.registeredUsersList.add("jenna");
		this.registeredUsersList.add("lacey");
		
		// Instantiate ArrayLists to hold Users currently logged on
		loggedOnUsers = new HashMap<String, ConnectedUser>();
	}
	
	public void run() {
		while (listening) {
			
			// Wait for a client to connect
			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			
			try {
				clientSocket = serverSocket.accept();
			} catch(IOException ioe) {
				System.out.println("Accept failed: " + serverSocket.getLocalPort());
				System.exit(-1);
			}
			
			// Read in Message from the client to get
			ServerMessageGetterSender getter = new ServerMessageGetterSender(clientSocket);
			
			Message message = getter.getMessage();
			
			// Check to see what the message is and act on it
			messageCheck(message, getter);
		}
	}
	
	public void messageCheck(Message messageToCheck, ServerMessageGetterSender messageCheckGetterSender) {
		
		// Get the message command and sender
		String command = messageToCheck.getCommand();
		String messageSender = messageToCheck.getSender();
		
		// Check to see if the sender is logged on
		boolean loggedOn = loggedOnUsers.containsKey(messageSender);
		
		// The message can only be of three different types: logon, logoff and message
		if (command.equals("logon")) {
			// Check to see if user is already logged on. If not
			// Instantiate a new ConnectedUser object, add to List and run Thread

			if (loggedOn) {
				System.out.println("User " + messageSender + " is already logged on.");
				// User may have been accidentally disconnected
				// TODO replace the socket in the appropriate ConnectedUser with the new one
			} else {
				// Log the user on: Instantiate a new User, Thread and start Thread
				ConnectedUser tempUser = new ConnectedUser(messageSender, messageCheckGetterSender, this);
				
				Thread tempThread = new Thread(tempUser);
				tempThread.start();
				
				// Add tempUser to loggedOnUsers ArrayList
				loggedOnUsers.put(messageSender, tempUser);
			}
			
			// Send the user the list of logged-on users
			FriendsListMessage friendsListMessage = new FriendsListMessage("server", this.registeredUsersList);
			messageCheckGetterSender.sendMessage(friendsListMessage);
			
		} else if (command.equals("logoff")) {
			// Check to see if the sender is logged on, log them off
			if (loggedOn) {
				System.out.println("Logging user " + messageSender + " off.");
				
				ConnectedUser tempUser = loggedOnUsers.remove(messageSender);
				tempUser.closeConnection();
			} else {
				System.out.println("User " + messageSender + " is not logged on.");
			}
		} else if (command.equals("message")) {
			// Check to see if the sender is logged on, if so, send the message to the recipient
			TextMessage textMessageToCheck = (TextMessage) messageToCheck;
			
			if (loggedOn) {
				// Get the recipient from the Message object
				String messageRecipient = textMessageToCheck.getRecipient();
				
				// Check to to see if the recipient is logged on
				boolean recipientLoggedOn = loggedOnUsers.containsKey(messageRecipient);
				
				if (recipientLoggedOn) {
					System.out.println("Sending message from " + messageSender + " to " + messageRecipient + ".");
					ConnectedUser recipientUser = (ConnectedUser) loggedOnUsers.get(messageRecipient);
					
					// get the recipient's ServerMessageGetterSender object to send the Message
					ServerMessageGetterSender sender = recipientUser.getServerMessageGetterSender();
					
					sender.sendMessage(textMessageToCheck);
				} else {
					System.out.println(messageRecipient + "is not logged on.");
				}
			} else {
				System.out.println("User " + messageSender + " is not logged on.");
			}
		} else {
			System.out.println("Error: Command received is not one of the following commands: logon, logoff, message."); 
		}
	}
	
	public static void main(String[] args) {
		
		InstantMessageServer imServer = new InstantMessageServer();
		Thread imServerThread = new Thread(imServer);
		imServerThread.start();
	}
}