package so.eric.im;

import so.eric.im.messages.*;

import java.net.*;
import java.io.*;
import java.lang.*;

public class ServerMessageGetterSender {
	
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ServerMessageGetterSender(Socket socket) {
	
		System.out.println("ServerMessageGetterSender: Checkpoint 1"); // TEST
		
		this.socket = socket;
		
		System.out.println("Server socket port: " + socket.getLocalPort()); // TEST
		
		// Get input and output streams in reverse order of ClientMessageGetterSender class
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("Could not get ObjectOutputStream on socket: " + socket.getLocalPort());
		}
		
		System.out.println("ServerMessageGetterSender: Checkpoint 2");
		
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ioe) {
			System.out.println("Could not get ObjectInputStream on socket: " + socket.getLocalPort());
		}
		
		System.out.println("ServerMessageGetterSender: Checkpoint 3");
	}
	
	public Message getMessage() {
		
		// TODO error handling if cannot read message
		
		System.out.println("ServerMessageGetterSender::getMessage Checkpoint 1");
		
		Message message;
		
		try {
			message = (Message) in.readObject();
			return message;
		} catch (IOException ioe) {
			System.out.println("Could not read from socket: " + socket.getLocalPort());
			
			// TODO create error Message type, instantiate one here and send it back
			return new CommandMessage("error", "error");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Error: Class not found");
			return new CommandMessage("error", "error");
		}
	}
	
	public boolean sendMessage(Message messageToSend) {
		
		System.out.println("ServerMessageGetterSender::sendMessage Checkpoint 1");
		
		try {
			out.writeObject(messageToSend);
		} catch (IOException ioe) {
			System.out.println("Could not write to socket: " + socket.getLocalPort());
			return false;
		}
		
		return true;
	}
}