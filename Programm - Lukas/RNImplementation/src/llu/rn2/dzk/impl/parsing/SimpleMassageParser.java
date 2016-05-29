package llu.rn2.dzk.impl.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.messages.CommunHeader;
import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.impl.parsing.messages.MassageFactory;
import llu.rn2.dzk.interfaces.FieldPather;
import llu.rn2.dzk.interfaces.MassageParser;

public class SimpleMassageParser implements MassageParser {
	
	private byte[] communHeaderRaw=new byte[CommunHeader.size];
	private int headerPointer=0;
	
	private CommunHeader communHeader=null;
	
	private Collection<Field> fields=new ArrayList<>();
	FieldPather fp;
	@Override
	public Massage toMessage() {		
		if(isDone()) return MassageFactory.newMassage(communHeader, fields);
		return null;
	}

	@Override
	public byte[] parse(byte[] in,int length) {
		int cursor=0;
		while(!(isDone()||cursor==length)){
			if(communHeader==null){
				communHeaderRaw[headerPointer]=in[cursor];
				headerPointer++;
				cursor++;
				if(headerPointer==CommunHeader.size){
					communHeader=new CommunHeader(communHeaderRaw);
					fp = new SimpleFieldPather();
				}
			}else{
				in=fp.parse(in,cursor, length);
				if(fp.isDone()){
					fields.add(fp.toField());
					fp=new SimpleFieldPather();
				}
				if(in != null){
					length=in.length;
					cursor=0;
				}else{
					cursor = length = 0;
				}
			}
		}
		if(cursor==length)return null;
		return Arrays.copyOfRange(in,cursor,length);
	}

	@Override
	public boolean isDone() {
		return communHeader!=null && fields.size()==communHeader.fieldCount;
	}
}
