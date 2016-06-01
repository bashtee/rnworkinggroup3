package apptest;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import irc.classes.Values;
import irc.controller.ControllerManager;
import irc.enums.MessageType;
import irc.interfaces.IClientController;
import irc.interfaces.IServerController;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

public class serverClientCommunicationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		IServerController serc = ControllerManager.createServerController("127.0.0.1",1337);
		IClientController clc = ControllerManager.createClientController();
		serc.startServer();
		clc.startClient();
		ReentrantLock lock = clc.getLock();
		Condition cond = clc.getLockCondition();
		String testmes = "Hoi, this is a little test for the Client-Server communication here. Don't mind me, I'm just sitting here, an I'm just playing dumb. Well you could do as you like at least xD";
		
		ISimpleUser user1 = Values.createNewUser("127.0.0.1", 1337);
		ISimpleHeader header1 = Values.createNewHeader(user1, MessageType.TEXT_MESSAGE, 0);
		ISimpleContent con1 = Values.createNewContentText(testmes);
		List<ISimpleUser> userlist = new ArrayList<>(1);
		List<ISimpleContent> contentList = new ArrayList<>(1);
		
		contentList.add(con1);
		userlist.add(user1);
		
		ISimpleMessage message1 = Values.createNewMessage(header1, userlist, contentList);
		Queue<ISimpleMessage> clmlist = clc.getMessageQueue();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.lock();
			//clmlist.add(message1);
			cond.signal();
		lock.unlock();
		try {
			TimeUnit.SECONDS.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serc.stopServer();
		clc.stopClient();	
	}

}
