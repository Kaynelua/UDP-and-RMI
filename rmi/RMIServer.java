/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry; 
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.RMISecurityManager;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		if(totalMessages == -1){ 				// TO-DO: On receipt of first message, initialise the receive buffer
			receivedMessages = new int[msg.totalMessages];
			totalMessages = msg.totalMessages;
		}
		receivedMessages[msg.messageNum-1] = 1;	// TO-DO: Log receipt of the message
		System.out.println("Receieved msg " + msg.messageNum);

		if( msg.messageNum == totalMessages){// TO-DO: If this is the last expected message, then identify
			System.out.println("Receipt Done");
			for(int i=0;i<totalMessages;i++){
				if(receivedMessages[i] == 0){
					System.out.println("Missing msg " +  i+1);
				}
			}
		}
		

	}


	public static void main(String[] args) {

		RMIServer rmis = null;
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());	// {TO-DO: Initialise Security Manager}
		}
		try{
			rmis = new RMIServer();										// TO-DO: Instantiate the server class
			rebindServer("RMIServer", rmis);			// TO-DO: Bind to RMI registry
		}
		catch(Exception e){
			System.out.println("Trouble: " + e);
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try{
			// TO-DO:
			// Start / find the registry (hint use LocateRegistry.createRegistry(...)
			// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
			Registry r = LocateRegistry.createRegistry(1099);
			// TO-DO:
			// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
			// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
			// expects different things from the URL field.
			Naming.rebind(serverURL,server);
		}
		catch(Exception e){
			System.out.println("Trouble: " + e);
		}

	}
}
