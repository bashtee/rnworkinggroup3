package irc.classes;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import irc.interfaces.ISimpleClient;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

final class Client implements ISimpleClient {
	
	//Variable to terminate the client
	private volatile boolean _terminated = false;
	private ReentrantLock _lock;
	private Condition _cond;
	
	private Queue<ISimpleMessage> _messages;
	
	//Constructor
	private Client(Queue<ISimpleMessage> massagePool,ReentrantLock lock, Condition cond){
		this._messages = massagePool;
		this._lock = lock;
		this._cond = cond;
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleClient valueOf(Queue<ISimpleMessage> massagePool,ReentrantLock lock, Condition cond){
		return new Client(massagePool,lock,cond);
	}

	@Override
	public void run() {
		mainwhile:while(!_terminated){
				ISimpleMessage m;
				_lock.lock();
					while(_messages.isEmpty()){
						try {
							System.out.println("Client: Sleeping <,<!");
							_cond.await();
						} catch (InterruptedException e) {
							terminate();
							continue mainwhile;
						}
					}
					m = _messages.poll();
				_lock.unlock();
				
				List<ISimpleUser> users = m.getRecipients();
				for(ISimpleUser user: users){
					try {
						System.out.println("Client: I will try to connect!");
						System.out.println(user.getIP()+":"+user.getPort());
						Socket clSocket = new Socket(user.getIP(),user.getPort());
						clSocket.getOutputStream().write(m.messageToBytes());
						
						clSocket.close();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}			
		}
	System.out.println("Client: Goodbye lov'ly world...");
	}

	@Override
	public void terminate() {
		this._terminated = true;
	}
}
