package llu.rn2.dzk.impl;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.interfaces.OutgoingConnection;

public class SimpleOutgoingConnection implements OutgoingConnection{

	private LinkedBlockingQueue<Exception> exceptionQueue;
	
	private final byte[] ip;
	private final int port;
	private String name;
	

	
	public SimpleOutgoingConnection(byte[]  ip, int port, String name) {
		this.ip=ip;
		this.port=port;
		this.name=name;
	}

	@Override
	public boolean isClean(){
		return exceptionQueue.isEmpty();
	}
	

	@Override
	public Queue<Exception> getExceptionQueue() {
		return exceptionQueue;
	}

	
	@Override
	public byte[] getTargetIp() {
		return ip;
	}

	@Override
	public int getTargetPort() {
		return port;
	}

		@Override
	public String getTargetName() {
		return name;
	}

	@Override
	public void setTargetName(String name) {
		this.name=name;
	}

	
}
