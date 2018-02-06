/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Scanner;

import java.util.concurrent.TimeUnit;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;		//no clue
	private int[] receivedMessages;		//tracking what is received/lost
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
		close = false;

		try{
			pacData = new byte[1000];
			recvSoc.setSoTimeout(5000); //{TO-DO: Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever}
			pac = new DatagramPacket(pacData,pacData.length);			
			while(!close){
				try{
					TimeUnit.MILLISECONDS.sleep(100);
					recvSoc.receive(pac);		//{TO-DO: Receive the message}
					processMessage(new String(pacData)); // {TO-DO: Receive the messages and process them by calling processMessage(...).
				}
				catch(SocketTimeoutException e){
					System.out.println("TimeOut: " + e.getMessage());
					close = true;
				}

			}
			int num_missing=0;
			for(int i=0; i<receivedMessages.length; i++){
				if(receivedMessages[i] == 0){
					System.out.println("Missing message number " + (i+1));
					num_missing = num_missing +1;
				}										    
			}
			System.out.println("Missed " + num_missing + " messages");
		}
		catch (Exception e) {System.out.println("IO: " + e.getMessage());}

	}

	public void processMessage(String data) {

		try{
			System.out.println(data.trim());
			MessageInfo msg = new MessageInfo(data); // {TO-DO: Use the data to construct a new MessageInfo object}

			if(totalMessages == -1 ){ // {TO-DO: On receipt of first message, initialise the receive buffer}
				receivedMessages = new int[msg.totalMessages];
				totalMessages = msg.totalMessages;
			}
			receivedMessages[msg.messageNum-1] = 1;	//  {TO-DO: Log receipt of the message}
		/*
			if(msg.messageNum == msg.totalMessages){ //{TO-DO: If this is the last expected message, then identify any missing messages}
				close = true;
			}*/
		}
		catch (Exception e){System.out.println("Exception: " + e.getMessage());}
	

	}


	public UDPServer(int rp) {
		try{
			recvSoc = new DatagramSocket(rp); // {TO-DO: Initialise UDP socket for receiving data}, port cannot be <1024	
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());}
		// Done Initialisation

		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);
		UDPServer server = new UDPServer(recvPort); 
		server.run();
		// TO-DO: Construct Server object and start it by calling run().
	}

}
