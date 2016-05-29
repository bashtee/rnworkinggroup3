package irc.interfaces;

import java.util.List;

public interface ISimpleChat {
	
	/**
	 * @author mader
	 * Function to receive all Users of this channel
	 * @return List of all User from this channel
	 * */
	public List<ISimpleUser> getUserList();
	
	
	/**
	 * @author mader
	 * Function to receive all actual Messages
	 * */
	public List<ISimpleMessage> getAllMessages();
	
	/**
	 * @author mader
	 * Function to add a User to this channel
	 * @param user New User
	 * */
	public void addUserToChannel(ISimpleUser user);
	
	/**
	 * @author mader
	 * Function to add a Message to this channel
	 * @param message Massage which should be added
	 * */
	public void addMessage(ISimpleMessage message);
	
	/**
	 * @author mader
	 * Function to remove a user from this channel
	 * @param user User who should be removed
	 * */
	public void removeUserFromChannel(ISimpleUser user);
}
