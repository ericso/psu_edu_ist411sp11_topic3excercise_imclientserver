package so.eric.im.messages;

public class CommandMessage extends Message {
	
	public CommandMessage(String sender, String command) {
		this.sender = sender;
		this.command = command;
	}
}