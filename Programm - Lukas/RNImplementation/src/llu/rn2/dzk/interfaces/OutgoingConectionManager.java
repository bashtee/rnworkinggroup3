package llu.rn2.dzk.interfaces;

import java.util.Collection;

public interface OutgoingConectionManager{
	
	
	/**
	 * a Threadsafe Collection of all ingoing Connections
	 * if ther is a new connection, the list gets automaticly updated
	 * */
	Collection<? extends OutgoingConnection> getAllConections();
	
	OutgoingConnection createConnection(byte[] ip,int port,String Name);
	
	boolean removeConnection(byte[] ip,int port);

	OutgoingConnection contains(byte[] ip, int port);
	
}
