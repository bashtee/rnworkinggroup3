package llu.rn2.dzk.impl.parsing.messages;

public abstract class Massage {
	public abstract byte[] toByteArray();
	public abstract CommunHeader getHeader();
}
