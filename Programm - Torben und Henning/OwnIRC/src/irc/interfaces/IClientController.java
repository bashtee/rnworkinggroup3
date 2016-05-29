package irc.interfaces;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public interface IClientController {
	
	public void startClient();
	
	public void stopClient();
	
	public Queue<ISimpleMessage> getMessageQueue();
	
	public ReentrantLock getLock();
	
	public Condition getLockCondition();
	
	public void addMessage(ISimpleMessage message);
}
