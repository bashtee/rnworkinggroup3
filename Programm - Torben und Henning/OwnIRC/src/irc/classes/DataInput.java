package irc.classes;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import irc.interfaces.ISimpleDataInput;

final class DataInput implements ISimpleDataInput {
	private Socket _s;
	private long _time;
	private byte[] _readedBytes;
	
	private DataInput(Socket s){
		this._s = s;
		this._time = System.nanoTime();
		this._readedBytes = new byte[0];
	}
	
	static ISimpleDataInput valueOf(Socket s){
		return new DataInput(s);
	}

	@Override
	public byte[] readBytes() {
		byte[] read = new byte[0];
		try {
			read = IOUtils.toByteArray(_s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(_readedBytes.length<1){
			_readedBytes = read;
		} else {
			_readedBytes = IRCUtils.addAll(_readedBytes,read);
		}
		return _readedBytes;
	}

	@Override
	public long getTimestamp() {
		return this._time;
	}

	@Override
	public void setTimestamp() {
		this._time = System.nanoTime();
	}

	@Override
	public void removeBytes(int removeInclude) {
		if(removeInclude >= _readedBytes.length-1){
			this._readedBytes = new byte[0];
		} else
			this._readedBytes = ArrayUtils.subarray(_readedBytes, 0, removeInclude+1);
	}

	@Override
	public void closeSocket() {
		try {
			_s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public boolean isValid() {
		try {
			if(_s.getInputStream().available()>0) {
				setTimestamp();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		long diff =  System.nanoTime()-_time;
		if(diff <= IRCUtils.TIMEOUT) {
			return true;
			}
		return false;
	}
	
	
	
	
	

}
