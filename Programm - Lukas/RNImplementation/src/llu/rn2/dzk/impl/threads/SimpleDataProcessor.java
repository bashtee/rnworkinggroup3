package llu.rn2.dzk.impl.threads;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.TMassage;
import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;
import llu.rn2.dzk.impl.parsing.messages.CommunHeader;
import llu.rn2.dzk.impl.parsing.messages.LoginMassage;
import llu.rn2.dzk.impl.parsing.messages.LogoutMassage;
import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.impl.parsing.messages.MassageFactory;
import llu.rn2.dzk.impl.parsing.messages.NameMassage;
import llu.rn2.dzk.impl.parsing.messages.TextMassage;
import llu.rn2.dzk.interfaces.DataProzessor;
import llu.rn2.dzk.interfaces.DataReciver;
import llu.rn2.dzk.interfaces.DataSender;
import llu.rn2.dzk.interfaces.IncomingConection;
import llu.rn2.dzk.interfaces.IncomingConectionManager;
import llu.rn2.dzk.interfaces.OutgoingConectionManager;
import llu.rn2.dzk.interfaces.OutgoingConnection;

public class SimpleDataProcessor implements DataProzessor {
	
	private IncomingConectionManager icm;
	private OutgoingConectionManager ocm;
	private DataReciver              dr;
	private DataSender               ds;
	
	private BlockingQueue<TMassage> toGui;
	
	public SimpleDataProcessor(IncomingConectionManager incommingManager,DataReciver dr, OutgoingConectionManager outgoingManager, SimpleDataSender ds){
		setIcm(incommingManager);
		setOcm(outgoingManager);
		setDr(dr);
		setDs(ds);
		toGui=new LinkedBlockingQueue<TMassage>();
	}
	

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
	
	@Override
	public boolean login(byte[] ip,int port,String myName){
		if(null!=ocm.contains(ip, port))return false;
		
		OutgoingConnection con = ocm.createConnection(ip, port, null);
		ocm.removeConnection(ip, port);
		Massage m = MassageFactory.createLogin(icm.getIp(), icm.getPort(), myName);
		ds.send(con,m);
		return true;
	}
	
	@Override
	public boolean logout(){
		if(ocm.getAllConections().isEmpty())return false;
		
		Massage m = MassageFactory.createLogout(icm.getIp(), icm.getPort());
		
		Collection<? extends OutgoingConnection> allConns=ocm.getAllConections();
		for(OutgoingConnection con:allConns){
			if(con.isClean()){
				ds.send(con,m);
			}
		}	
		return true;
	}
	@Override
	public void sendMassage(Collection<UserListEntrie> to,String text){
		Massage m = MassageFactory.createTextMassage(icm.getIp(), icm.getPort(),to,text);
		
		for(UserListEntrie t:to){
			OutgoingConnection oc = ocm.contains(t.ip, t.port);
			if(oc!=null){
				ds.send(oc, m);
			}else{
				
			}
		}
	}
	
	void Handle(LoginMassage m){
		if(Arrays.equals(m.getIp(),icm.getIp()) && m.getPort() == icm.getPort()){
			//es ist eine antwort auf meine anmeldung
			CommunHeader header = m.getHeader();
			if(null==ocm.contains(header.getIP(), header.serderPort)){
				ocm.createConnection(header.getIP(), header.serderPort, null);
			}else{
				return;
			}
		}else{
			if(null!=ocm.contains(m.getIp(), m.getPort())){
				return; //wir haben den client bereits in unserere liste
			}else{
				ocm.createConnection(m.getIp(), m.getPort(),m.getName());
				m.updateHeader(icm.getIp(), icm.getPort());
				for(OutgoingConnection oc:ocm.getAllConections()){
					ds.send(oc,m);
				}
			}
		}
	}
	
	void Handle(LogoutMassage m){
		if(ocm.removeConnection(m.getIp(), m.getPort())){
			m.updateHeader(icm.getIp(), icm.getPort());
			for(OutgoingConnection oc:ocm.getAllConections()){
				ds.send(oc,m);
			}	
		}
	}
	
	void Handle(TextMassage m){
		TMassage tm = new TMassage(m);
		toGui.offer(tm);
	}
	
	void Handle(NameMassage m){
		CommunHeader header=m.getHeader();
		OutgoingConnection con;
		if(null!=(con=ocm.contains(header.getIP(), header.serderPort))){
			con.setTargetName(m.getName());
		}
	}
	
	void Handle(Massage m){
		if(m instanceof LoginMassage){
			Handle((LoginMassage) m);
		}else if (m instanceof LogoutMassage){
			Handle((LogoutMassage) m);
		}else if (m instanceof TextMassage){
			Handle((TextMassage) m);
		}else if (m instanceof NameMassage){
			Handle((NameMassage) m);
		}else{
			System.out.println("unknown massage");
		}
	}
	
	@Override
	public void run() {
		while(true){
			Massage m = dr.getMassage();
			Handle(m);
		}
	}


	public void setIcm(IncomingConectionManager icm) {
		this.icm = icm;
	}


	public void setOcm(OutgoingConectionManager ocm) {
		this.ocm = ocm;
	}
	public void setDr(DataReciver dr) {
		this.dr = dr;
	}
	public void setDs(DataSender ds) {
		this.ds = ds;
	}


	@Override
	public boolean hasMassage() {
		return !toGui.isEmpty();
	}


	@Override
	public TMassage getMassage() throws InterruptedException {
		// TODO Auto-generated method stub
		return toGui.take();
	}


	@Override
	public void login(String string, Short port, String myName) throws UnknownHostException {
		InetAddress adress= Inet4Address.getByName(string);
		login(adress.getAddress(), port, myName);
	}


	@Override
	public UserListEntrie getUser(byte[] ip, int port) {
		OutgoingConnection oc=ocm.contains(ip, port);
		if(oc==null) return null;
		return new UserListEntrie(oc.getTargetIp(), oc.getTargetPort());
	}


	@Override
	public UserListEntrie getUser(String string, int port) throws UnknownHostException {
		InetAddress adress= Inet4Address.getByName(string);
		return getUser(adress.getAddress(),port);
		
	}
}
