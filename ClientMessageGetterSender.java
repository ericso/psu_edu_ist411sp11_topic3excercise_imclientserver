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
		
		// TODO make separate MessageGetterSender objects for the client and server
		// For server, swap input and output stream retrieval
		
		// TEST
		System.out.println("ClientMessageGetterSender: Checkpoint 1");
		
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
		
		System.out.println("ClientMessageGetterSender: Checkpoint 2");
	}
	
	public Message getMessage() {
		
		// TODO error handling if cannot read message
		
		Message message;
		
		System.out.println("ClientMessageGetterSender::getMessage: Checkpoint 1");
		
		try {
			System.out.println("ClientMessageGetterSender::getMessage: Checkpoint 2");
			
			// blocking here, whY?
			// What else is using the inputstream?
			// how can I test?
			message = (Message) in.readObject();
			
			System.out.println("ClientMessageGetterSender::getMessage: Checkpoint 3");
			
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
		
		System.out.println("ClientMessageGetterSender::sendMessage: Checkpoint 1");
		
		try {
			out.writeObject(messageToSend);
			
			System.out.println("ClientMessageGetterSender::sendMessage: Checkpoint 2");
			
		} catch (IOException ioe) {
			System.out.println("Could not write to socket: " + socket.getLocalPort());
			return false;
		}
		
		return true;
	}
}