package apptest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import irc.classes.IRCUtils;
import irc.classes.Values;
import irc.enums.MessageType;
import irc.interfaces.*;

public class SCTPCommunikationTest {

	@Test
	public void test() {
		int port = 4567;
		Queue<ISimpleDataInput> pendingMessages = new LinkedList<>();
		ReentrantLock lock = new ReentrantLock();
		Condition cond = lock.newCondition();
		ISimpleServer serv = Values.createNewServer(port, pendingMessages, lock, cond,true);
		
		
		
		Queue<ISimpleMessage> massagePool = new LinkedList<>();
		ISimpleClient cl = Values.createNewClient(massagePool, lock, cond, true);
		
		ISimpleUser user = Values.createNewUser("127.0.0.1", port);
		List<ISimpleUser> userList = new ArrayList<>();
		userList.add(user);
		
		ISimpleContent userContent1 = Values.createNewContentIP(user.getIP());
		ISimpleContent userContent2 = Values.createNewContentPort(user.getPort());
		
		List<ISimpleContent> contentList = new ArrayList<>();
		contentList.add(userContent1);
		contentList.add(userContent2);
		
		ISimpleHeader header = Values.createNewHeader(user, MessageType.LOGIN, IRCUtils.VERSION);
		ISimpleMessage message = Values.createNewMessage(header, userList, contentList);
		
		massagePool.add(message);
		
		Thread clT = new Thread(cl);
		Thread servT = new Thread(serv);
		
		servT.start();
		//clT.start();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			ISimpleDataInput dada = pendingMessages.poll();

			if(dada != null)
				System.out.println(Arrays.toString(dada.readBytes()));
		}
	}

}
