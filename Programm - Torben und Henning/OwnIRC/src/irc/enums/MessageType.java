package irc.enums;

public enum MessageType {
	
	LOGIN((byte)0x1),
	LOGOUT((byte)0x2),
	TEXT_MESSAGE((byte)0x3),
	MY_NAME((byte)0x4);
	
	private final byte _type1;
	
	MessageType(byte type1){
		this._type1 = type1;
		
	}
	
	public byte getType(){
		return this._type1;
	}
	
	public static MessageType valueOf(byte bytes){
		
		for(MessageType elem : MessageType.values()){
			if(elem.getType() == bytes)
				return elem;
		}
		return null;
	}

}
