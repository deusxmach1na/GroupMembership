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
			//DELETE
			if((currentTime - compareTime) > timeFail && !key.equals(this.gs.getProcessId())) {
				this.gs.getMembershipList().getMember(key).setDeletable(true);
				System.out.println(key + " DELETE");
				//LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#DELETE#" + key + this.gs.getMembershipList().getMember(key));
				//lt.start();	
			}			
			//delete if it is marked as isDeletable and has passed 2 * timeFail milliseconds
			//REMOVE
			if((currentTime - compareTime) > 2 * timeFail && this.gs.getMembershipList().getMember(key).isDeletable()) {
				this.gs.getMembershipList().removeMember(key);
				System.out.println(key + " REMOVE");
				//LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#REMOVE#" + key + this.gs.getMembershipList().getMember(key));
				//lt.start();	
			}		
			
		}
	}
}
