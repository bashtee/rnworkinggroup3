package llu.rn2.dzk.impl.parsing;

import java.util.Arrays;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.fields.FieldFactory;
import llu.rn2.dzk.interfaces.FieldPather;

public class SimpleFieldPather implements FieldPather {
	
	private byte[] fieldHeader=new byte[Field.headerSize];
	private int headerPointer=0;
	
	private short dataSize=-1;
	private byte[] data=null;
	private int dataPointer=0;
	
	@Override
	public Field toField() {
		if(isDone()) return FieldFactory.newField(fieldHeader,data);
		return null;
	}

	@Override
	public byte[] parse(byte[] in,int offset, int length) {
		int cursor=0;
		while(!((data!=null && dataSize==dataPointer )|| (offset+cursor)==length) ){
			if(data==null){
				fieldHeader[headerPointer]=in[offset+cursor];
				headerPointer++;
				if(headerPointer==Field.headerSize){
					dataSize=(short)((((short)fieldHeader[2])<<8) | ((short)fieldHeader[3]));
					data=new byte[dataSize];
				}
			}else{
				data[dataPointer]=in[offset+cursor];
				dataPointer++;
			}
			cursor++;
		}

		if((offset+cursor)==length)return null;
		return Arrays.copyOfRange(in,offset+cursor,length);
	}

	@Override
	public boolean isDone() {
		return data!=null && dataPointer==dataSize;
	}

}
