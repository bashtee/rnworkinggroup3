package llu.rn2.dzk.impl.threads;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import llu.rn2.dzk.impl.parsing.SimpleMassageParser;
import llu.rn2.dzk.impl.parsing.messages.Massage;
import llu.rn2.dzk.interfaces.DataProzessor;
import llu.rn2.dzk.interfaces.DataReciver;
import llu.rn2.dzk.interfaces.IncomingConection;
import llu.rn2.dzk.interfaces.MassageParser;

public class SimpleDataReciver implements DataReciver {
	BlockingQueue<IncomingConection> incommingConnections;
	BlockingQueue<Massage> needToProzess;
	
	Map<? extends IncomingConection, MassageParser> inProgressMassages;

	
	public SimpleDataReciver() {
		inProgressMassages = new HashMap<>();
		needToProzess=new LinkedBlockingQueue<Massage>();
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
	public void run() {
		while (true) {
			IncomingConection conn;
			try {
				conn = incommingConnections.take();
				if (conn.isClean()){				
					try {
						int readable = conn.getRecivingSocket().getInputStream().available();
						if (readable != 0){
							byte[] byteArray = new byte[readable];
							int length = conn.getRecivingSocket().getInputStream().read(byteArray, 0, readable);
							conn.setMillis();
							do {
								MassageParser parser = inProgressMassages.getOrDefault(conn, new SimpleMassageParser());
			
								byteArray = parser.parse(byteArray, length);
								length = 0;
								if (parser.isDone()) {
									inProgressMassages.remove(conn);
									needToProzess.offer(parser.toMessage());
								}
								if (byteArray == null) {
									length = 0;
								} else {
									length = byteArray.length;
								}
							} while (length != 0);
						}
					} catch (IOException e) {
						conn.getExceptionQueue().add(e);
					}
					if(conn.timedOut()){
						incommingConnections.offer(conn);
					}else{
						conn.close();
					}
				}		
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void setConnectionCollection(BlockingQueue<IncomingConection> c) {
		this.incommingConnections = c;
	}

	@Override
	public Massage getMassage() {
		try {
			return needToProzess.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
