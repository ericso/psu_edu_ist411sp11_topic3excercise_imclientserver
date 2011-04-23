package so.eric.im.ui;

import so.eric.im.*;
import so.eric.im.handler.*;

import java.net.*;

import java.awt.*;
import javax.swing.*;

public class InstantMessageDialog extends JDialog {
	
	private String clientUsername;
	private ClientMessageGetterSender clientMessageGetterSender;
	private String recipient;
	
	private JButton sendButton;
	private JTextField textField;
	private JTextArea textArea;
	
	private SendMessageHandler sendMessageHandler;
	
	public InstantMessageDialog(Frame owner, String clientUsername, String recipient, ClientMessageGetterSender clientMessageGetterSender) {
		super(owner, recipient, false);
		
		System.out.println("InstantMessageDialog Checkpoint 1"); // TEST
		
		this.clientUsername = clientUsername;
		this.clientMessageGetterSender = clientMessageGetterSender;
		this.recipient = recipient;
		
		System.out.println("InstantMessageDialog Checkpoint 2"); // TEST
		
		// Make buttons and text field
		sendButton = new JButton("Send");
		textField = new JTextField(20);
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		// Make panel to hold buttons
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(textField);
		bottomPanel.add(sendButton);
		
		// Get the content pane and add components
		Container instantMessageDialogContentPane = this.getContentPane();
		instantMessageDialogContentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JScrollPane messageScrollPane = new JScrollPane(textArea);
		instantMessageDialogContentPane.add(messageScrollPane, BorderLayout.CENTER);
		
		JLabel recipientMessageLabel = new JLabel("Sending message to " + recipient);
		instantMessageDialogContentPane.add(recipientMessageLabel, BorderLayout.NORTH);
		
		this.setSize(400, 200);
		
		System.out.println("InstantMessageDialog Checkpoint 3"); // TEST
		
		// Make a SendMessageHandler ActionListener and register the buttons and the JTextArea
		sendMessageHandler = new SendMessageHandler(textField, textArea, recipient, this);
		sendButton.addActionListener(sendMessageHandler);
	}
	
	public void updateTextArea(String textToAppend) {
		
		System.out.println("InstantMessageDialog::updateTextArea Checkpoint 1"); // TEST
		
		// Add the sent text to the JTextArea
		textArea.append(textToAppend + "\n");
	}
	
	public void clearTextField() {
		textField.setText("");
	}
	
	public String getClientUsername() {
		return clientUsername;
	}
	
	public ClientMessageGetterSender getClientMessageGetterSender() {
		return clientMessageGetterSender;
	}
	
	public SendMessageHandler getSendMessageHandler() {
		return sendMessageHandler;
	}
}