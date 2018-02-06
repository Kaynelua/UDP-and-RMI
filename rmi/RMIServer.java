/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.*;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer

		if (totalMessages == -1){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];		
		}

			receivedMessages[msg.messageNum] = 1;
			System.out.println ("Message " + msg.messageNum  );
			
			if(msg.messageNum == msg.totalMessages-1) {
				int numReceived = 0;
				for(int i=0; i <msg.totalMessages ;i++){
					if(receivedMessages[i] == 1){
						numReceived = numReceived +1;
					}
					
					else{
						System.out.println ("Message " + i + " was not received" );
					}
				}
				System.out.println ("Total number of messages received = " + numReceived + " out of " + msg.totalMessages );	
		
		
		}

		// TO-DO: Log receipt of the message

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

	}


	public static void main(String[] args) {

		RMIServer rmis = null;
		if (System.getSecurityManager() == null) {
			System.setSecurityManager (new RMISecurityManager());
		}
		try {
		
			rmis = new RMIServer();
			String URL = "RMIServer";
			rebindServer(URL,rmis);
		} catch(Exception e) { System.out.println ("Exception : " + e.getMessage()); }

		// TO-DO: Initialise Security Manager

		// TO-DO: Instantiate the server class

		// TO-DO: Bind to RMI registry

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {System.out.println ("Exception : " + e.getMessage());}

		try {		
			Naming.rebind(serverURL, server);

		} catch (Exception e) {System.out.println ("Exception : " + e.getMessage());}
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}
