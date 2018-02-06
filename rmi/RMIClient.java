/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		try{

		RMIServerI iRMIServer = null;

			// Check arguments for Server host and number of messages
			if (args.length < 2){
				System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
				System.exit(-1);
			}

			String urlServer = new String("rmi://" + args[0] + "/RMIServer");
			int numMessages = Integer.parseInt(args[1]);

			if(System.getSecurityManager() == null){
				System.setSecurityManager(new RMISecurityManager());	// {TO-DO: Initialise Security Manager}
			}

			iRMIServer = (RMIServerI) Naming.lookup(urlServer); // TO-DO: Bind to RMIServer

			for(int i =0; i < numMessages; i++){  // TO-DO: Attempt to send messages the specified number of times
				iRMIServer.receiveMessage(new MessageInfo(numMessages,i+1)); 
			}

			

		}
		catch(Exception e){
			System.out.println("Trouble: " + e);
		}

	}
}
