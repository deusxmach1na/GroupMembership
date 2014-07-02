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
				//LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#DELETE#" + key);
				//lt.start();	
				this.gs.getMembershipList().getMember(key).setDeletable(true);
				//System.out.println(key + " DELETE");
			}	
			
			//mark as do NOT DELETE if it has responded
			if((currentTime - compareTime) <= timeFail) {
				this.gs.getMembershipList().getMember(key).setDeletable(false);
			}
			
			//delete if it is marked as isDeletable and has passed 2 * timeFail milliseconds
			//REMOVE
			if((currentTime - compareTime) > 2 * timeFail && this.gs.getMembershipList().getMember(key).isDeletable()) {
				LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#REMOVED_FAILED_PROCESS#" + key);
				lt.start();	
				this.gs.getMembershipList().removeMember(key);
				//System.out.println(key + " REMOVE");
			}	
			
			//if process has left voluntarily mark it as such
			//LEFT VOLUNTARILY
			if(this.gs.getMembershipList().getMember(key).isHasLeft()) {
				//if it is not the contact server remove it
				//leave the contact server in so when it rejoins it can be updated
				if(!this.gs.getMembershipList().getMember(key).isContact()) {
					this.gs.getMembershipList().removeMember(key);	
					LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#REMOVED_LEFT_VOLUNTARILY#" + key);
					lt.start();	
				}
				else {
					LoggerThread lt = new LoggerThread(this.gs.getProcessId(), "#CONTACT_SERVER_LEFT_VOLUNTARILY_DO_NOT_REMOVE#" + key);
					lt.start();	
				}
			}
			
		}
	}
}
