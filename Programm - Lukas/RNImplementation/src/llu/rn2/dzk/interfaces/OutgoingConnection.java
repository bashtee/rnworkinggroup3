package llu.rn2.dzk.interfaces;

import java.net.Socket;
import java.util.Queue;

import llu.rn2.dzk.impl.parsing.messages.Massage;

public interface OutgoingConnection {

	byte[]    getTargetIp();
	int    getTargetPort();
	String getTargetName();
	void   setTargetName(String name);
	
	Queue<Exception> getExceptionQueue();
	boolean isClean();
}
