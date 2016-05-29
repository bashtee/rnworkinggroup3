package llu.rn2.dzk.impl.parsing.fields;

public class PortField extends Field {

	public PortField(int  port) {
		super(FieldFactory.TYPE_PORT,(short)2,toByteArray((short)port));
	}
	
	public PortField(byte[] header, byte[] data) {
		super(header, data);
	}

	public int value() {
		return (int)((((int)data[0])<<8)|((int)data[1]))&0xFFFF;
	}

}
