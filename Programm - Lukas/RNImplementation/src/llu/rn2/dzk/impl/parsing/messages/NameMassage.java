package llu.rn2.dzk.impl.parsing.messages;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.fields.Field;
import llu.rn2.dzk.impl.parsing.fields.NameField;

public class NameMassage extends SimpleMassage {

	private NameField name = null;
	
	public NameMassage(CommunHeader header, Collection<Field> fields) {
		super(header, fields);
		for(Field f : fields){
			if(f instanceof NameField){
				name=(NameField) f;
				break;
			}
		}
	}
	
	public String getName(){
		return name.value();
	}
}
