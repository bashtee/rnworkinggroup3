package irc.classes;

import java.util.LinkedList;
import java.util.List;

import irc.interfaces.ISimpleChat;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

final class Chat implements ISimpleChat {
	
	//Userlist of this chat
	private List<ISimpleUser> _userList;
	
	//List of Messages (want to save them maybe)
	private List<ISimpleMessage> _protokoll;
	
	private int _readIndex = 0;

	//Constructor
	private Chat(List<ISimpleUser> userList){
		this._userList = userList;
		this._protokoll = new LinkedList<>();
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleChat valueOf(List<ISimpleUser> userList){
		return new Chat(userList);
	}

	@Override
	public List<ISimpleUser> getUserList() {
		return this._userList;
	}

	@Override
	public List<ISimpleMessage> getAllMessages() {
		return this._protokoll;
	}

	@Override
	public void addUserToChannel(ISimpleUser user) {
		this._userList.add(user);
	}

	@Override
	public void addMessage(ISimpleMessage message) {
		assert(message == null);
		this._protokoll.add(message);
	}

	@Override
	public void removeUserFromChannel(ISimpleUser user) {
		assert(user == null);
		this._userList.remove(user);
	}

	@Override
	public int getReadIndex() {
		return _readIndex;
	}

	@Override
	public int countMessages() {
		return this._protokoll.size();
	}

	@Override
	public ISimpleMessage getMessage(int index) {
		return this._protokoll.get(index);
	}

	@Override
	public void setReadIndex(int countMessages) {
		this._readIndex = countMessages;		
	}

	@Override
	public boolean isChatFor(List<ISimpleUser> uList) {
		//More than one chat isn't possible TODO
		return true;
	}
}
