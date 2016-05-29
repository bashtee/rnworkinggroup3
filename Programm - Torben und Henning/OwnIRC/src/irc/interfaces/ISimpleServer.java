package irc.interfaces;

public interface ISimpleServer extends Runnable {
	
	/**
	 * @author mader
	 * Function to terminate the Thread
	 * */
	public void terminate();
	
	/**
	 * @author mader
	 * Function to get the actual port on which the server is listening
	 * @return int Port of the server
	 * */
	public int getPort();

}
