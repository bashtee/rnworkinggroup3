package irc.controller;

import irc.interfaces.IClientController;
import irc.interfaces.IServerController;
import irc.view.GuiController;

public final class ControllerManager {
	private ControllerManager(){}
	
	/**
	 * @author mader
	 * Function to initialize a new ServerController
	 * @param port Port on which the Server should run
	 * @return new Instance of a IServerController
	 * */
	public static IServerController createServerController(String ip, int port,GuiController gCtrl){
		return ServerController.valueOf(ip,port,gCtrl);
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
