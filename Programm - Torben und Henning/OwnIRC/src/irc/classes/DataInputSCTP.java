package irc.classes;

import irc.interfaces.ISimpleDataInput;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

import com.sun.nio.sctp.*;


public class DataInputSCTP implements ISimpleDataInput{

	private SctpChannel _chan;
	private long _time;
	private byte[] _readedBytes;
	private ByteBuffer _b = ByteBuffer.allocateDirect(IRCUtils.BYTEBUFFER_SIZE);
    
	private DataInputSCTP(SctpChannel s){
		this._chan = s;
		this._time = System.nanoTime();
		this._readedBytes = new byte[0];
	}
	
	static ISimpleDataInput valueOf(SctpChannel s){
		return new DataInputSCTP(s);
	}
	@Override
	public byte[] readBytes() {
		byte[] read = new byte[0];
		MessageInfo messageInfo = null;
        do {
        	if (_b.remaining() > 0){
            	byte[] data = new byte[_b.remaining()];
            	_b.get(data, 0, _b.remaining());
            	_readedBytes = IRCUtils.addAll(_readedBytes,data);
            	_b.clear();
            }
            try {
            	messageInfo = _chan.receive(_b, null,null);
            } catch (IOException e) {
				e.printStackTrace();
				return read;
			}
            if (_b.remaining() > 0){
            	byte[] data = new byte[_b.remaining()];
            	_b.get(data, 0, _b.remaining());
            	_readedBytes = IRCUtils.addAll(_readedBytes,data);
            }
            _b.clear();
        } while (messageInfo.bytes()>0);
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
			_chan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@Override
	public boolean isValid() {
		try {
			_chan.receive(_b, null, null);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if(_b.remaining() > 0) {
			setTimestamp();
			return true;
		}
		long diff =  System.nanoTime()-_time;
		if(diff <= IRCUtils.TIMEOUT) {
			return true;
			}
		return false;
	}
}
