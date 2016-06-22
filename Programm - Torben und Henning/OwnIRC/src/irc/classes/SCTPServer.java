package irc.classes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import irc.interfaces.ISimpleDataInput;
import irc.interfaces.ISimpleServer;
import com.sun.nio.sctp.*;

public class SCTPServer implements ISimpleServer{

	//Port for the application
	private final int _port;
	private volatile boolean _terminated = false;
	private Queue<ISimpleDataInput> _messages;
	private ReentrantLock _lock;
	private Condition _cond;
	
	private SCTPServer(int port, Queue<ISimpleDataInput> messages,ReentrantLock lock, Condition cond){
		this._port = port;
		this._messages = messages;
		this._lock = lock;
		this._cond = cond;
	}
	
	static ISimpleServer valueOf(int port, Queue<ISimpleDataInput> messages,ReentrantLock lock, Condition cond){
		return new SCTPServer(port,messages,lock,cond);
	}
	
	@Override
	public void run() {
		SctpServerChannel s = null;
		try {
			s = SctpServerChannel.open();
			InetSocketAddress serverAddr = new InetSocketAddress(_port);
			s.bind(serverAddr);
		} catch (IOException e) {
			terminate();
			e.printStackTrace();
		}
		while(!_terminated){
			try {
				SctpChannel sc = s.accept();
				_lock.lock();
					_messages.offer(Values.createNewDataInput(sc));
					_cond.signal();
				_lock.unlock();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void terminate() {
		this._terminated = true;
	}

	@Override
	public int getPort() {
		return _port;
	}

}
