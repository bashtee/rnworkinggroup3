package irc.interfaces;

import irc.enums.MessageType;

public interface ISimpleHeader {

	/**
	 * @author mader
	 * Function to return the protocol version
	 * @return int Protocolversion
	 * */
	public int getVersion();
	
	/**
	 * @author mader
	 * Function to return the Header as byte Array (version 1, Message type 1, reserved 2, Sender IP 4, sender port 2, Number of Fields 2; -> 12 bytes)
	 * @return byte[] 
	 * */
	public byte[] toByteHeader();
	
	/**
	 * @author mader
	 * Function to receive the Senderport
	 * @return int Sender Port;
	 * */
	public int getSenderPort();
	
	/**
	 * @author mader
	 * Function to receive the Sender IP as String
	 * @return String Sender IP
	 * */
	public String getSenderIP();
	
	/**
	 * @author mader
	 * Function to get the messagetype
	 * @return MEssageTypes type of Message
	 * */
	public MessageType getMessageType();
	
	/**
	 * @author mader
	 * Function to get the Number of field from this Message
	 * @return int number of fields
	 * */
	public int getNumberOfFields();
	
	/**
	 * @author mader
	 * Function to get the whole Sender
	 * @return ISimpleUser sender of the Message
	 * */
	public ISimpleUser getSender();
	
	/**
	 * @author mader
	 * Function to set the number of Fields
	 * @param numberOfFields Number of Field from this message. This number must be >=0 else the number of field'll be set to 0
	 * */
	public void setNumberOfFields(int numberOfFields);
	
}
