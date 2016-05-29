package irc.classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import irc.interfaces.ISimpleDataInput;
import irc.interfaces.ISimpleServer;

final class Server implements ISimpleServer {
	
	//Port for the application
	private final int _port;
	private volatile boolean _terminated = false;
	private Queue<ISimpleDataInput> _messages;
	private ReentrantLock _lock;
	private Condition _cond;
	
		
	//Constructor
	private Server(int port,Queue<ISimpleDataInput> pendingMessages,ReentrantLock lock,Condition cond){
		this._port = port;
		this._messages = pendingMessages;
		this._lock = lock;
		this._cond = cond;
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleServer valueOf(int port,Queue<ISimpleDataInput> pendingMessages, ReentrantLock lock, Condition cond){
		return new Server(port,pendingMessages, lock, cond);
	}

	@Override
	public void run() {
		System.out.println("Server: Hoi I'm here");
		ServerSocket serverS = null;
		try {
			serverS = new ServerSocket(_port);
		} catch (IOException e) {
			terminate();
			e.printStackTrace();
		}
		while(!_terminated){
			try {
				System.out.println("Server: Wait for Action");
				Socket socket = serverS.accept();
					_lock.lock();
						_messages.offer(Values.createNewDataInput(socket));
						_cond.signal();
					_lock.unlock();
			} catch (IOException e) {
				terminate();
				e.printStackTrace();
			}
		}
		System.out.println("Server: Goodbye lovly word...");
	}

	@Override
	public void terminate() {
		this._terminated = true;
	}

	@Override
	public int getPort() {
		return this._port;
	}
	
	

}
