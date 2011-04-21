package so.eric.im.messages;

public class TextMessage extends Message {
	
	private String recipient;
	private String messageText;
	
	
	public TextMessage(String sender, String recipient, String messageText) {
		
		// The command field in a TextMessage is set to "message" by default
		this.command = "message";
		this.sender = sender;
		this.recipient = recipient;
		this.messageText = messageText;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public String getMessageText() {
		return messageText;
	}
}