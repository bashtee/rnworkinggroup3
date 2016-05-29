package llu.rn2.dzk.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import llu.rn2.dzk.impl.threads.SimpleDataReciver;
import llu.rn2.dzk.impl.threads.SimpleDataSender;
import llu.rn2.dzk.impl.threads.SimpleIncomingConectionManager;
import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;
import llu.rn2.dzk.impl.threads.SimpleDataProcessor;
import llu.rn2.dzk.interfaces.DataProzessor;
import llu.rn2.dzk.interfaces.IncomingConectionManager;
import llu.rn2.dzk.interfaces.OutgoingConectionManager;

public class UIControler implements Runnable {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		int port=-1;
		String ip;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("IP adress to bind to:");
			ip=bufferRead.readLine();
			System.out.print("Use Port :");
			port=Integer.parseInt(bufferRead.readLine());
			
			InetAddress address = Inet4Address.getByName(ip);
			serverSocket = new ServerSocket(port,16,address );
			System.out.println("listening on : "+ip+":"+port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: "+port);
			System.exit(1);
		}

		IncomingConectionManager icm = new SimpleIncomingConectionManager(serverSocket);
		OutgoingConectionManager ocm = new SimpleOutgoingConectionManager();

		SimpleDataSender ds = new SimpleDataSender();

		SimpleDataReciver dr = new SimpleDataReciver();
		dr.setConnectionCollection(icm.getAllConections());

		SimpleDataProcessor pc = new SimpleDataProcessor(icm, dr, ocm, ds);

		UIControler ui = new UIControler(pc);

		Thread icmT = new Thread(icm);

		Thread dsT = new Thread(ds);

		Thread drT = new Thread(dr);

		Thread pcT = new Thread(pc);

		icmT.start();
		dsT.start();
		drT.start();
		pcT.start();
		ui.run();
	}

	DataProzessor pc;

	private UIControler(DataProzessor pc) {
		this.pc = pc;
	}

	@Override
	public void run() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.print('>');
				String line = bufferRead.readLine();
				process(line);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void process(String line) {
		if(line==null) return;
		String[] task = line.split("\\s+", 2);
		try {
			switch (task[0]) {
			case "login":
				task = task[1].split("\\s+", 3);

				String myName = null;
				if (task.length == 3) {
					myName = task[2];
				}
				pc.login(task[0], Short.decode(task[1]), myName);
				break;
			case "logout":
				pc.logout();
				break;
			case "myname":
				break;
			case "show":
				while(pc.hasMassage()){
					TMassage m = pc.getMassage();
					System.out.println(m.toString());
				}
				System.out.println("keine weiteren Nachrichten");
				break;
			case "send":
				task = task[1].split("\\s+", 3);
				
				Collection<UserListEntrie> ulc=new ArrayList<>();
				UserListEntrie ule=pc.getUser(task[0], Short.decode(task[1]));
				ulc.add(ule);
				boolean failed=(ule==null);
				for(;!failed && !task[2].startsWith("/");task = task[2].split("\\s+", 3)){
					ule=pc.getUser(task[0], Short.decode(task[1]));
					ulc.add(ule);
					failed=(ule==null);
				}
				
				if(failed){
					System.out.println("Unknown user!");
				}else{
					pc.sendMassage(ulc, task[2]);
				}
				break;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block	
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		}
	}

}
