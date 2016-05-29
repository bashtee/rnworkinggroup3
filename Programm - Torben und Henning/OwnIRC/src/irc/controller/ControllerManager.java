package irc.controller;

import irc.interfaces.IClientController;
import irc.interfaces.IServerController;

public final class ControllerManager {
	private ControllerManager(){}
	
	/**
	 * @author mader
	 * Function to initialize a new ServerController
	 * @param port Port on which the Server should run
	 * @return new Instance of a IServerController
	 * */
	public static IServerController createServerController(int port){
		return ServerController.valueOf(port);
	}
	
	/**
	 * @author mader
	 * Function to initialize a new ClientController
	 * @return new Instance of a IClientController
	 * */
	public static IClientController createClientController(){
		return ClientController.valueOf();
	}
}
