package interfacetests.content;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

import irc.classes.IRCUtils;
import irc.classes.Values;
import irc.interfaces.ISimpleContent;

public class ISimpleContentName {

	@Test
	public void test() {
		String name1 = "EinName";
		String name2 = "ZweiName";
		ISimpleContent con1 = Values.createNewContentName(name1);
		ISimpleContent con2 = Values.createNewContentName(name2);
		
		byte[] con1AsByte = new byte[]{0x0,0x4,0x0,0x7};
		byte[] con2AsByte = new byte[]{0x0,0x4,0x0,0x8};
		
		try {
			con1AsByte = IRCUtils.addAll(con1AsByte,name1.getBytes(IRCUtils.ENCODING));
			con2AsByte = IRCUtils.addAll(con2AsByte,name2.getBytes(IRCUtils.ENCODING));
		} catch (UnsupportedEncodingException e) {
			fail("That was an exception");
			e.printStackTrace();
		}
		
		byte[] sad = con1.contentToBytes();

		assertTrue(Arrays.equals(con1AsByte,con1.contentToBytes()));
		assertTrue(Arrays.equals(con2AsByte,con2.contentToBytes()));
		
		ISimpleContent con3 = Values.createNewContent(con1AsByte);
		ISimpleContent con4 = Values.createNewContent(con2AsByte);

		assertEquals(con3.contentAsString(),con1.contentAsString());
		assertEquals(con4.contentAsString(),con2.contentAsString());
	}

}
