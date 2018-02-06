/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import rmi.RMIServerI;


import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);
		try {
			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null) {
				System.setSecurityManager (new RMISecurityManager());
			}
			// TO-DO: Bind to RMIServer
			RMIServerI s = (RMIServerI) Naming.lookup(urlServer);
			long startTime = System.currentTimeMillis();



			for(int i=0; i < numMessages ; i ++){
				MessageInfo m = new MessageInfo(numMessages,i);
				s.receiveMessage(m);
				
				//System.out.println("Message " + i + "sent");
			}

			long endTime = System.currentTimeMillis();

			System.out.println("That took " + (endTime - startTime) + " milliseconds");	

		}catch(RemoteException e) { System.out.println("Remote Exception: " + e.getMessage()); }
		catch(NotBoundException e) { System.out.println("NotBoundException: " + e.getMessage()); }
		catch (Exception e) { System.out.println("Exception " + e.getMessage()); }	
		
		// TO-DO: Attempt to send messages the specified number of times
		
	}
		
	
}
