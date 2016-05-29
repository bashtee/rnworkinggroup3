package llu.rn2.dzk.interfaces;

import java.net.ServerSocket;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public interface IncomingConectionManager extends ControllableRunnable {
	
	
	/**
	 * a Threadsafe Collection of all ingoing Connections
	 * if ther is a new connection, the list gets automaticly updated
	 * */
	BlockingQueue<IncomingConection> getAllConections();
	
	/**
	 * param s is a established server socket
	 * */
	void setListenerSocket(ServerSocket s);
	
	
	byte[] getIp();
	
	short getPort();
}
