package llu.rn2.dzk.impl;


import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.interfaces.OutgoingConectionManager;
import llu.rn2.dzk.interfaces.OutgoingConnection;

public class SimpleOutgoingConectionManager implements OutgoingConectionManager {

	private Collection<OutgoingConnection> connectionList;
	
	public SimpleOutgoingConectionManager(){
		connectionList = new LinkedBlockingQueue<OutgoingConnection>();
	}
	
	
	@Override
	public Collection<? extends OutgoingConnection> getAllConections() {
		return connectionList;
	}

	@Override
	public OutgoingConnection createConnection(byte[] ip, int port,String name) {
		OutgoingConnection conn = new SimpleOutgoingConnection(ip,port,name);
		conn.setTargetName(name);
		connectionList.add(conn);
		return conn ;
	}

	@Override
	public boolean removeConnection(byte[] ip, int port) {
		for(OutgoingConnection oc:getAllConections()){
			if(Arrays.equals(oc.getTargetIp(),ip) && oc.getTargetPort()==port){
				getAllConections().remove(oc);
				return true;
			}
		}
		return false;
	}


	@Override
	public OutgoingConnection contains(byte[] ip, int port) {
		
		for(OutgoingConnection oc:getAllConections()){
			if(Arrays.equals(oc.getTargetIp(),ip) && oc.getTargetPort()==port)return oc;
		}
		return null;
	}

}
