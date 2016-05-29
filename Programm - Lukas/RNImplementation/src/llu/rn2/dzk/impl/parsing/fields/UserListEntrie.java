package llu.rn2.dzk.impl.parsing.fields;

import llu.rn2.dzk.interfaces.OutgoingConnection;

public class UserListEntrie {
	public final byte[] ip;
	public final int port;
	public final static int size=8;
	
	public UserListEntrie(byte[] ip,int i){
		this.ip=ip;
		this.port=i;
	}
	
	public long hash(){
		return -1;//(((long)ip<<(Integer.BYTES*8))|((long)port));
	}
/*	@Override
	public String toString() {
		String toret="U"+Long.toString(())+"_";		
		return toret;
	}*/
}
