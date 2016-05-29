package irc.classes;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import irc.enums.MessageType;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleUser;

final class Header implements ISimpleHeader {
	private int _version;
	private MessageType _type; 
	private ISimpleUser _sender;
	private int _numberFields;
	
	//Constructor
	private Header(int version,MessageType type, ISimpleUser sender, int numberFields){
		this._numberFields = numberFields;
		this._sender = sender;
		this._type = type;
		this._version = version;
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleHeader valueOf(int version,MessageType type, ISimpleUser sender) throws IllegalArgumentException{
		if(type == null || sender == null || version <0) throw new IllegalArgumentException("Bad Arguments");
		return new Header(version,type,sender,0);
	}
	
	static ISimpleHeader createFromBytes(byte[] bytes){
		
		int version = IRCUtils.convertByteToInt(bytes[0]);
		MessageType type = MessageType.valueOf(bytes[1]); 
		String ip = "";
		for(int i = IRCUtils.IP_POS; i < IRCUtils.IP_POS_END;i++){
			ip += Integer.toString(IRCUtils.convertByteToInt(bytes[i]));
			if(i != IRCUtils.IP_POS_END-1){
				ip+=".";
			}
		}
		int port = IRCUtils.convertByteToInt(bytes[8], bytes[9]);
		int numOfFields = IRCUtils.convertByteToInt(bytes[10], bytes[11]);
		ISimpleUser user = Values.createNewUser(ip, port); 
		ISimpleHeader header = valueOf(version,type,user);
		header.setNumberOfFields(numOfFields);
		return header;
	}

	@Override
	public int getVersion() {
		return _version;
	}

	@Override
	public byte[] toByteHeader() {
		byte[] header = new byte[IRCUtils.HEADER_BYTE_SIZE];
		byte[] sender = IRCUtils.ipToByte(_sender.getIP());
		byte[] ipAsByteArr = new byte[4];		
		byte[] port = ByteBuffer.allocate(4).putInt(_sender.getPort()).array();
		byte[] numFields = ByteBuffer.allocate(4).putInt(_numberFields).array();
		
		header[0]=(byte) _version;
		header[1]=_type.getType();
		header[2]=IRCUtils.ZERO;
		header[3]=IRCUtils.ZERO;
		for(int i = 0; i<sender.length;i++){
			header[i+IRCUtils.IP_POS] = sender[i];
		}
		header[8]=port[2];
		header[9]=port[3];
		header[10]=numFields[2];
		header[11]=numFields[3];
		
		return header;
	}

	@Override
	public int getSenderPort() {
		return _sender.getPort();
	}

	@Override
	public String getSenderIP() {
		
		return _sender.getIP();
	}

	@Override
	public MessageType getMessageType() {
		
		return _type;
	}

	@Override
	public int getNumberOfFields() {
		
		return _numberFields;
	}

	@Override
	public ISimpleUser getSender() {
		return _sender;
	}

	@Override
	public void setNumberOfFields(int numberOfFields) {
		if(numberOfFields>=0){
			this._numberFields = numberOfFields;
		} else {
			this._numberFields =0;
		}
	}

}
