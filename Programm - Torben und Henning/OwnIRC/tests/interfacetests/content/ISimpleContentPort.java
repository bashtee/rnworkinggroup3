package interfacetests.content;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import irc.classes.Values;
import irc.interfaces.ISimpleContent;

public class ISimpleContentPort {

	@Test
	public void test() {
		int port1 = 1337;
		int port2 = 12345;
		ISimpleContent con1 = Values.createNewContentPort(port1);
		ISimpleContent con2 = Values.createNewContentPort(port2);

		byte[] con1AsByte = new byte[]{0x0,0x2,0x0,0x2,0x5,0x39};
		byte[] con2AsByte = new byte[]{0x0,0x2,0x0,0x2,0x30,0x39};
		byte[] sad = con1.contentToBytes();
		assertTrue(Arrays.equals(con1.contentToBytes(),con1AsByte));
		assertTrue(Arrays.equals(con2.contentToBytes(),con2AsByte));
		
		ISimpleContent con3 = Values.createNewContent(con1AsByte);
		ISimpleContent con4 = Values.createNewContent(con2AsByte);

		assertEquals(con1.contentAsString(),con3.contentAsString());
		assertEquals(con2.contentAsString(),con4.contentAsString());

	}

}
