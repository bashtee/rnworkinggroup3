package llu.rn2.dzk.impl.parsing.fields;

public class Field {
	public final short type;
	public final short datalength;
	public final byte[] data;
	
	public static final int headerSize=4;

	protected Field(short type,short datalength,byte[] data){
		this.type=type;
		this.datalength=datalength;
		this.data=data;
	}
	
	public Field(byte[] header,byte[] data){
		type=(short)(((short)header[0]<<8)|((short)header[1]));
		datalength=(short)(((short)header[2]<<8)|((short)header[3]));
		this.data=data;
	}
	
	protected static byte[] toByteArray(int ip){
		byte[] tmp={(byte)(ip>>24),(byte)(ip>>16),(byte)(ip>>8),(byte)(ip)};
		return tmp;
	}
	protected static byte[] toByteArray(short port){
		byte[] tmp={(byte)(port>>8),(byte)(port)};
		return tmp;
	}
	
	public byte[] toByteArray(){
		byte[] array=new byte[datalength+headerSize];
		
		array[0]=(byte)(type>>8);
		array[1]=(byte)(type);
		array[2]=(byte)(datalength>>8);
		array[3]=(byte)(datalength);
		System.arraycopy(data, 0, array, 4, datalength);
		
		return array;
	}
}
