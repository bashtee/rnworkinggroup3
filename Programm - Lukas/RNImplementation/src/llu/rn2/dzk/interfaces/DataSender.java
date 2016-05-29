package llu.rn2.dzk.interfaces;

import java.util.Collection;

import llu.rn2.dzk.impl.parsing.messages.Massage;

public interface DataSender extends ControllableRunnable {

	void send(OutgoingConnection con, Massage m);
	
	
}
