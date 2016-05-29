package llu.rn2.dzk.impl.parsing.fields;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetDecoder;

public class TextField extends Field {

	public TextField(byte[] header, byte[] data) {
		super(header, data);
	}
	
	static byte[] s2b(String t){
		try {
			return t.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public TextField(String text) {
		super(FieldFactory.TYPE_TEXT,(short)s2b(text).length, s2b(text));
	}
	
	
	public String value() {
		try {
			return new String(data, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
