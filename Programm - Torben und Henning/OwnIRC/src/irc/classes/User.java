package irc.classes;

import irc.interfaces.ISimpleUser;

final class User implements ISimpleUser {
	
	private final String _ip;
	private final int _port;
	private String _nickname;
	
	//Constructor
	private User(String ip,int port){
		this._ip = ip;
		this._port = port;
		this._nickname = null;
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleUser valueOf(String ip,int port){
		assert(ip == null);
		return new User(ip,port);
	}

	@Override
	public String getIP() {
		return this._ip;
	}

	@Override
	public int getPort() {
		return this._port;
	}

	@Override
	public String getNickname() {
		return this._nickname;
	}

	@Override
	public void setNickname(String nickname) {
		if(IRCUtils.checkNickname(nickname))
			this._nickname = nickname;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(!(o instanceof User)) return false;
		User u = (User)o;
		return u.getPort() == this.getPort() && u.getIP().equals(this.getIP());
	}
	
}
