package llu.rn2.dzk.impl.parsing.messages;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.fields.IpField;
import llu.rn2.dzk.impl.parsing.fields.PortField;

public class LogoutMassage extends SimpleMassage {

	private IpField ip=null;
	private PortField port=null;
	
	public LogoutMassage(CommunHeader header, Collection<Field> fields) {
		super(header, fields);
		for(Field f : fields){
			if(ip==null && (f instanceof IpField)){
				ip=(IpField) f;
			}else if(port==null && (f instanceof PortField)){
				port=(PortField) f;
			}
			if(ip!=null && port != null){
				break;
			}
		}
	}
	
	public byte[] getIp(){
		return ip.value();
	}
	
	public int getPort(){
		return port.value();
	}
}
