package llu.rn2.dzk.interfaces;

import java.net.UnknownHostException;
import java.util.Collection;

import llu.rn2.dzk.impl.TMassage;
import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;

public interface DataProzessor extends ControllableRunnable{

	boolean hasMassage();
	TMassage getMassage() throws InterruptedException;
	
	boolean login(byte[] ip,int port,String myName);
	
	boolean logout();
	
	void sendMassage(Collection<UserListEntrie> to,String text);
	
	void login(String string, Short decode, String myName) throws UnknownHostException;
	
	UserListEntrie getUser(byte[] ip, int port);
	UserListEntrie getUser(String string, int port) throws UnknownHostException;
}
