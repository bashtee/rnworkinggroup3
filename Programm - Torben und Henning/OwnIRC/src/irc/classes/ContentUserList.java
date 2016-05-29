package irc.classes;

import java.util.List;

import irc.enums.FieldType;
import irc.interfaces.ISimpleContentUserList;
import irc.interfaces.ISimpleUser;

final class ContentUserList implements ISimpleContentUserList{

	private List<ISimpleUser> _userList;
	
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
		//TODO
		return new ContentUserList(null);
	}

	@Override
	public byte[] contentToBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String contentAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FieldType getFieldType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ISimpleUser> getUserList() {
		// TODO Auto-generated method stub
		return null;
	}
}
