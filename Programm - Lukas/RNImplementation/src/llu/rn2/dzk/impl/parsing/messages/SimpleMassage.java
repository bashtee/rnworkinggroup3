package llu.rn2.dzk.impl.parsing.messages;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;

public class SimpleMassage extends Massage{

	protected CommunHeader header;
	protected Collection<Field> fields;
	
	public SimpleMassage(CommunHeader header,Collection<Field> fields){
		this.header=header;
		this.fields=fields;
	}
	
	@Override
	public byte[] toByteArray() {
		byte[] array = header.toByteArray();
		for(Field f:fields){
			array=concat(array,f.toByteArray());
		}
		return array;
	}
	private byte[] concat(byte[] a, byte[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   byte[] c= new byte[aLen+bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}
	
	public void updateHeader(byte[] ip,int port){
		header=new CommunHeader(header,ip,port);
	}

	@Override
	public CommunHeader getHeader() {
		return header;
	}
	
	
	

}
