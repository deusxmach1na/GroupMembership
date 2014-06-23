package com.kleck.GroupMembership;

/*
 * real simple thread just needs to update the heartbeat
 * 
 */
public class UpdateHeartbeatThread extends Thread{
	private GroupServer gs;
	
	public UpdateHeartbeatThread(GroupServer gs) {
		this.gs = gs;
	}
	
	public void run() {
		this.gs.getMembershipList().getMember(this.gs.getProcessId()).incrementHbCounter();
	}

}
