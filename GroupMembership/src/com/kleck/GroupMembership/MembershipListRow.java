package com.kleck.GroupMembership;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;


//1 entry in a MembershipList
class MembershipListRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String processId;
	private String ipAddress;
	private int portNumber;
	private int hbCounter;
	private long timeStamp;
	private boolean isDeletable;
	private HashMap<String, String> mr;

	public MembershipListRow(String processId, int portNumber) {
		this.processId = processId;
		this.setPortNumber(portNumber);
		this.hbCounter = 0;
		this.isDeletable = false;
		setIpAddress();
		setTimeStamp();
		mr = new HashMap<String, String>();
		updateHashMap();
	}
	
	//mutations
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getHbCounter() {
		return hbCounter;
	}

	public void setHbCounter(int hbCounter) {
		this.hbCounter = hbCounter;
	}
	
	public void incrementHbCounter() {
		this.hbCounter += 1;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}	
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress() {
		try {
			this.ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Could not get host address");
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getHashMap() {
		return this.mr;
	}
	
	public void updateHashMap() {
		this.mr.put("processId", this.processId);
		this.mr.put("ipAddress", this.ipAddress);
		this.mr.put("hbCounter", Integer.toString(this.hbCounter));
		this.mr.put("timeStamp", Long.toString(this.hbCounter));
		this.mr.put("isDeletable", Boolean.toString(this.isDeletable));
	}
	
	public String toString() {
		return this.processId + " - " + this.portNumber + " - " + this.hbCounter + " - " + this.timeStamp + " - " + this.isDeletable;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
}
