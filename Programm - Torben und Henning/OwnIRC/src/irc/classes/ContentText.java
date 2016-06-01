package irc.classes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContent;

final class ContentText implements ISimpleContent{
	
	private final String _message;
	
	private final FieldType _type = FieldType.TEXT;

	private ContentText(String message){
		this._message = message;
	} 
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(String message){
		return new ContentText(message);
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(byte[] message,int length){
		try {
			return new ContentText(new String(ArrayUtils.subarray(message, 0, length),IRCUtils.ENCODING));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] contentToBytes() {
		return IRCUtils.addAll(IRCUtils.createFieldHeader(_type, getLength()),messageToByte());
	}
	
	private byte[] messageToByte(){
		return _message.getBytes(Charset.forName(IRCUtils.ENCODING));
	}

	@Override
	public String contentAsString() {
		return _message;
	}

	@Override
	public int getLength() {
		return _message.length();
	}
	
	public FieldType getFieldType(){
		return this._type;
	}

}
