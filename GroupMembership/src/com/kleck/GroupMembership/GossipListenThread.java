package com.kleck.GroupMembership;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class GossipListenThread extends Thread {
	private DatagramSocket server;
	private int portNumber;
	private GroupServer gs;
	
	
	public GossipListenThread(int portNumber, GroupServer groupServer) {
		this.portNumber = portNumber;
		this.gs = groupServer;
		try {
			this.server = new DatagramSocket(this.portNumber);
		} catch (BindException e) {
			System.out.println("Could not start server.  The port is in use.");
			System.exit(1);
		} catch (SocketException e) {
			System.out.println("Could not start server");
			e.printStackTrace();
		}
	}
	
	public void run() {
		byte[] receiveData = new byte[2048];
		while(true) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				server.receive(receivePacket);
				ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
				ObjectInput in = new ObjectInputStream(bis);
				MembershipList ml = null;
				Object temp = null;
				try {
					temp = in.readObject();
				} catch (ClassNotFoundException e) {
					System.out.println("Did not find Membership List object");
					e.printStackTrace();
				}
				ml = (MembershipList) temp;
				//System.out.println("RECEIVED: " +  ml.toString());

				//spin up a new thread to merge the two lists
				MergeMembershipListThread mmlt = new MergeMembershipListThread(ml, this.gs);
				//System.out.println(this.gs.getMembershipList().toString());
				mmlt.start();
				/*
	        	try {
					mmlt.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	System.out.println(this.gs.getMembershipList().toString());
				 */

				//if it's the first time the server


			}        	
			catch(EOFException e) {
				System.out.println("End of File Error\n");
				e.printStackTrace();
			}
			catch(IOException e) {
				System.out.println("I/O issue on the server side");
				e.printStackTrace();
			}        	
		}
	}	
}
