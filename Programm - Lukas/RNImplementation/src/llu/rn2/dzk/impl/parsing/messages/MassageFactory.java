package llu.rn2.dzk.impl.parsing.messages;

import java.util.ArrayList;
import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.fields.IpField;
import llu.rn2.dzk.impl.parsing.fields.PortField;
import llu.rn2.dzk.impl.parsing.fields.TextField;
import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;
import llu.rn2.dzk.impl.parsing.fields.UserListField;

public class MassageFactory {
	static byte version = 0x01;

	private MassageFactory(){}

	public static final byte TYPE_LOGIN  		= 0x01;
	public static final byte TYPE_LOGOUT 		= 0x02;
	public static final byte TYPE_MYNAME 		= 0x03;
	public static final byte TYPE_TEXTMESSAGE 	= 0x04;
	
	public static Massage newMassage(CommunHeader header,Collection<Field> fields){
		switch(header.type){
		case TYPE_LOGIN:
			return new LoginMassage(header,fields);
		case TYPE_LOGOUT:
			return new LogoutMassage(header,fields);
		case TYPE_MYNAME:
			return new NameMassage(header,fields);
		case TYPE_TEXTMESSAGE:
			return new TextMassage(header,fields);
		default:
			return null;
		}
	}
	public static LoginMassage createLogin(byte[] myIP,int port,String name){
		
		CommunHeader ch= new CommunHeader(version, MassageFactory.TYPE_LOGIN, (short)0, myIP, port,(short)2);
		IpField ipf=new IpField(myIP);
		PortField pf = new PortField(port);
		Collection<Field> fc=new ArrayList<>(2);
		fc.add(ipf);
		fc.add(pf);
		//TODO: name wird nicht ermöglicht
		return new LoginMassage(ch, fc);
	}
	public static Massage createLogout(byte[] ip, short port) {
		CommunHeader ch= new CommunHeader(version, MassageFactory.TYPE_LOGOUT, (short)0, ip, port,(short)2);
		IpField ipf=new IpField(ip);
		PortField pf = new PortField(port);
		Collection<Field> fc=new ArrayList<>(2);
		fc.add(ipf);
		fc.add(pf);
		//TODO: name wird nicht ermöglicht
		return new LogoutMassage(ch, fc);
	}
	public static Massage createTextMassage(byte[] myIP,short port,Collection<UserListEntrie> to, String text) {
		CommunHeader ch   = new CommunHeader(version, MassageFactory.TYPE_TEXTMESSAGE, (short)0, myIP, port,(short)2);
		TextField tm      = new TextField(text);
		UserListField ulf = new UserListField(to);
		
		Collection<Field> fc=new ArrayList<>(2);
		fc.add(tm);
		fc.add(ulf);
		
		return new TextMassage(ch, fc);
	}
	
}
