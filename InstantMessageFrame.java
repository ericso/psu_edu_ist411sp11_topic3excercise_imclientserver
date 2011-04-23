package so.eric.im.ui;

import so.eric.im.*;
import so.eric.im.handler.*;

import java.net.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

public class InstantMessageFrame extends JFrame {
	
	private String clientUsername;
	private JList friendsList;
	private HashMap<String, InstantMessageDialog> currentConversations;
	private ClientMessageGetterSender clientMessageGetterSender;
	
	public InstantMessageFrame(String clientUsername, java.util.List<String> friendsListArray, HashMap<String, InstantMessageDialog> currentConversations,  ClientMessageGetterSender clientMessageGetterSender) {
		super(clientUsername);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.clientUsername = clientUsername;
		this.friendsList = new JList(friendsListArray.toArray());
		this.currentConversations = currentConversations;
		this.clientMessageGetterSender = clientMessageGetterSender;
		
		
		// Setup the InstantMessageFrame
		Container instantMessageFrameContentPane = this.getContentPane();
		instantMessageFrameContentPane.setLayout(new BorderLayout());
		instantMessageFrameContentPane.add(getFriendsPane(), BorderLayout.CENTER);
		
		// Create handler for double-clicks on the friends list
		DisplayMessageDialogHandler displayMessageDialogHandler = new DisplayMessageDialogHandler(clientUsername, currentConversations, clientMessageGetterSender);
		friendsList.addMouseListener(displayMessageDialogHandler);
	}
	
	public JScrollPane getFriendsPane() {
		JScrollPane pane = new JScrollPane(friendsList);
		return pane;
	}
	
}