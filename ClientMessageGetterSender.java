package so.eric.im;

import so.eric.im.messages.*;

import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientMessageGetterSender {
	
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ClientMessageGetterSender(Socket socket) {
		
		this.socket = socket;
		
		// Get input and output streams
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ioe) {
			System.out.println("Could not get ObjectInputStream on socket: " + socket.getLocalPort());
		}
		
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("Could not get ObjectOutputStream on socket: " + socket.getLocalPort());
		}
	}
	
	public Message getMessage() {
		
		// TODO error handling if cannot read message
		
		Message message;
		
		try {
			message = (Message) in.readObject();
			return message;
		} catch (IOException ioe) {
			System.out.println("Could not read from socket: " + socket.getPort());
			System.out.println("Socket's local port: " + socket.getLocalPort());
			
			// TODO create error Message type, instantiate one here and send it back
			return new CommandMessage("error", "error");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Error: Class not found");
			return new CommandMessage("error", "error");
		}
	}
	
	public boolean sendMessage(Message messageToSend) {
		
		try {
			out.writeObject(messageToSend);
			
		} catch (IOException ioe) {
			System.out.println("Could not write to socket: " + socket.getLocalPort());
			return false;
		}
		
		return true;
	}
}