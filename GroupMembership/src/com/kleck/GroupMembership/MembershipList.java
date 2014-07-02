package com.kleck.GroupMembership;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//stores the membershiplist for 1 process
//access by processId
public class MembershipList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String, MembershipListRow> ml;
	
	public MembershipList() {
		this.ml = new ConcurrentHashMap<String, MembershipListRow>();	
	}
	
	public void addNewMember(String processId, int portNumber, boolean isContact) {
		this.ml.putIfAbsent(processId, new MembershipListRow(processId, portNumber, isContact));
	}
	
	public void removeMember(String processId) {
		this.ml.remove(processId);
	}
	
	public void updateMember(String processId, MembershipListRow mr) {
		this.ml.put(processId, mr);
	}

	public MembershipListRow getMember(String processId) {
		return this.ml.get(processId);
	}
	
	public Set<String> getKeys() {
		return this.ml.keySet();
	}
	
	public boolean hasKey(String processId) {
		return this.ml.containsKey(processId);
	}
	
	public int size() {
		return this.ml.size();
	}
	
	public String toString() {
		String ret = "";
		for(String key: this.ml.keySet()) {
			ret += this.ml.get(key).toString() + "\n";
		}
		return ret;
	}
	
}

