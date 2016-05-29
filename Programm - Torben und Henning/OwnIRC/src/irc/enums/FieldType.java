package irc.enums;

import java.util.Arrays;

import irc.classes.IRCUtils;

public enum FieldType {

	IP(IRCUtils.ZERO,(byte)0x01),
	PORT(IRCUtils.ZERO,(byte)0x02),
	USER_LIST(IRCUtils.ZERO,(byte)0x03),
	NAME(IRCUtils.ZERO,(byte)0x04),
	TEXT(IRCUtils.ZERO,(byte)0x05);

	private final byte _type1;
	private final byte _type2;
	
	FieldType(byte type1,byte type2){
		this._type1 = type1;
		this._type2 = type2;
	}
	
	public byte[] getType(){
		return new byte[]{_type1,_type2};
	}
	
	public static FieldType valueOf(byte[] bytes){
		for(FieldType elem: FieldType.values()){
			if(Arrays.equals(elem.getType(), bytes))
				return elem;
		}
		return null;
	}
	
}
