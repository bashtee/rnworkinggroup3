package irc.classes;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContent;

final class ContentPort implements ISimpleContent{
	
	private FieldType _type = FieldType.PORT;
	
	private int _port;

	private ContentPort(int port){
		_port = port;
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(int port){
		return new ContentPort(port);
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(byte[] bytes,int length){
		return new ContentPort(IRCUtils.convertByteToInt(bytes[0], bytes[1]));
	}

	@Override
	public byte[] contentToBytes() {
		byte[] port = ByteBuffer.allocate(Integer.BYTES).putInt(_port).array();
		return IRCUtils.addAll(IRCUtils.createFieldHeader(_type, getLength()),ArrayUtils.subarray(port, 2, 4));
	}

	@Override
	public String contentAsString() {
		return Integer.toString(_port);
	}

	@Override
	public int getLength() {
		return IRCUtils.FIELDLENGTH_LENGTH;
	}

	@Override
	public FieldType getFieldType() {
		return _type;
	}
}
