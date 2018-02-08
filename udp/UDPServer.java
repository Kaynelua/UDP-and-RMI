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
	private int totalMessages = -1;		
	private int[] receivedMessages;		
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
		close = false;
		try{
			pacData = new byte[1000];
			recvSoc.setSoTimeout(5000);
			pac = new DatagramPacket(pacData,pacData.length);			
			while(!close){
				try{
					recvSoc.receive(pac);		
					processMessage(new String(pacData));
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
			MessageInfo msg = new MessageInfo(data); 

			if(totalMessages == -1 ){
				receivedMessages = new int[msg.totalMessages]; //initialise array length of totalMessages
				totalMessages = msg.totalMessages;
			}
			receivedMessages[msg.messageNum-1] = 1;	

		}
		catch (Exception e){System.out.println("Exception: " + e.getMessage());}
	}


	public UDPServer(int rp) {
		try{
			recvSoc = new DatagramSocket(rp); //Port cannot be <1024	
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
		//Construct Server object and start it by calling run().
	}

}
