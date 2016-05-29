package llu.rn2.dzk.interfaces;

import llu.rn2.dzk.impl.parsing.messages.Massage;

public interface MassageParser {
	Massage toMessage();
	byte[] parse(byte[] in,int length);
	boolean isDone();
}
