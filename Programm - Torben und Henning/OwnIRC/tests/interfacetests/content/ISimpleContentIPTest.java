package interfacetests.content;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import irc.classes.Values;
import irc.interfaces.ISimpleContent;

public class ISimpleContentIPTest {

	@Test
	public void test() {
		String ip1 = "127.0.0.1";
		String ip2 = "100.100.100.100";
		ISimpleContent con1 = Values.createNewContentIP(ip1);
		ISimpleContent con2 = Values.createNewContentIP(ip2);
		byte[] con1AsByte = new byte[]{0x0,0x1,0x0,0x4,0x7f,0x0,0x0,0x1};
		byte[] con2AsByte = new byte[]{0x0,0x1,0x0,0x4,0x64,0x64,0x64,0x64};
		
		assertTrue(Arrays.equals(con1.contentToBytes(), con1AsByte));
		assertTrue(Arrays.equals(con2.contentToBytes(), con2AsByte));
		
		ISimpleContent con3 = Values.createNewContent(con1AsByte);
		ISimpleContent con4 = Values.createNewContent(con2AsByte);
		
		assertEquals(con1.contentAsString(),con3.contentAsString());
		assertEquals(con2.contentAsString(),con4.contentAsString());
		
	}

}
