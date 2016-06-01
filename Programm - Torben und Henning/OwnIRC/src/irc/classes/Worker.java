package irc.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.enums.MessageType;
import irc.interfaces.IServerController;
import irc.interfaces.ISimpleChat;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleContentUserList;
import irc.interfaces.ISimpleDataInput;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;
import irc.interfaces.IWorker;

final class Worker implements IWorker{
	
	private Queue<ISimpleDataInput> _pendingMessages;
	private Lock _lock;
	private Condition _cond;
	private IServerController _serverCtrl;
	private volatile boolean _terminate = false;
	private Thread _mySoul = null;
	
	private Worker(Queue<ISimpleDataInput> pendingMessages, Lock lock, Condition cond, IServerController serverCtrl){
		this._pendingMessages = pendingMessages;
		this._lock = lock;
		this._cond = cond;
		this._serverCtrl = serverCtrl;
	}
	
	static IWorker valueOf(Queue<ISimpleDataInput> pendingMessages,Lock lock, Condition cond,IServerController serverCtrl){
		return new Worker(pendingMessages,lock,cond,serverCtrl);
	}

	@Override
	public void run() {
		_mySoul = Thread.currentThread();
		System.out.println("Worker nr: " +_mySoul.getId()+ " ready");
		mainwhile:while(!_terminate){
			byte[] m = new byte[0];
			_lock.lock();
			while(_pendingMessages.isEmpty()){
				try {
					System.out.println("Sleepy");
					_cond.await();
				} catch (InterruptedException e) {
					terminate();
					break mainwhile;
				}
			}
			ISimpleDataInput s = _pendingMessages.poll();
			_lock.unlock();
			
			if(s.isValid()){
				m = s.readBytes();
				System.out.println("Was Valid!");
			} else {
				s.closeSocket();
				continue mainwhile;
			}
			if(m.length> IRCUtils.HEADER_BYTE_SIZE-1){
				byte[] headerbytes = ArrayUtils.subarray(m, 0, IRCUtils.HEADER_BYTE_SIZE);
				ISimpleHeader header = Values.createNewHeader(headerbytes);
				List<ISimpleContent> contentList = IRCUtils.decodeContent(ArrayUtils.subarray(m, IRCUtils.HEADER_BYTE_SIZE, m.length),header.getNumberOfFields());
				if(contentList== null){
					insertIntoPending(s);
					continue mainwhile;
				} else {
					s.removeBytes(IRCUtils.HEADER_BYTE_SIZE-1+ IRCUtils.getUsedBytes(contentList));
				}
				ISimpleMessage simMess = Values.createNewMessage(header,null, contentList);				
				
				
				if(simMess.getMessageType() == MessageType.TEXT_MESSAGE){
					List<ISimpleUser> uList = new ArrayList<>();

					ISimpleContent con = IRCUtils.contentsOfContents(contentList, FieldType.USER_LIST);
					if(con != null){
						uList = ((ISimpleContentUserList)con).getUserList();
					}
					if(!uList.isEmpty()){
						ISimpleChat acChat = null;
						List<ISimpleChat> chatList = _serverCtrl.getChatlist();
						synchronized (chatList) {
							chatlist:for(ISimpleChat chat : chatList){
								if(chat.isChatFor(uList)){
									acChat = chat;
									break chatlist;
								}
							}
						//Loop end
							if(acChat == null){
								acChat = Values.createNewChat(uList);
								chatList.add(acChat);
							}	
						}
						_serverCtrl.addMessage(acChat,simMess);
						
					}
				} else if(simMess.getMessageType() == MessageType.LOGOUT) {
					boolean done = false;
					ISimpleContent con1 = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.IP);
					ISimpleContent con2 = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.PORT);
					ISimpleUser user = Values.createNewUser(con1.contentAsString(), Integer.parseInt(con2.contentAsString()));
					done  = _serverCtrl.removeUser(user);
					if(done){
						ISimpleHeader messHeader = Values.createNewHeader(_serverCtrl.getME(), MessageType.LOGOUT, IRCUtils.VERSION);
						ISimpleMessage message = Values.createNewMessage(messHeader, _serverCtrl.getAllUser(), simMess.getWholeMessage());
						_serverCtrl.addMessage(message);
					}
				} else if(simMess.getMessageType() == MessageType.MY_NAME) {
					ISimpleContent con = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.NAME);
					ISimpleUser user = simMess.getSender();
					if(con != null)
					user.setNickname(con.contentAsString());
				} else if(simMess.getMessageType() == MessageType.LOGIN) {
					ISimpleContent con1 = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.IP);
					ISimpleContent con2 = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.PORT);
					ISimpleContent con3 = IRCUtils.contentsOfContents(simMess.getWholeMessage(), FieldType.NAME);
					ISimpleUser user = Values.createNewUser(con1.contentAsString(), Integer.parseInt(con2.contentAsString()));
					if(con3 != null){
						user.setNickname(con3.contentAsString());
					}
					
					
					boolean added = _serverCtrl.addUser(user);
					
					if(added){
						ISimpleHeader messHeader = Values.createNewHeader(_serverCtrl.getME(), MessageType.LOGIN, IRCUtils.VERSION);
						ISimpleMessage message = Values.createNewMessage(messHeader, _serverCtrl.getAllUser(), simMess.getWholeMessage());
						_serverCtrl.addMessage(message);
					} else if(user.equals(_serverCtrl.getME())) {
						_serverCtrl.addUser(simMess.getSender());
					}
				}
			}
			insertIntoPending(s);
		}

		System.out.println("Worker: Here ends it");
	}

	@Override
	public void terminate() {
		this._terminate= true;
	}
	
	private void insertIntoPending(ISimpleDataInput d){
		_lock.lock();
		_pendingMessages.offer(d);
		_lock.unlock();
	}

	@Override
	public Thread getThread() {
		return _mySoul;
	}

	@Override
	public boolean isAlive() {
		return _mySoul.isAlive();
	}

}
