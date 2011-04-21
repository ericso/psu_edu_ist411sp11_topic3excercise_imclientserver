package so.eric.im.handler;

import so.eric.im.*;
import so.eric.im.ui.*;

import java.net.*;

import java.awt.event.*;
import javax.swing.*;

public class DisplayMessageDialogHandler extends MouseAdapter {
	
	private String clientUsername;
	private ClientMessageGetterSender clientMessageGetterSender;
	
	private int numberOfClicks;
	
	public DisplayMessageDialogHandler(String clientUsername, ClientMessageGetterSender clientMessageGetterSender) {
		super();
		
		this.clientUsername = clientUsername;
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
			
			System.out.println("DisplayMessageDialogHandler Checkpoint 1"); // TEST
			
			InstantMessageDialog imDialog = new InstantMessageDialog(null, clientUsername, recipient, clientMessageGetterSender);
			
			System.out.println("DisplayMessageDialogHandler Checkpoint 2"); // TEST
			
			imDialog.setVisible(true);
			
			System.out.println("DisplayMessageDialogHandler Checkpoint 3"); // TEST
			
		}
	}
}