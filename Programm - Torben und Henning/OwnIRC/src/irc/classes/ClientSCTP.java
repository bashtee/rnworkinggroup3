package irc.classes;

import com.sun.nio.sctp.*;
import irc.interfaces.ISimpleClient;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

final class ClientSCTP implements ISimpleClient {

	//Variable to terminate the client
	private volatile boolean _terminated = false;
	private ReentrantLock _lock;
	private Condition _cond;

	private Queue<ISimpleMessage> _messages;

	//Constructor
	private ClientSCTP(Queue<ISimpleMessage> massagePool, ReentrantLock lock, Condition cond){
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
		return new ClientSCTP(massagePool,lock,cond);
	}

	@Override
	public void run() {
		mainwhile:
		while (!_terminated) {
			ISimpleMessage m;
			_lock.lock();
			while (_messages.isEmpty()) {
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
			for (ISimpleUser user : users) {
				try {
					SctpServerChannel ssc = SctpServerChannel.open();
					InetSocketAddress serverAddr = new InetSocketAddress(user.getPort());
					ssc.bind(serverAddr);
					ByteBuffer buf = ByteBuffer.allocateDirect(60);

					while (true) {
						SctpChannel sc = ssc.accept();

						buf.put(m.messageToBytes());

						MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
						sc.send(buf, messageInfo);

						buf.clear();
						sc.close();
					}
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
