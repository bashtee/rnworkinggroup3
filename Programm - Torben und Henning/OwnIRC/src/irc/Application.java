package irc;

import irc.controller.ControllerManager;
import irc.interfaces.IServerController;

public class Application {
	public static void main(String args[]){
		IServerController servC = ControllerManager.createServerController(1337);
		servC.startServer();
		servC.stopServer();
	}
}
