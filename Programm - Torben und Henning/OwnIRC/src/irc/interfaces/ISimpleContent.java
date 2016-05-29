package irc.interfaces;

import irc.enums.FieldType;

public interface ISimpleContent {
	
	/**
	 * Function to get the Content as bytes
	 * @author mader
	 * @return Content as Bytearray, if the Content contains a String this String is encoded as defined in IRCUtils
	 * */
	public byte[] contentToBytes();
	
	/**
	 * Function to get the Content as String
	 * @author mader
	 * @return string content as String
	 * */
	public String contentAsString();
	
	/**
	 * Function to get the length of the Content
	 * @author mader
	 * @return int length of the content
	 * */
	public int getLength();
	
	/**
	 * Function to get the FieldType of the Content
	 * @author mader
	 * @return FieldType of the Content
	 * */
	public FieldType getFieldType();
}
