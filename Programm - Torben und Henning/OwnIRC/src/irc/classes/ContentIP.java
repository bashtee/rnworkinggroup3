package irc.classes;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContent;

final class ContentIP implements ISimpleContent{

	private FieldType _type = FieldType.IP;
	
	private String _ip;
	
	
	private ContentIP(String ip){
		this._ip = ip;
	}
	
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(String ip){
		return new ContentIP(ip);
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContent valueOf(byte[] bytes,int length){
		String ip = "";
		for(int i = 0 ; i < IRCUtils.IP_LENGTH; i++){
			ip += IRCUtils.convertByteToInt(bytes[i]);
			 if(i<IRCUtils.IP_LENGTH-1) ip += ".";
		}
		return new ContentIP(ip);
	}

	@Override
	public byte[] contentToBytes() {
		return IRCUtils.addAll(IRCUtils.createFieldHeader(_type,getLength()),IRCUtils.ipToByte(_ip));
	}


	@Override
	public String contentAsString() {
		return _ip;
	}

	@Override
	public int getLength() {
		return IRCUtils.IP_LENGTH;
	}

	@Override
	public FieldType getFieldType() {
		return _type;
	}
}
