package llu.rn2.dzk.impl.parsing.messages;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.fields.TextField;
import llu.rn2.dzk.impl.parsing.fields.UserListEntrie;
import llu.rn2.dzk.impl.parsing.fields.UserListField;

public class TextMassage extends SimpleMassage {
	
	TextField text=null;
	UserListField userList=null;
	
	
	public TextMassage(CommunHeader header, Collection<Field> fields) {
		super(header, fields);
		for(Field f : fields){
			if(text==null && (f instanceof TextField)){
				text=(TextField) f;
			}else if(userList==null && (f instanceof UserListField)){
				userList=(UserListField) f;
			}
			if(text!=null && userList != null){
				break;
			}
		}
	}
	
	public String getText(){
		
		return text.value();
	}
	
	public Collection<UserListEntrie> getUserList(){
		return userList.value();
	}
}
