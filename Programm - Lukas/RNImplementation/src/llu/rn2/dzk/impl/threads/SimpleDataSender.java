package llu.rn2.dzk.impl.threads;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.interfaces.DataSender;
import llu.rn2.dzk.interfaces.OutgoingConnection;

public class SimpleDataSender implements DataSender {
	BlockingQueue<Msg> toSend=new LinkedBlockingQueue<Msg>();
	
	@Override
	public void pauseThread() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeThread() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitThread() {
		// TODO Auto-generated method stub

	}
	
	private byte[] int2bitArray(int i){
		return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
	}

	
	@Override
	public void run() {
		while(true){
			try {
				Msg m = toSend.take();
				byte[] ip= m.oConn.getTargetIp();
				int port=m.oConn.getTargetPort();
				
				try{
					Socket s = new Socket(Inet4Address.getByAddress(ip), port);
					s.getOutputStream().write(m.m.toByteArray());
					s.getOutputStream().flush();
					s.close();
				} catch (UnknownHostException e) {
					m.oConn.getExceptionQueue().offer(e);
				} catch (IOException e) {
					m.oConn.getExceptionQueue().offer(e);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void send(OutgoingConnection con, Massage m) {
		toSend.offer(new Msg(con,m));
	}
	
	private class Msg{
		public Msg(OutgoingConnection oConn, Massage m) {
			this.oConn=oConn;
			this.m=m;
		}
		public OutgoingConnection oConn;
		public Massage m;
	}

}
