package irc.interfaces;


public interface IWorker extends Runnable{
	
	public void terminate();
	
	public Thread getThread();
	
	public boolean isAlive();

}
