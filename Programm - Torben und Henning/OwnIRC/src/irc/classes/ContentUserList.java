package irc.classes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContentUserList;
import irc.interfaces.ISimpleUser;

final class ContentUserList implements ISimpleContentUserList{

	private List<ISimpleUser> _userList;
	private FieldType _type = FieldType.USER_LIST;
	
	private ContentUserList(List<ISimpleUser> userList){
		this._userList = userList;
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContentUserList valueOf(List<ISimpleUser> userList){
		return new ContentUserList(userList);
	}
	
	/**
	 * Factory Method
	 * @author mader
	 * @return new Instance of this class
	 * */
	static ISimpleContentUserList valueOf(byte[] bytes,int length){
		int users = bytes.length / 8;
		List<ISimpleUser> userList = new ArrayList<>(users);
		int i = 0;
		while(i < users){
			String ip = "";
			int count = i*IRCUtils.USERLIST_SIZE;
			for(int k = count; k < count+IRCUtils.IP_LENGTH; k++){
				ip+= Integer.toString(IRCUtils.convertByteToInt(bytes[k]));
				if(k<count+IRCUtils.IP_LENGTH -1){
					ip +=".";
				}
			}
			//jumping over reserved bytes
			byte byte1 =bytes[count+IRCUtils.IP_LENGTH+IRCUtils.USERLIST_RESERVED_BYTES];
			byte byte2 =bytes[count+IRCUtils.IP_LENGTH+IRCUtils.USERLIST_RESERVED_BYTES+1]; 
			int port = IRCUtils.convertByteToInt(byte1,byte2 );
			userList.add(Values.createNewUser(ip, port));
			i++;
		}
		return new ContentUserList(userList);
	}

	@Override
	public byte[] contentToBytes() {
		List<byte[]> bytes = new ArrayList<>(getLength());
		byte[] filler = new byte[IRCUtils.USERLIST_RESERVED_BYTES];
		for(int i =0; i<IRCUtils.USERLIST_RESERVED_BYTES;i++){
			filler[i] = IRCUtils.ZERO;
		}
		bytes.add(IRCUtils.createFieldHeader(_type, getLength()));
		for(ISimpleUser user : _userList){
			byte[] ip = IRCUtils.ipToByte(user.getIP());
			byte[] port = ByteBuffer.allocate(Integer.BYTES).putInt(user.getPort()).array();
			port = ArrayUtils.subarray(port, 2, 4);
			bytes.add(ip);
			bytes.add(filler);
			bytes.add(port);
		}
		return IRCUtils.addAll(bytes);
	}

	@Override
	public String contentAsString() {
		return "Not really supported";
	}

	@Override
	public int getLength() {
		return this._userList.size()*IRCUtils.USERLIST_SIZE;
	}

	@Override
	public FieldType getFieldType() {
		return FieldType.USER_LIST;
	}

	@Override
	public List<ISimpleUser> getUserList() {
		return this._userList;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == this) return true;
		if(!(o instanceof ContentUserList)) return false;
		ContentUserList con = (ContentUserList) o;
		return con._userList.containsAll(this._userList) && this._userList.containsAll(con._userList);
	}
}
