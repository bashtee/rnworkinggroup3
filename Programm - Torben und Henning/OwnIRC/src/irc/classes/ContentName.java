package irc.classes;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContent;

final class ContentName implements ISimpleContent{

	private FieldType _type = FieldType.NAME;
	
	private String _name;
	
	private ContentName(String name){
		this._name = name;
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(String name){
		return new ContentName(name);
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(byte[] bytes,int length){
		String name = "";
		try {
			name = new String(ArrayUtils.subarray(bytes,0,length+1),IRCUtils.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ContentName(name);
	}

	@Override
	public byte[] contentToBytes() {
		byte[] name = new byte[0];
		try {
			name = _name.getBytes(IRCUtils.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return IRCUtils.addAll(IRCUtils.createFieldHeader(_type, getLength()),name);
	}

	@Override
	public String contentAsString() {
		return _name;
	}

	@Override
	public int getLength() {
		byte[] name;
		try {
			name = _name.getBytes(IRCUtils.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		}
		return name.length;
	}

	@Override
	public FieldType getFieldType() {
		return _type;
	}
}
