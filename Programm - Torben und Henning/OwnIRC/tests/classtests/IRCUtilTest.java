package classtests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import irc.classes.IRCUtils;

public class IRCUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void checkNicknameTest() {
		String testnick1 = "Hellowwww";
		String testnick2 = "3";
		String testnick3 = null;
		assertTrue(IRCUtils.checkNickname(testnick1));
		assertFalse(IRCUtils.checkNickname(testnick2));
		assertFalse(IRCUtils.checkNickname(testnick3));
	}
	
	@Test
	public void checkIpTest() {
		String ip1 = "0.0.0.0";
		String ip2 = "256.0.0.0";
		String ip3 = "256.256.0.0";
		String ip4 = "256.256.256.0";
		String ip5 = "256.256.256.256";
		String ip6 = "Hallo";
		String ip7 = null;
		String ip8 = "";
		String ip9 = "255.255.255.255";
		String ip10 = "-255.255.255.255";
		String ip11 = "255.-255.255.255";
		String ip12 = "255.255.-255.255";
		String ip13 = "255.255.255.-255";
		String ip14 = "255.255.255.255.255";
		String ip15 = "255.255.255.255255.255.255.255";
		String ip16 = "255.255.255";
		
		assertTrue(IRCUtils.checkIP(ip1));
		assertTrue(IRCUtils.checkIP(ip9));

		assertFalse(IRCUtils.checkIP(ip2));
		assertFalse(IRCUtils.checkIP(ip3));
		assertFalse(IRCUtils.checkIP(ip4));
		assertFalse(IRCUtils.checkIP(ip5));
		assertFalse(IRCUtils.checkIP(ip6));
		assertFalse(IRCUtils.checkIP(ip7));
		assertFalse(IRCUtils.checkIP(ip8));
		assertFalse(IRCUtils.checkIP(ip10));
		assertFalse(IRCUtils.checkIP(ip11));
		assertFalse(IRCUtils.checkIP(ip12));
		assertFalse(IRCUtils.checkIP(ip13));
		assertFalse(IRCUtils.checkIP(ip14));
		assertFalse(IRCUtils.checkIP(ip15));
		assertFalse(IRCUtils.checkIP(ip16));
	}

}
