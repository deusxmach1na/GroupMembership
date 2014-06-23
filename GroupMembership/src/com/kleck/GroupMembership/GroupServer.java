package com.kleck.GroupMembership;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class GroupServer {
	private boolean isContact;
	//private boolean run;
	private String processId;
	private String ipAddress;
	private MembershipList ml;

	private int portNumber;
	Properties props;

	public GroupServer(int portNumber, boolean isContact) {
		//start server
		this.portNumber = portNumber;
		this.isContact = isContact;
		this.ml = new MembershipList();
		this.props = loadParams();
		try {
			this.ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Could not get host address");
			e.printStackTrace();
		}
		this.processId = Long.toString(System.currentTimeMillis()) + "" + this.ipAddress; 
		
		//add yourself to your membership list
		ml.addNewMember(processId, this.portNumber);
		
		//start listening for gossip
		GossipListenThread glt = new GossipListenThread(this.portNumber, this);
		glt.start();
		
		//contact the contact server to add yourself
		if(!this.isContact) {
			String contactHostname = props.getProperty("contactserver").split(",")[0];
			int contactPortNumber = Integer.parseInt(props.getProperty("contactserver").split(",")[1]);
			GossipSendThread gst = new GossipSendThread(contactHostname, contactPortNumber, this.ml);
			gst.start();
		}
	}
	
	
	//main method
	//get args and spin up the Logging Server
	public static void main (String args[]) {
		int port = 6667;
		try {
			if(args.length >= 1) {
				port = Integer.parseInt(args[0]);
			}		
		}
		catch (NumberFormatException nfe) {
			port = 6667;
			System.out.println("Using default port " + port + " for gossip communication.");
		}
			
		
		//change port and server if args are passed
		if(args.length == 2) {
			if(!args[1].equals("false")) {
				System.out.println("Starting Contact Server");
				new GroupServer(port, true);
			}
			else {
				System.out.println("Starting Server.");
				new GroupServer(port, false);
			}		
		}
		else {
			System.out.println("Starting Server.");
			new GroupServer(port, false);
		}	
	}
	
	//open property file to get the hostName and portNumber
	public static Properties loadParams() {
	    Properties props = new Properties();
	    InputStream is = null;
	    
	    //load file
	    try {
	        File f = new File("settings.prop");
	        is = new FileInputStream(f);
	 
	        // Try loading properties from the file (if found)
	        props.load(is);
	        is.close();
	    }
	    catch (Exception e) { 
	    	System.out.println("Did not find hostname file. Ensure it is in the same folder as the jar.");
	    }
	    
	    return props;
	}
	
	//get set MembershipList
	public MembershipList getMembershipList() {
		return ml;
	}

	public void setMembershipList(MembershipList ml) {
		this.ml = ml;
	}
	
	public boolean isContact() {
		return this.isContact;
	}
	
	public String getProcessId() {
		return this.processId;
	}
}
