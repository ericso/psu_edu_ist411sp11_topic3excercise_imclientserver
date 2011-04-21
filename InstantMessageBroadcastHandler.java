// TODO go through this class and incorporate it into the main program

package so.eric.im.handler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InstantMessageBroadcastHandler implements ActionListener {
	private JTextField textField;
	private JList friendsList;
	private ListModel friendsListModel;
	
	public InstantMessageBroadcastHandler(JTextField textField, JList friendsList) {
		this.textField = textField;
		this.friendsList = friendsList;
		this.friendsListModel = friendsList.getModel();
	}
	
	public void actionPerformed(ActionEvent evt) {
		String friend;
		
		for (int i=0; i<friendsListModel.getSize(); i++) {
			// Get friend's name as String
			friend = (String) friendsListModel.getElementAt(i);
			System.out.println(friend + ": " + textField.getText());
		}
	}
}