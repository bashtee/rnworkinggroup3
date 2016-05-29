package irc.interfaces;

import java.util.List;

public interface IWorker extends Runnable{
	
	public void terminate();
	
	public Thread getThread();
	
	public boolean isAlive();

	public void setListOfChats(List<ISimpleChat> chatliste);
	
	public List<ISimpleChat> getListOfChats();

}
