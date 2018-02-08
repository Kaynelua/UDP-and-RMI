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
		if (totalMessages == -1){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];	//intialize receive buffer on first msg		
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
	}


	public static void main(String[] args) {

		RMIServer rmis = null;
		if (System.getSecurityManager() == null) {
			System.setSecurityManager (new RMISecurityManager()); //Initialise Security Manager
		}
		try {
		
			rmis = new RMIServer(); //Instantiate the server class
			String URL = "RMIServer";
			rebindServer(URL,rmis); //Bind to RMI registry
		} catch(Exception e) { System.out.println ("Exception : " + e.getMessage()); }


	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try {
			LocateRegistry.createRegistry(1099);	//Start Registry
		} catch (RemoteException e) {System.out.println ("Exception : " + e.getMessage());}
		try {		
			Naming.rebind(serverURL, server);
		} catch (Exception e) {System.out.println ("Exception : " + e.getMessage());}
	}
}
