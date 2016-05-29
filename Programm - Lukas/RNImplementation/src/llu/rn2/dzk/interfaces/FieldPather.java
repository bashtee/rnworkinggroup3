package llu.rn2.dzk.interfaces;

import llu.rn2.dzk.impl.parsing.fields.Field;

public interface FieldPather {
	Field toField();
	byte[] parse(byte[] in,int offset,int length);
	boolean isDone();
}
