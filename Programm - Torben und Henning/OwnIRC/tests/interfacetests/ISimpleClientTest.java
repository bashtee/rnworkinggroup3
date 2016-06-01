package interfacetests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import irc.classes.Values;
import irc.controller.ControllerManager;
import irc.enums.MessageType;
import irc.interfaces.IServerController;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

public class ISimpleClientTest {

	@Test
	public void test() {
		IServerController serc = ControllerManager.createServerController("127.0.0.1",1337);
		serc.startServer();
		
		
		ISimpleUser user1 = Values.createNewUser("192.168.137.1", 1337);
		ISimpleUser user2 = Values.createNewUser("192.168.137.33", 1337);
		ISimpleHeader header1 = Values.createNewHeader(user2, MessageType.LOGIN, 0);
		ISimpleContent con1 = Values.createNewContentIP("192.168.137.33");
		ISimpleContent con2 = Values.createNewContentPort(1337);
		List<ISimpleUser> userlist = new ArrayList<>(1);
		List<ISimpleContent> contentList = new ArrayList<>(2);
		
		contentList.add(con1);
		contentList.add(con2);
		
		userlist.add(user1);
		
		ISimpleMessage message1 = Values.createNewMessage(header1, userlist, contentList);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serc.addMessage(message1);
		
		
		try {
			TimeUnit.SECONDS.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
