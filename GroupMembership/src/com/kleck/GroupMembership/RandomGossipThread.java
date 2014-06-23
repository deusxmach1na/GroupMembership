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
		this.numGossips = (int) Math.log(gs.getMembershipList().size());
	}
	
	public void run() {
		Set<String> keys = this.gs.getMembershipList().getKeys();
		ArrayList<String> randomKeys = new ArrayList<String>();
		ArrayList<String> completedGossips = new ArrayList<String>();
		Random rand = new Random();
		
		//convert to arraylist for easier randomization
		for(String key: keys) {
			randomKeys.add(key);
		}
		
		for(int i=0;i<numGossips;i++) {
			int n = rand.nextInt(keys.size());
			String randomId = randomKeys.get(n);
			if(!completedGossips.contains(randomId)) {
				String ipAddress = this.gs.getMembershipList().getMember(randomId).getIpAddress();
				int portNumber = this.gs.getMembershipList().getMember(randomId).getPortNumber();
				GossipSendThread gst = new GossipSendThread(ipAddress, portNumber, this.gs.getMembershipList());
				gst.start();
			}
		}
		
		
	}

}
