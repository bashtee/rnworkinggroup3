package irc.classes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleUser;

/**
 * @author mader
 * Utilclass for this project
 * */
public final class IRCUtils {

	public static final int HEADER_BYTE_SIZE = 12;
	public static final int FIELD_HEADER_BYTE_SIZE = 4;
	public static final int ALINGMENT = 4;
	public static final int IP_POS = 4;
	public static final int IP_LENGTH = 4;
	public static final byte ZERO = 0x0;
	public static final int USER_BYTE_SIZE = 4;
	public static final String ENCODING = "ASCII";
	public static final int NUMBER_OF_WORKER = 4;
	public static final int IP_POS_END = 8;
	public static final long TIMEOUT = 1000000000l;
	public static final int FIELDTYPE_LENGTH = 2;
	public static final int FIELDLENGTH_LENGTH = 2;
	public static final int VERSION = 1;
	public static final int USERLIST_SIZE = 8;
	public static final int USERLIST_RESERVED_BYTES = 2;
	
	private static final Pattern _ipv4Regex = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	
	//Constructor
	private IRCUtils(){}

	
	/**
	 * @author mader
	 * Function to check if a Nickname is valid
	 * @param nickname Nickname which should be checked
	 * @return True if this nickname is valid, false if not
	 * */
	public static boolean checkNickname(String nickname){
		return nickname != null && nickname.length()>2;
	}
	
	/**
	 * @author mader
	 * Function to check if a IP is valid
	 * @param ip IP as String
	 * @return true if it is an ip false if not
	 * */
	public static boolean checkIP(String ip){
		if(ip == null) return false ;
		Matcher m = _ipv4Regex.matcher(ip);
		return m.matches();
	}
	
	/**
	 * @author mader
	 * Function to create a FieldHeader
	 * @param type FieldType which should be in the Header
	 * @param length length of the following block
	 * */
	public static byte[] createFieldHeader(FieldType type, int length){
		byte[] header = new byte[FIELD_HEADER_BYTE_SIZE];
		byte[] byteType = type.getType();
		byte[] byteLength = ByteBuffer.allocate(Integer.BYTES).putInt(length).array();
		header[0] = byteType[0];
		header[1] = byteType[1];
		header[2] = byteLength[2];
		header[3] = byteLength[3];
		return header;
	}
	
	/**
	 * Function to concat a number of Arrays of byte
	 * @author mader
	 * 
	 * */
	public static byte[] addAll(byte[]... fields){
		int length = 0;
		for(byte[] field: fields){
			length += field.length;
		}
		//int allign = length % ALINGMENT; for alignment
		//length += allign;
		byte[] result = new byte[length];
		int pos = 0;
		for(byte[] field:fields){
			for(byte elem : field){
				result[pos] = elem;
				pos++;
			}
		}
		while(pos<length){
			result[pos] = ZERO;  
			pos++;
		}
		return result;
	}
	
	/**
	 * Function to concat a Collection of bytearray
	 * @author mader
	 * @param coll Collection of bytearray
	 * @return a bytearray which contains all bytearrays of coll. 
	 * */
	public static byte[] addAll(Collection<? extends byte[]> coll){
		int length = 0;
		for(byte[] field: coll){
			length += field.length;
		}
		//int allign = length % ALINGMENT; for alignment
		//length += allign;
		byte[] result = new byte[length];
		int pos = 0;
		for(byte[] field: coll){
			for(byte elem : field){
				result[pos] = elem;
				pos++;
			}
		}
		while(pos<length){
			result[pos] = ZERO;  
			pos++;
		}
		return result;
	}
	
	/**
	 * Function to convert a byte to a unsigned Int
	 * @author mader
	 * @param bytes byte which should be converted
	 * @return int Unsigned Integer
	 * */
	public static int convertByteToInt(byte bytes){
		return ((int)bytes) & 0xFF;
	}
	
	/**
	 * Function to convert two bytes to a unsigned Int
	 * @author mader
	 * @param bytes byte which should be converted
	 * @return int Unsigned Integer
	 * */
	public static int convertByteToInt(byte byte1, byte byte2){
		return ((byte1 << 8) & 0x0000ff00) | (byte2 & 0x000000ff);
	}

	/**
	 * Function to convert two bytes to a unsigned Int
	 * @author mader
	 * @param bytes byte which should be converted
	 * @return int Unsigned Integer
	 * */
	public static int convertByteToInt(byte byte1, byte byte2,byte byte3, byte byte4){
		return ( (byte1 << 24) & 0xff000000) | ((byte1 << 16) & 0xff0000) | ((byte1 << 8) & 0xff00) | (byte2 & 0xff);
	}
	
	/**
	 * Function to decode the Content
	 * @param conBytes Arrays of all contentbytes which should be encoded
	 * @param numOfFields nuber of Fields
	 * @return List<ISimpleContent> Content as List of contents
	 * */
	public static List<ISimpleContent> decodeContent(byte[] conBytes, int numOfFields){
		List<ISimpleContent> contentList = new ArrayList<>();
		for(int i = 0; i< numOfFields; i++){
			ISimpleContent content = Values.createNewContent(conBytes);
			if(content == null) return null;
			contentList.add(content);
			conBytes = ArrayUtils.subarray(conBytes, content.getLength()+IRCUtils.FIELD_HEADER_BYTE_SIZE, conBytes.length);
		}
		
		return contentList;
	}
	
	/**
	 * Function to get a content out of a list of contents
	 * @author mader
	 * @param contents List of Contents
	 * @param wanted FieldType of the wanted Content
	 * @return List of Contents with the FieldType
	 * */
	public static ISimpleContent contentsOfContents(List<ISimpleContent> contents, FieldType wanted){
		//List<ISimpleContent> result = new ArrayList<>();
		for(ISimpleContent elem : contents){
			if(elem.getFieldType() == wanted){
				return elem;
			}
		}
		return null;
	}


	public static int getUsedBytes(List<ISimpleContent> contentList) {
		int bytes = 0;
		for(ISimpleContent elem : contentList){
			bytes += FIELD_HEADER_BYTE_SIZE+ elem.getLength();
		}
		return bytes;
	}
	
	public static byte[] ipToByte(String _ip) {
		byte[] ip = new byte[IRCUtils.IP_LENGTH];
		String[] ipArr = _ip.split("\\.");
		for(int i = 0; i< ipArr.length; i++){
			ip[i] = (byte) Integer.parseInt(ipArr[i]);
		}
		return ip;
	}
}
