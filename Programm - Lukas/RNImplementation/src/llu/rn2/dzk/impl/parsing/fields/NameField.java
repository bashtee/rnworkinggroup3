package llu.rn2.dzk.impl.parsing.fields;

public class NameField extends Field {

	public NameField(byte[] header, byte[] data) {
		super(header, data);
	}

	String value=null;
	public String value() {
		if(null==value){
			value=new String(data, 0, datalength);
		}
		return value;
	}

}
