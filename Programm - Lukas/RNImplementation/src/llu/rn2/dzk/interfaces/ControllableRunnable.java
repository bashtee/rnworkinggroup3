package llu.rn2.dzk.interfaces;

public interface ControllableRunnable extends Runnable{
	void pauseThread();
	void resumeThread();
	void exitThread();
}
