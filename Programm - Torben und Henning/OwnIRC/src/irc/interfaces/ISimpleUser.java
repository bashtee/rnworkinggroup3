package irc.interfaces;

public interface ISimpleUser {
	
	
	/**
	 * @author mader
	 * Function to get the IP of the user
	 * @return IP as String
	 * */
	public String getIP();
	
	/**
	 * @author mader
	 * Function to get the port of the user
	 * @return Port of the user as int
	 * */
	public int getPort();
	
	/**
	 * @author mader
	 * Function to get the nickname of the user
	 * @return Nickname as String
	 * */
	public String getNickname();
	
	/**
	 * @author mader
	 * Function to set the nickname of the user if the nick is Valid
	 * @param nickname New nickname for the user
	 * */
	public void setNickname(String nickname);
}
