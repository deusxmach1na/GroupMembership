package com.kleck.GroupMembership;

import java.util.ArrayList;

public class MergeMembershipListThread extends Thread{
	private MembershipList mlIncoming;
	private GroupServer gs;
	
	
	public MergeMembershipListThread(MembershipList ml, GroupServer gs) {
		this.mlIncoming = ml;
		this.gs = gs;
	}

	public void run() {
		MembershipList mlCurrent = gs.getMembershipList();
		gs.setMembershipList(mergeLists(this.mlIncoming, mlCurrent));
		//System.out.println(this.gs.getMembershipList().toString());
	}

	private MembershipList mergeLists(MembershipList mlIncoming, MembershipList mlCurrent) {
		//compare heartbeats from each process_id in the list
		//need a list of all keys in both ML's
		ArrayList<String> allKeys = new ArrayList<String>();
		
		System.out.println("Merged This List\n");
		System.out.println(mlCurrent.toString());
		
		for(String key:mlIncoming.getKeys()) {
			allKeys.add(key);
		}
		for(String key:mlCurrent.getKeys()) {
			if(!allKeys.contains(key))
				allKeys.add(key);
		}
		
		//we have the current keys now compare heartbeats
		for(int i=0;i < allKeys.size();i++) {
			String inspectKey = allKeys.get(i);
			//if they both have the same key then compare heartbeats
			if(mlCurrent.hasKey(inspectKey) && mlIncoming.hasKey(inspectKey)) {
				//get the MembershiplistRows
				MembershipListRow incomingRow = mlIncoming.getMember(inspectKey);
				MembershipListRow currentRow = mlCurrent.getMember(inspectKey);
				
				//compare HbCounters
				//if incoming row is greater than update 
				if(incomingRow.getHbCounter()  > currentRow.getHbCounter()) {
					MembershipListRow newRow = currentRow;
					newRow.setTimeStamp();
					mlCurrent.updateMember(inspectKey, newRow);	
				}
			}	
			//if the current list does not have the key then add entry
			else if(!mlCurrent.hasKey(inspectKey) && mlIncoming.hasKey(inspectKey)) {
				if(!mlIncoming.getMember(inspectKey).isDeletable()) {
					mlCurrent.addNewMember(inspectKey, mlIncoming.getMember(inspectKey).getPortNumber());
				}
				//if this is the contact server and it is adding a new record then send your membership list back
				if(this.gs.isContact() && mlIncoming.size() == 1) {
					GossipSendThread gst = new GossipSendThread(mlIncoming.getMember(inspectKey).getIpAddress(),
												mlIncoming.getMember(inspectKey).getPortNumber(),
												mlCurrent);
					gst.start();
				}	
				
			}
			//if the incoming list does not have the entry then do nothing
		}
		System.out.println("With This List\n");
		System.out.println(mlIncoming.toString());
		System.out.println("And Got This List\n");
		System.out.println(mlCurrent.toString());
		
		
		return mlCurrent;
	}
	
	
}
