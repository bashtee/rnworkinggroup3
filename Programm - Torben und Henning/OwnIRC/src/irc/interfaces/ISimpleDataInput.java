package irc.interfaces;

public interface ISimpleDataInput {

	/**
	 * Function to read bytes from DataInput
	 * @author tin
	 * @return byte[] A byte[] with the readed bytes. The bytes won't be removed.
	 * */
	public byte[] readBytes();
	
	/**
	 * Function to remove bytes from the DataInput
	 * @author tin
	 * @param removeInclude Index till (including the given index) the bytes 'll be removed
	 * */
	public void removeBytes(int removeInclude);

	/**
	 * Function to get the Timestamp
	 * @author tin
	 * @return Timestamp of the DataInput
	 * */
	public long getTimestamp();
	
	/**
	 * function to set the Timestamp
	 * @author tin
	 * */
	public void setTimestamp();
	
	/**
	 * Function to close the connection
	 * @author tin
	 * */
	public void closeSocket();
	
	/**
	 * Function to control if the the datainput is valid
	 * @author tin
	 * @return true -> valid ; false -> not valid
	 * */
	public boolean isValid();
}
