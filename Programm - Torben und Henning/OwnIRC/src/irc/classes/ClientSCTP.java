package irc.classes;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import com.sun.nio.sctp.SctpChannel;
import irc.interfaces.ISimpleClient;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import com.sun.nio.*;

final class ClientSCTP implements ISimpleClient {

	//Variable to terminate the client
	private volatile boolean _terminated = false;
	private ReentrantLock _lock;
	private Condition _cond;

	private Queue<ISimpleMessage> _messages;

	//Constructor
	private ClientSCTP(Queue<ISimpleMessage> massagePool, ReentrantLock lock, Condition cond){
		this._messages = massagePool;
		this._lock = lock;
		this._cond = cond;
	}
	
	/**
	 * @author mader
	 * Factory Method
	 * @return new Instance of this class
	 * */
	static ISimpleClient valueOf(Queue<ISimpleMessage> massagePool,ReentrantLock lock, Condition cond){
		return new ClientSCTP(massagePool,lock,cond);
	}

	@Override
	public void run() {
		mainwhile:
		while (!_terminated) {
			ISimpleMessage m;
			_lock.lock();
			while (_messages.isEmpty()) {
				try {
					System.out.println("Client: Sleeping <,<!");
					_cond.await();
				} catch (InterruptedException e) {
					terminate();
					continue mainwhile;
				}
			}
			m = _messages.poll();
			_lock.unlock();

			List<ISimpleUser> users = m.getRecipients();
			for (ISimpleUser user : users) {

					// Muss noch an korrekter Stelle eingeordnet werden.
					clSocket.getOutputStream().write(m.messageToBytes());

				try {
					System.out.println("Client: I will try to connect!");
					System.out.println(user.getIP() + ":" + user.getPort());
					InetSocketAddress serverAddr = new InetSocketAddress(user.getIP(), user.getPort());
					ByteBuffer buf = ByteBuffer.allocateDirect(60);
					Charset charset = Charset.forName("ASCII");
					CharsetDecoder decoder = charset.newDecoder();

					SctpChannel sctp = SctpChannel.open(serverAddr, 0, 0);

					// handler to keep track of association setup and termination
					AssociationHandler assocHandler = new AssociationHandler();

					// expect two messages and two notifications
					MessageInfo messageInfo = null;
					do {
						messageInfo = sctp.receive(buf, System.out, (NotificationHandler<PrintStream>) assocHandler);
						buf.flip();

						if (buf.remaining() > 0) {
							System.out.println("" + decoder.decode(buf).toString());
						}

						buf.clear();
					} while (messageInfo != null);

					sctp.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Client: Goodbye lov'ly world...");
	}

	@Override
	public void terminate() {
		this._terminated = true;
	}

	static class AssociationHandler	extends AbstractNotificationHandler<printstream> {
		public HandlerResult handleNotification(AssociationChangeNotification not, PrintStream stream) {
			if (not.event().equals(COMM_UP)) {
				int outbound = not.association().maxOutboundStreams();
				int inbound = not.association().maxInboundStreams();
				stream.printf("New association setup with %d outbound streams" + ", and %d inbound streams.\n", outbound, inbound);
			}

			return HandlerResult.CONTINUE;
		}

		public HandlerResult handleNotification(ShutdownNotification not, PrintStream stream) {
			stream.printf("The association has been shutdown.\n");
			return HandlerResult.RETURN;
		}
	}
}
