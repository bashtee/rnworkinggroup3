package irc.interfaces;

import java.util.Queue;

public interface IServerController {
	
	public void startServer();
	
	public void stopServer();
	
	public Thread getThread();
	
	public Queue<ISimpleDataInput> getMessageQueue();
	
	public void addMessage(ISimpleMessage message);

}
