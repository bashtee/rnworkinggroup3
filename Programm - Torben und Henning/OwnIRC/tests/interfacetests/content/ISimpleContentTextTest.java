package interfacetests.content;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

import irc.classes.IRCUtils;
import irc.classes.Values;
import irc.interfaces.ISimpleContent;

public class ISimpleContentTextTest {

	@Test
	public void test() {
		String message1 = "Hoi";
		String message2 = "Wie gehts es dir?  :D";
		
		
		ISimpleContent con1 = Values.createNewContentText(message1);
		ISimpleContent con2 = Values.createNewContentText(message2);
		
		byte[] con1AsByte = new byte[]{0x0,0x5,0x0,0x3};
		byte[] con2AsByte = new byte[]{0x0,0x5,0x0,0x15};
		
		try {
			con1AsByte = IRCUtils.addAll(con1AsByte,message1.getBytes(IRCUtils.ENCODING));
			con2AsByte = IRCUtils.addAll(con2AsByte,message2.getBytes(IRCUtils.ENCODING));
		} catch (UnsupportedEncodingException e) {
			fail("That was an exception");
			e.printStackTrace();
		}
		
		byte[] sad = con2.contentToBytes();
		
		assertTrue(Arrays.equals(con1AsByte,con1.contentToBytes()));
		assertTrue(Arrays.equals(con2AsByte,con2.contentToBytes()));
		
		ISimpleContent con3 = Values.createNewContent(con1AsByte);
		ISimpleContent con4 = Values.createNewContent(con2AsByte);

		assertEquals(con3.contentAsString(),con1.contentAsString());
		assertEquals(con4.contentAsString(),con2.contentAsString());
	}

}
