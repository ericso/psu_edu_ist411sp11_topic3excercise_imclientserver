package so.eric.im.handler;

import so.eric.im.*;
import so.eric.im.ui.*;
import so.eric.im.messages.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SendMessageHandler implements ActionListener {
	
	private JTextField textField;
	private JTextArea textArea;
	private String recipient;
	private InstantMessageDialog dialog;
	
	private String clientUsername;
	private String textToSend;
	
	public SendMessageHandler(JTextField textField, JTextArea textArea, String recipient, InstantMessageDialog dialog) {
		this.textField = textField;
		this.textArea = textArea;
		this.recipient = recipient;
		this.dialog = dialog;
		
		this.clientUsername = dialog.getClientUsername();
		this.textToSend = textField.getText();
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Get button clicked
		JButton buttonClicked = (JButton) evt.getSource();
		String actionCommand = new String(buttonClicked.getActionCommand());
		
		// TODO add event for Enter key being pressed
		
		textToSend = textField.getText();
		
		if (actionCommand.equals("Send")) {
			System.out.println(recipient + ": " + textToSend);
			
			// Create the Message object to send and send using a ClientMessageGetterSender object
			TextMessage messageToSend = new TextMessage(clientUsername, recipient, textToSend);
			
			ClientMessageGetterSender sender = dialog.getClientMessageGetterSender();
			sender.sendMessage(messageToSend);
			
			// Add the sent text to the JTextArea
			dialog.updateTextArea(textToSend);
			dialog.clearTextField();
		}
	}
}