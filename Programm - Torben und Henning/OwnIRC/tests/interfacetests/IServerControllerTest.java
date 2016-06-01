package interfacetests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import irc.controller.ControllerManager;
import irc.interfaces.IServerController;

public class IServerControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void runnTest() {
		IServerController serv = ControllerManager.createServerController("127.0.0.1",1337);
		serv.startServer();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serv.stopServer();
		try {
			serv.getThread().join(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(serv.getThread().isAlive()){
			fail("Server didn't stop!");
		}
	}

}
