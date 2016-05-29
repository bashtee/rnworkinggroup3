package irc.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import irc.classes.IRCUtils;
import irc.classes.Values;
import irc.interfaces.IClientController;
import irc.interfaces.IServerController;
import irc.interfaces.ISimpleChat;
import irc.interfaces.ISimpleDataInput;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleServer;
import irc.interfaces.ISimpleUser;
import irc.interfaces.IWorker;

final class ServerController implements IServerController{
	private ISimpleServer _server;
	private Thread _serverThread;
	private Queue<ISimpleDataInput> _pendingMessages;
	private List<ISimpleUser> _userList;
	private boolean _running;
	private ReentrantLock _lock = new ReentrantLock();
	private Condition _cond = _lock.newCondition();
	private List<IWorker> _workerList;
	private List<ISimpleChat> _chatList;
	private IClientController _clientCtrl;
	
	private ServerController(int port){
		this._clientCtrl = ControllerManager.createClientController();
		this._pendingMessages = new LinkedList<>();
		this._chatList = new ArrayList<>();
		this._userList = new ArrayList<>();
		_workerList = new ArrayList<>(IRCUtils.NUMBER_OF_WORKER);
		for(int i =0; i< IRCUtils.NUMBER_OF_WORKER;i++){
			IWorker worker = Values.createNewWorker(_pendingMessages,_clientCtrl, _chatList, _userList, _lock,_cond);
			_workerList.add(worker);
			new Thread(worker).start();
		}
		this._server = Values.createNewServer(port,_pendingMessages, _lock,_cond);
		this._serverThread = new Thread(_server);
		this._running = false;
		
	}
	
	static ServerController valueOf(int port){
		return new ServerController(port);
	}

	@Override
	public void startServer() {
		if(!_running){
			_serverThread.start();
			_clientCtrl.startClient();
		}
		_running = true;
	}

	@Override
	public void stopServer() {
		_server.terminate();
		_clientCtrl.stopClient();
		_workerList.forEach(elem -> {
			elem.terminate();
			elem.getThread().interrupt();
		});
		_running = false;
	}

	@Override
	public Thread getThread() {
		return this._serverThread;
	}

	@Override
	public Queue<ISimpleDataInput> getMessageQueue() {
		return this._pendingMessages;
	}

	@Override
	public void addMessage(ISimpleMessage message) {
		_clientCtrl.addMessage(message);
	}
}
