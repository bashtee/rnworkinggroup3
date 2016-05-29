package llu.rn2.dzk.impl.parsing.messages;

import java.nio.ByteBuffer;

public class CommunHeader {

	public static int size = 12;

	public final byte version;
	public final byte type;
	public final short reserved;
	public final int serderIP;
	public final short serderPort;
	public final short fieldCount;

	public CommunHeader(byte version, byte type, short reserved, byte[] ip, int serderPort, int fieldCount) {
		this.version = version;
		this.type = type;
		this.reserved = reserved;
		ByteBuffer iip=ByteBuffer.wrap(ip);
		this.serderIP = iip.getInt();
		this.serderPort = (short)serderPort;
		this.fieldCount = (short)fieldCount;
	}

	public CommunHeader(byte[] formByte) {
		this.version = formByte[0];
		this.type = formByte[1];
		this.reserved = (short) ((((short) formByte[2]) << 8) | ((short) formByte[3]));
		this.serderIP = ((((((((int) formByte[4]) << 8) | (int) formByte[5]) << 8) | (int) formByte[6]) << 8)
				| (int) formByte[7]);
		this.serderPort = (short) ((((short) formByte[8]) << 8) | ((short) formByte[9]));
		this.fieldCount = (short) ((((short) formByte[10]) << 8) | ((short) formByte[11]));
	}

	public byte[] getIP(){
		ByteBuffer bb=ByteBuffer.allocate(4);
		bb.putInt(this.serderIP);
		return bb.array();
	}
	public CommunHeader(CommunHeader base, byte[] ip, int port) {
		version = base.version;
		type = base.type;
		reserved = base.reserved;
		ByteBuffer iip=ByteBuffer.wrap(ip);
		serderIP = iip.getInt();
		serderPort = (short) (port&0xffff);
		fieldCount = base.fieldCount;
	}

	public byte[] toByteArray() {
		byte[] byteArray = new byte[size];
		byteArray[0] = version;
		byteArray[1] = type;
		byteArray[2] = (byte) (reserved >> 8);
		byteArray[3] = (byte) reserved;

		byteArray[4] = (byte) (serderIP >> 24);
		byteArray[5] = (byte) (serderIP >> 16);
		byteArray[6] = (byte) (serderIP >> 8);
		byteArray[7] = (byte) (serderIP);

		byteArray[8] = (byte) (serderPort >> 8);
		byteArray[9] = (byte) (serderPort);
		byteArray[10] = (byte) (fieldCount >> 8);
		byteArray[11] = (byte) (fieldCount);

		return byteArray;
	}

}
