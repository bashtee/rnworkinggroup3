package irc.interfaces;

import java.util.List;

import irc.enums.MessageType;

public interface ISimpleMessage {
	
	/**
	 * @author mader
	 * Function to get the sender of this Message
	 * @return Sender of this Message as user
	 * */
	public ISimpleUser getSender();
	
	/**
	 * @author mader
	 * Function to get the recipients of this message
	 * @return Recipients as List of ISimpleUser
	 * */
	public List<ISimpleUser> getRecipients();
	
	/**
	 * @author mader
	 * Function to get the message as string
	 * @return Message as string
	 * */
	public List<ISimpleContent> getWholeMessage();
	
	/**
	 * @author mader
	 * Function to get the Header of the Message
	 * @return ISimpleHeader Header of the Message
	 * */
	public ISimpleHeader getHeader();
	
	
	/**
	 * @author mader
	 * Function to tranform the whole Message to bytes
	 * @return byte[] Message as bytes
	 * */
	public byte[] messageToBytes();
	
	/**
	 * Function to get the MessageType
	 * @author mader
	 * @return MessageType
	 * */
	public MessageType getMessageType();
	
}
