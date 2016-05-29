package llu.rn2.dzk.impl.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.SimpleIncommingConnection;
import llu.rn2.dzk.interfaces.IncomingConection;
import llu.rn2.dzk.interfaces.IncomingConectionManager;

public class SimpleIncomingConectionManager implements IncomingConectionManager {

	private BlockingQueue<IncomingConection> connectionList;
	
	private ServerSocket listenerSocket;
	
	private short port;
	private byte[] ip;
	
	public SimpleIncomingConectionManager(ServerSocket s) {
		this.connectionList=new LinkedBlockingQueue<IncomingConection>();
		setListenerSocket(s);
		
		this.port=(short)s.getLocalPort();
		this.ip=s.getInetAddress().getAddress();
	}
	
	@Override
	public void run() {
		while(true){
			try {
				SimpleIncommingConnection newConn = new SimpleIncommingConnection(listenerSocket.accept());
				connectionList.offer(newConn);
			} catch (IOException e) {
				System.err.println("Accept failed.");
			}
		}
	}

	@Override
	public BlockingQueue<IncomingConection> getAllConections() {
		return connectionList;
	}

	@Override
	public void setListenerSocket(ServerSocket s) {
		this.listenerSocket=s;
	}

	@Override
	public void pauseThread() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resumeThread() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void exitThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getIp() {
		return ip;
	}

	@Override
	public short getPort() {
		return port;
	}

}
