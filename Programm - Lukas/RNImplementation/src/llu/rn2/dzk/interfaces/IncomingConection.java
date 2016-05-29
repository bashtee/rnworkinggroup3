package llu.rn2.dzk.interfaces;

import java.net.Socket;
import java.util.Queue;

import llu.rn2.dzk.impl.parsing.messages.Massage;

public interface IncomingConection {

	void setRecivingSocket(Socket rs);
	
	Socket getRecivingSocket();
	
	boolean hasData();
	Massage recive()     throws InterruptedException;
	
	Queue<Exception> getExceptionQueue();
	boolean isClean();

	long lastMillis();
	void  setMillis();
	
	boolean timedOut();
	
	void close();
}
