/*
	This class is the message that contains the friends list that is sent from the server to each client as they log on
*/

package so.eric.im.messages;

import java.util.*;


public class FriendsListMessage extends Message {
	
	private List<String> friendsList;
	
	public FriendsListMessage(String sender, List<String> friendsList) {
		this.command = "friendslist";
		
		this.sender = sender;
		
		this.friendsList = new ArrayList<String>(friendsList);
	}
	
	public List<String> getFriendsList() {
		return friendsList;
	}
}