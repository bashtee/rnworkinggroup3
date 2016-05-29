package irc.classes;

import java.util.ArrayList;
import java.util.List;

import irc.enums.MessageType;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

final class Message implements ISimpleMessage {
	
	private final ISimpleHeader _header;
	
	private List<ISimpleContent> _contentList;
	
	private final int NUM_OF_FIELDS;
	
	private final List<ISimpleUser> _recipients;
	
	//Constructor
	private Message(ISimpleHeader header,List<ISimpleUser> recipients ,List<ISimpleContent> contentList){
		this._contentList = contentList;
		this._header = header;
		this._recipients = recipients;
		this.NUM_OF_FIELDS = contentList.size();
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleMessage valueOf(ISimpleHeader header,List<ISimpleUser> recipients , List<ISimpleContent> contentList){
		return new Message(header,recipients,contentList);
	}

	@Override
	public ISimpleUser getSender() {
		return this._header.getSender();
	}

	@Override
	public List<ISimpleUser> getRecipients() {
		return this._recipients;
	}

	@Override
	public List<ISimpleContent> getWholeMessage() {
		return this._contentList;
	}

	@Override
	public ISimpleHeader getHeader() {
		return _header;
	}

	@Override
	public byte[] messageToBytes() {
		byte[] message = contentToBytes();
		
		_header.setNumberOfFields(NUM_OF_FIELDS);
		byte[] header = _header.toByteHeader();
		return IRCUtils.addAll(header,message);
	}
	
	private byte[] contentToBytes(){
		List<byte[]> content = new ArrayList<>(_contentList.size());
		_contentList.forEach(elem -> content.add(elem.contentToBytes()));
		return IRCUtils.addAll(content);
	}

	@Override
	public MessageType getMessageType() {
		return _header.getMessageType();
	}
}
