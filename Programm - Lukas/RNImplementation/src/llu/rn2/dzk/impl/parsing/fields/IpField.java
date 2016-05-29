package llu.rn2.dzk.impl.parsing.fields;

public class IpField extends Field {

	public IpField(byte[] header, byte[] data) {
		super(header, data);
	}
	
	public IpField(byte[] ip) {
		super(FieldFactory.TYPE_IP,(short)4,ip);
	}
	public byte[] value(){
		return data;
	}

}
