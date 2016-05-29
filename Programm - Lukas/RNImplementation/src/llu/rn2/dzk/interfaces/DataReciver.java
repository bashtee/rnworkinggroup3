package llu.rn2.dzk.interfaces;

import java.util.concurrent.BlockingQueue;

import llu.rn2.dzk.impl.parsing.messages.Massage;

public interface DataReciver extends ControllableRunnable {

	void setConnectionCollection(BlockingQueue<IncomingConection> c);
	
	Massage getMassage();
}
