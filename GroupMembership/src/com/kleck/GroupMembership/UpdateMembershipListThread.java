package com.kleck.GroupMembership;

/*
 * contains logic for marking membership list entries as 
 * isDeletable and actually removes old entries
 * 
 * 
 */
public class UpdateMembershipListThread extends Thread {
	private GroupServer gs;
	private long timeFail;
	
	public UpdateMembershipListThread(GroupServer gs, long timeFail) {
		this.gs = gs;	
		this.timeFail = timeFail;
	}
	
	public void run() {
		long currentTime = System.currentTimeMillis();
		//mark things as deletable if they have not been updated 
		//in timeFail milliseconds
		for(String key: this.gs.getMembershipList().getKeys()) {
			long compareTime = this.gs.getMembershipList().getMember(key).getTimeStamp();
			//mark as deletable if it's been timeFail milliseconds
			if((currentTime - compareTime) > timeFail) {
				this.gs.getMembershipList().getMember(key).setDeletable(true);
			}			
			//delete if it is marked as isDeletable and has passed 2 * timeFail milliseconds
			if((currentTime - compareTime) > 2 * timeFail && this.gs.getMembershipList().getMember(key).isDeletable()) {
				this.gs.getMembershipList().removeMember(key);;
			}		
			
		}
	}
}
