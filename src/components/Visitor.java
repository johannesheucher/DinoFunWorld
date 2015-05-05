package components;

import java.time.LocalDate;


public class Visitor {
	private String id;
	private VisitorPath path;
	private Group group;
	
	
	public String getId() {
		return id;
	}
	
	
	public VisitorPath getPath() {
		return path;
	}
	
	
	public Group getGroup() {
		return group;
	}
	
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	
	public Visitor(String id) {
		this.id = id;
		path = new VisitorPath();
		path.setVisitor(this);
	}
	
	
	public void addPathPoint(LocalDate date, GridCell cell, String activity) {
		path.addPathPoint(new VisitorPathPoint(date, cell, activity));
	}
}
