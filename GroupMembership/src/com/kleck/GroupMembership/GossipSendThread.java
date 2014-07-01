package com.kleck.GroupMembership;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GossipSendThread extends Thread{
	public String ipAddress;
	public int portNumber;
	public GroupServer gs;
	
	public GossipSendThread (String ipAddress, int portNumber, GroupServer gs) {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.gs = gs;
	}

	public void run() {
		try {
			//int bytesUsed = 0;
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(this.ipAddress);
			
			//prepare to send packet
			byte[] sendData = new byte[10000];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);   
			out.writeObject(this.gs.getMembershipList()); 
			sendData = bos.toByteArray();
			
			//send the packet to the appropriate place
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, this.portNumber);
			clientSocket.send(sendPacket);
			
			//measurements
			//bytesUsed = bos.size();
			//this.gs.updateBytesUsed(bytesUsed);
			//this.gs.updateRunTime();
			
			//clean up
			clientSocket.close();
			bos.close();
			out.close();
			
		}
		catch (IOException e) {
			System.out.println("I/O Exception with server.");
			e.printStackTrace();
		}
	}

}
