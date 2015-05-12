package components;

import java.util.HashSet;
import java.util.Set;

public class Group {
	private final String id;
	private Set<Visitor> members;
	
	
	public String getId() {
		return id;
	}
	
	
	public Set<Visitor> getMembers() {
		return members;
	}
	
	
	public void addMember(Visitor member) {
		this.members.add(member);
		if (member.getGroup() != this) {
			member.setGroup(this);
		}
	}
	
	
	public Group(String id) {
		this.id = id;
		this.members = new HashSet<Visitor>();
	}
}
