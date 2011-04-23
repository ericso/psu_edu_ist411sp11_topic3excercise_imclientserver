package so.eric.im.handler;

import so.eric.im.*;
import so.eric.im.ui.*;

import java.net.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.*;

public class DisplayMessageDialogHandler extends MouseAdapter {
	
	private String clientUsername;
	private HashMap<String, InstantMessageDialog> currentConversations;
	private ClientMessageGetterSender clientMessageGetterSender;
	
	private int numberOfClicks;
	
	public DisplayMessageDialogHandler(String clientUsername, HashMap<String, InstantMessageDialog> currentConversations, ClientMessageGetterSender clientMessageGetterSender) {
		super();
		
		this.clientUsername = clientUsername;
		this.currentConversations = currentConversations;
		this.clientMessageGetterSender = clientMessageGetterSender;
	}
	
	public void mouseClicked(MouseEvent evt) {
		// Get the source of the clicks
		JList list = (JList) evt.getSource();
		
		// Get the number of clicks
		numberOfClicks = evt.getClickCount();
		
		// Check for a double-click
		if (numberOfClicks == 2) {
			int index = list.locationToIndex(evt.getPoint());
			String recipient = (String) list.getSelectedValue();
			System.out.println("Talking to " + index + ". " + recipient);
			
			InstantMessageDialog tempDialog = new InstantMessageDialog(null, clientUsername, recipient, clientMessageGetterSender);
			
			// Add the JDialog to the currentConversations HashMap
			currentConversations.put(recipient, tempDialog);
			
			tempDialog.setVisible(true);
			
		}
	}
}