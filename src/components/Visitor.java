package components;

import java.time.LocalDateTime;


public class Visitor {
	private String id;
	private VisitorPath path;
	private Group group;
	private Park park;
	
	
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
	
	
	public Park getPark() {
		return park;
	}
	
	
	public void setPark(Park park) {
		this.park = park;
	}
	
	
	public Visitor(String id) {
		this.id = id;
		path = new VisitorPath();
		path.setVisitor(this);
	}
	
	
	public void addPathPoint(LocalDateTime date, GridCell cell, String activity) {
		path.addPathPoint(new VisitorPathPoint(date, cell, activity));
	}
	
	
	public String toPathPattern() {
		return id + "," + path.toPathPattern();
	}
}
