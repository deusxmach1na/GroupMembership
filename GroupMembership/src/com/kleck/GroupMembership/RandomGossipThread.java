package com.kleck.GroupMembership;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/*
 * needs to choose log(n) server(s) at random to gossip to
 * 
 * 
 */

public class RandomGossipThread extends Thread {
	private GroupServer gs;
	private int numGossips;
	
	public RandomGossipThread (GroupServer gs) {
		this.gs = gs;
		//this.numGossips = 2;
		
	}
	
	public void run() {
		//number of gossips = LOG2(N)
		this.numGossips = (int)(Math.log(gs.getMembershipList().size())/Math.log(2));
		//System.out.println(numGossips);
		Set<String> keys = this.gs.getMembershipList().getKeys();
		ArrayList<String> randomKeys = new ArrayList<String>();
		ArrayList<String> completedGossips = new ArrayList<String>();
		Random rand = new Random();
		
		//convert to arraylist for easier randomization
		for(String key: keys) {
			randomKeys.add(key);
		}
		
		while(completedGossips.size() < this.numGossips) {
			int n = rand.nextInt(keys.size());
			String randomId = randomKeys.get(n);
			if(!completedGossips.contains(randomId) && !randomId.equals(this.gs.getProcessId())) {
				String ipAddress = this.gs.getMembershipList().getMember(randomId).getIpAddress();
				int portNumber = this.gs.getMembershipList().getMember(randomId).getPortNumber();
				GossipSendThread gst = new GossipSendThread(ipAddress, portNumber, this.gs);
				completedGossips.add(randomId);
				//System.out.println("sending thread started");
				gst.start();
			}
		}
		
	}

}
