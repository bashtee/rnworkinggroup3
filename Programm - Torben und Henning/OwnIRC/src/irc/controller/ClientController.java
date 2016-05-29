package irc.controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import irc.classes.Values;
import irc.interfaces.IClientController;
import irc.interfaces.ISimpleClient;
import irc.interfaces.ISimpleMessage;

final class ClientController implements IClientController {
	private ISimpleClient _client;
	private Thread _threadClient;
	private Queue<ISimpleMessage> _messagePool;
	private ReentrantLock _lock = new ReentrantLock();
	private Condition _cond = _lock.newCondition();
	
	private ClientController(){
		this._messagePool = new LinkedList<>();
		this._client = Values.createNewClient(_messagePool,_lock,_cond);
		this._threadClient = new Thread(_client);
	}
	
	static IClientController valueOf(){
		return new ClientController();
	}

	@Override
	public void startClient() {
		_threadClient.start();
	}

	@Override
	public void stopClient() {
		_client.terminate();
		_threadClient.interrupt();
	}

	@Override
	public Queue<ISimpleMessage> getMessageQueue() {
		return this._messagePool;
	}

	@Override
	public ReentrantLock getLock() {
		return this._lock;
	}

	@Override
	public Condition getLockCondition() {
		return this._cond;
	}

	@Override
	public void addMessage(ISimpleMessage message) {
		this._lock.lock();
		this._messagePool.offer(message);
		this._cond.signal();
		this._lock.unlock();
	}
	
}
