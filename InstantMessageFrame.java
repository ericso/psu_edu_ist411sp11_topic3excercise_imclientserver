package so.eric.im.ui;

import so.eric.im.*;
import so.eric.im.handler.*;

import java.net.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

public class InstantMessageFrame extends JFrame {
	
	private String clientUsername;
	
	private ClientMessageGetterSender clientMessageGetterSender;
	
	private JTextField messageTextField;
	private JList friendsList;
	private JButton sendButton;
	
	public InstantMessageFrame(String clientUsername, java.util.List<String> friendsListArray, ClientMessageGetterSender clientMessageGetterSender) {
		super(clientUsername);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.clientUsername = clientUsername;
		this.clientMessageGetterSender = clientMessageGetterSender;
		this.friendsList = new JList(friendsListArray.toArray());
		
		// Setup the InstantMessageFrame
		Container instantMessageFrameContentPane = this.getContentPane();
		instantMessageFrameContentPane.setLayout(new BorderLayout());
		instantMessageFrameContentPane.add(getMessagePanel(), BorderLayout.SOUTH);
		instantMessageFrameContentPane.add(getFriendsPane(), BorderLayout.CENTER);

// TODO Remove this code about InstantMessageBroadcastHandler
		// Create handler to handle sending IMs and add to Send button and Enter key
		InstantMessageBroadcastHandler broadcastHandler = new InstantMessageBroadcastHandler(messageTextField, friendsList);
		sendButton.addActionListener(broadcastHandler);
		messageTextField.addActionListener(broadcastHandler);
		
		// Create handler for double-clicks on the friends list
		DisplayMessageDialogHandler displayMessageDialogHandler = new DisplayMessageDialogHandler(clientUsername, clientMessageGetterSender);
		friendsList.addMouseListener(displayMessageDialogHandler);
	}
	
	public JPanel getMessagePanel() {
		JPanel messagePanel = new JPanel(new BorderLayout());
		
		messageTextField = new JTextField();
		sendButton = new JButton("Send");
		
		messagePanel.add(messageTextField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		return messagePanel;
	}
	
	public JScrollPane getFriendsPane() {
		JScrollPane pane = new JScrollPane(friendsList);
		return pane;
	}
	
}