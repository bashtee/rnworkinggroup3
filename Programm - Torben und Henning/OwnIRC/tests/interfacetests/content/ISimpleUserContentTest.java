package interfacetests.content;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import irc.classes.Values;
import irc.interfaces.ISimpleContentUserList;
import irc.interfaces.ISimpleUser;

public class ISimpleUserContentTest {

	@Test
	public void test() {
		List<ISimpleUser> userList = new ArrayList<>(2);
		userList.add(Values.createNewUser("127.0.0.1", 1337));
		userList.add(Values.createNewUser("127.0.0.1", 1338));
		byte[] userListAsByteList = new byte[]{0x0,0x3,0x0,0x10,0x7f,0x0,0x0,0x1,0x0,0x0,0x5,0x39,0x7f,0x0,0x0,0x1,0x0,0x0,0x5,0x3A};
		
		ISimpleContentUserList con1 =(ISimpleContentUserList) Values.createNewContentUserList(userList);
		byte[] sad = con1.contentToBytes();
		assertTrue(Arrays.equals(userListAsByteList, con1.contentToBytes()));
		ISimpleContentUserList con2 =(ISimpleContentUserList) Values.createNewContent(userListAsByteList);
		
		
		
		assertTrue(con1.equals(con2));
		
	}

}
