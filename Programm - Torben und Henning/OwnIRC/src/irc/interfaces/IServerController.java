package irc.interfaces;

import java.util.List;
import java.util.Queue;

public interface IServerController {
	
	public void startServer();
	
	public void stopServer();
	
	public Thread getThread();
	
	public Queue<ISimpleDataInput> getMessageQueue();
	
	public void addMessage(ISimpleMessage message);
	
	public ISimpleUser getME();
	
	public void sendLogin(String ip, int port);
	
	public void sendText(ISimpleChat chat,String message);
	
	public void sendLogout();
	
	public void sendName(List<ISimpleUser> userList);

	public void stopAllWorker();
	
	public boolean addUser(ISimpleUser user);
	
	public boolean removeUser(ISimpleUser user);
	
	public List<ISimpleChat> getChatlist();
	
	public List<ISimpleUser> getAllUser();
	
	public void addMessage(ISimpleChat chat, ISimpleMessage message);
}
