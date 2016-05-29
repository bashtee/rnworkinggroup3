package interfacetests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import irc.classes.Values;
import irc.enums.MessageType;
import irc.interfaces.ISimpleHeader;
import irc.interfaces.ISimpleUser;

public class ISimpleHeaderTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toByteHeadertest() {
		ISimpleUser user1 = Values.createNewUser("127.0.0.1", 1337);
		ISimpleUser user2 = Values.createNewUser("46.96.110.112", 4400);
		ISimpleHeader header1 = Values.createNewHeader(user1, MessageType.TEXT_MESSAGE, 0);
		header1.setNumberOfFields(50);
		ISimpleHeader header2 = Values.createNewHeader(user2, MessageType.LOGIN, 3);
		header2.setNumberOfFields(0);
		byte[] test1 = {0x00,0x03,0x00,0x00,0x7f,0x00,0x00,0x01,0x05,0x39,0x00,0x32};
		byte[] test2 = {0x03,0x01,0x00,0x00,0x2e,0x60,0x6e,0x70,0x11,0x30,0x00,0x01};
		byte[] result = header1.toByteHeader();
		assertTrue(Arrays.equals(result, test1));
		result = header2.toByteHeader();
		assertTrue(Arrays.equals(result, test2));
	}
	
	@Test
	public void createISimpleHeader(){
		ISimpleUser user1 = Values.createNewUser("127.0.0.1", 1337);
		ISimpleUser user2 = Values.createNewUser("46.96.110.112", 4400);
		//Allowed
		@SuppressWarnings("unused")
		ISimpleHeader header1 = Values.createNewHeader(user1, MessageType.TEXT_MESSAGE, 0);
		header1 = Values.createNewHeader(user2, MessageType.LOGIN, 3);
		
		//Not allowed
		exception.expect(IllegalArgumentException.class);
		header1 = Values.createNewHeader(null, MessageType.LOGIN, 0);
		exception.expect(IllegalArgumentException.class);
		header1 = Values.createNewHeader(user1, null, 1);
		exception.expect(IllegalArgumentException.class);
		header1 = Values.createNewHeader(user1, MessageType.LOGIN, -1);
	}

}
