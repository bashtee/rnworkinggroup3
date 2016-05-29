package llu.rn2.dzk.impl;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.interfaces.IncomingConection;

public class SimpleIncommingConnection implements IncomingConection{

	private Socket recivingSocket;
	
	private Queue<Massage> inBuffer;
	
	private static long timeout=10000; // ein timeout von 10 sekunden
	

	private LinkedBlockingQueue<Exception> exceptionQueue;

	private long millis;
	
	public SimpleIncommingConnection(Socket rs) {
		this.recivingSocket=rs;
		
		exceptionQueue=new LinkedBlockingQueue<>();
		inBuffer =new LinkedBlockingQueue<>();
	}
	
	@Override
	public boolean isClean(){
		return exceptionQueue.isEmpty()&&(!recivingSocket.isClosed());
	}
	
	@Override
	public boolean hasData() {
		return !inBuffer.isEmpty();
	}

	@Override
	public Massage recive() throws InterruptedException {
		return inBuffer.poll();
	}
	
	@Override
	public void setRecivingSocket(Socket rs) {
		this.recivingSocket=rs;
	}
	@Override
	public Socket getRecivingSocket() {
		return recivingSocket;
	}
	@Override
	public Queue<Exception> getExceptionQueue() {
		return exceptionQueue;
	}

	@Override
	public void close() {
		try {
			recivingSocket.close();
		} catch (Exception e) {
			exceptionQueue.add(e);
		}
	}

	@Override
	public long lastMillis() {
		return millis;
	}

	@Override
	public void setMillis() {
		this.millis=System.currentTimeMillis();
	}
	
	
	@Override
	public boolean timedOut(){
		return millis>System.currentTimeMillis()-SimpleIncommingConnection.timeout;
		
	}

	
}
