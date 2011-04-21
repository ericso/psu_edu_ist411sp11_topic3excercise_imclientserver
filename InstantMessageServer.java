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
			serverSocket.setSoTimeout(60000); // Waits 60 seconds for connection on port
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
			
			// NOTE: clientSocket is the socket returned by the ServerSocket.accept() method
			// This socket represents the server's end of the connection. It is stored in the
			// ConnectedUser objects and can be retrieved by the server to write to and read from.
			
			System.out.println("InstantMessageServer: Checkpoint 1"); // TEST
			
			// Read in Message from the client to get 
			ServerMessageGetterSender getter = new ServerMessageGetterSender(clientSocket);
			
			System.out.println("InstantMessageServer: Checkpoint 2"); // TEST
			
			Message message = getter.getMessage();
			
			System.out.println("InstantMessageServer: Checkpoint 3"); // TEST
			
			// Check to see what the message is and act on it
			messageCheck(message, getter);
			
			System.out.println("InstantMessageServer: Checkpoint 4"); // TEST
			
			// Now we need a loop, thread to continously read and act on incomming messages
			// The loop has to go through each ConnectedUser and call readObject() on the InputStream
			// Can this be done in the ConnectedUser class?
			// When a message is received by the ConnectedUser object, this represents a message from that user,
			// The ConnectedUser has to somehow send that to the server to be parsed
			// The ConnectedUser can call messageCheck() and pass in the message and its own socket
			
		}
	}
	
	public void messageCheck(Message messageToCheck, ServerMessageGetterSender messageCheckGetterSender) {
		
		System.out.println("InstantMessageServer::messageCheck Checkpoint 1"); // TEST
		
		// Get the message command and sender
		String command = messageToCheck.getCommand();
		String messageSender = messageToCheck.getSender();
		
		System.out.println("InstantMessageServer::messageCheck Checkpoint 2"); // TEST
		
		// Check to see if the sender is logged on
		boolean loggedOn = loggedOnUsers.containsKey(messageSender);
		
		System.out.println("InstantMessageServer::messageCheck Checkpoint 3"); // TEST
		
		// The message can only be of three different types: logon, logoff and message
		if (command.equals("logon")) {
			
			System.out.println("InstantMessageServer::messageCheck (logon) Checkpoint 1"); // TEST
			
			// Check to see if user is already logged on. If not
			// Instantiate a new ConnectedUser object, add to List and run Thread

			if (loggedOn) {
				System.out.println("User " + messageSender + " is already logged on.");
				// User may have been accidentally disconnected
				// TODO replace the socket in the appropriate ConnectedUser with the new one
			} else {
				
				System.out.println("InstantMessageServer::messageCheck (logon) Checkpoint 2"); // TEST
				
				// Log the user on: Instantiate a new User, Thread and start Thread
				ConnectedUser tempUser = new ConnectedUser(messageSender, messageCheckGetterSender, this);
				
				System.out.println("InstantMessageServer::messageCheck (logon) Checkpoint 3"); // TEST
				
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
			
			// Cast messageToCheck to a TextMessage before calling getTransmissionFlag()
			TextMessage textMessageToCheck = (TextMessage) messageToCheck;
			
			if (loggedOn) {
				// Get the recipient from the Message object
				String messageRecipient = textMessageToCheck.getRecipient();
				
				// Check to to see if the recipient is logged on
				boolean recipientLoggedOn = loggedOnUsers.containsKey(messageRecipient);
				
				if (recipientLoggedOn) {
					System.out.println("Sending message from " + messageSender + " to " + messageRecipient + ".");
					ConnectedUser recipientUser = (ConnectedUser) loggedOnUsers.get(messageRecipient);
					
					System.out.println("InstantMessageServer::messageCheck (message) Checkpoint 4"); // TEST
					
					// get the recipient's ServerMessageGetterSender object to send the Message
					ServerMessageGetterSender sender = recipientUser.getServerMessageGetterSender();
					
					System.out.println("InstantMessageServer::messageCheck (message) Checkpoint 5"); // TEST
					
					// TODO need to flip the sender and the recipient?
					
					sender.sendMessage(textMessageToCheck);
					
					System.out.println("InstantMessageServer::messageCheck (message) Checkpoint 6"); // TEST
						
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