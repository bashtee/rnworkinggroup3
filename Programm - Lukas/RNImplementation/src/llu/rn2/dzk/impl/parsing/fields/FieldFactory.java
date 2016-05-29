package llu.rn2.dzk.impl.parsing.fields;

public class FieldFactory {

	private FieldFactory (){}
	
	public static final short TYPE_IP = 0x01;
	public static final short TYPE_PORT = 0x02;
	public static final short TYPE_USERLIST = 0x03;
	public static final short TYPE_NAME = 0x04;
	public static final short TYPE_TEXT = 0x05;

	public static Field newField(byte[] fieldHeader, byte[] data) {
		short type = (short) (((short) fieldHeader[0]) << 8 | ((short) fieldHeader[1]));

		switch (type) {
		case TYPE_IP:
			return new IpField(fieldHeader, data);
		case TYPE_PORT:
			return new PortField(fieldHeader, data);
		case TYPE_USERLIST:
			return new UserListField(fieldHeader, data);
		case TYPE_NAME:
			return new NameField(fieldHeader, data);
		case TYPE_TEXT:
			return new TextField(fieldHeader, data);
		default:
			return null;
		}
	}
}
