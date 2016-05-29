package llu.rn2.dzk.impl.parsing.fields;

import java.util.ArrayList;
import java.util.Collection;

public class UserListField extends Field {

	public UserListField(byte[] header, byte[] data) {
		super(header, data);
	}

	private static byte[] userList2Byte(Collection<UserListEntrie> ul) {
		byte[] toret = new byte[ul.size() * 8];
		int cursor = 0;
		for (UserListEntrie ule : ul) {
			byte[] ip = ule.ip;
			byte[] port = toByteArray(ule.port);
			toret[cursor  ] = ip[0];
			toret[cursor+1] = ip[1];
			toret[cursor+2] = ip[2];
			toret[cursor+3] = ip[3];

			toret[cursor+6] = port[0];
			toret[cursor+7] = port[1];
			cursor++;
		}
		return toret;
	}

	public UserListField(Collection<UserListEntrie> to) {
		super(FieldFactory.TYPE_USERLIST, (short) (8 * to.size()), userList2Byte(to));
	}

	public Collection<UserListEntrie> value() {
		Collection<UserListEntrie> toret = new ArrayList<>(this.datalength);

		for (int offset = 0; offset < this.datalength; offset+=UserListEntrie.size) {
			byte[] ip = new byte[4];
			ip[0]=data[0];
			ip[1]=data[1];
			ip[2]=data[2];
			ip[3]=data[3];
			int port = (short) (((short) data[offset + 6]) << 8 | ((short) data[offset + 7]));
			toret.add(new UserListEntrie(ip, port));
		}
		return toret;

	}
}
