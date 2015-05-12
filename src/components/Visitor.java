package components;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class Visitor {
	private final String id;
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
		if (!group.getMembers().contains(this)) {
			group.addMember(this);
		}
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
		path.addPathPoint(date, cell, activity);
	}
	
	
	public String toPathPattern() {
		return id + "," + path.toPathPattern();
	}
	
	
	public String toCSV() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss", Locale.US);
		String result = "";
		for (VisitorPathPoint point : getPath().getPathPoints()) {
			Point position = point.getCell().getPosition();
			result += point.getDate().format(formatter) + "," + getId() + "," + point.getActivity() + "," + position.x + "," + position.y + "," + getGroup().getId() + "\n";
		}
		return result;
	}
}
