/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);

		// TO-DO: Construct UDP client class and try to send messages
		UDPClient myClient = new UDPClient();
		long startTime = System.currentTimeMillis();
		myClient.testLoop(serverAddr,recvPort,countTo);

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");	
	}

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try { sendSoc = new DatagramSocket(); // creates socket on any available port no.
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());}
		
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int tries = 1;
		while(tries <= countTo){
			// TO-DO: Send the messages to the server
			MessageInfo info =new MessageInfo(countTo,tries);
			send(info.toString(),serverAddr,recvPort);
			//System.out.println("Sending Message No. " + tries);
			tries ++;
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int	payloadSize = payload.length();
		byte[]	pktData = payload.getBytes();
		try{	
			DatagramPacket request = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
			sendSoc.send(request);
			//TimeUnit.MILLISECONDS.sleep(10);
			//System.out.println("Sent Message " + payload);
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());}
		catch (Exception e){System.out.println("IO: " + e.getMessage());}
		// TO-DO: build the datagram packet and send it to the server
	}
}
