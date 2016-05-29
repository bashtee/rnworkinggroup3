package llu.rn2.dzk.impl;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;
import llu.rn2.dzk.impl.parsing.messages.TextMassage;

public class TMassage {
	public TMassage(TextMassage m) {
		cc=m.getUserList();
		sender=new UserListEntrie(m.getHeader().getIP(),m.getHeader().serderPort);
		text=m.getText();
	}
	public final UserListEntrie sender;
	public final String text;
	public final Collection<UserListEntrie> cc; 
	
	@Override
	public String toString() {
		String toret=sender.toString() + " [";
		for(UserListEntrie e:cc){
			toret+=e.toString()+", ";
		}
		toret+="] : " + text;
		
		return toret;
	}
}
