package so.eric.im.messages;

import java.io.*;

public abstract class Message implements Serializable {
	
	protected String sender;
	
	protected String command;
	
	public String getSender() {
		return sender;
	}
	
	public String getCommand() {
		return command;
	}
}