package components;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Park {
	private GridCell[][] cells;
	private Map<String, Visitor> visitors;
	private Map<String, Group> groups;
	
	
	public GridCell[][] getCells() {
		return cells;
	}
	
	
	public void setCells(GridCell[][] cells) {
		this.cells = cells;
	}
	
	
	public Map<String, Visitor> getVisitors() {
		return visitors;
	}
	
	
	public Map<String, Group> getGroups() {
		return groups;
	}
	
	
	public Park() {
		cells = new GridCell[100][100];
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				cells[x][y] = new GridCell(new Point(x, y));
			}
		}
		visitors = new HashMap<>(8000);
		groups = new HashMap<>(5000);
	}
	
	
	public void addVisitorActivity(String visitorId, LocalDateTime date, String activity, Point position) {
		Visitor visitor = null;
		visitor = visitors.get(visitorId);
		if (visitor == null) {
			visitor = new Visitor(visitorId);
			visitor.setPark(this);
			visitors.put(visitorId, visitor);
		}
		
		// TODO: Validate position
		if (!activity.equals("movement") && !activity.equals("check-in") || position.x < 0 || position.x > 99 || position.y < 0 || position.y > 99) {
			System.out.printf(">> Error: Visitor %s is invalid: %s, (%d, %d)\n", visitorId, activity, position.x, position.y);
		}
		
		visitor.addPathPoint(date, cells[position.x][position.y], activity);
	}
	
	
	public void addGroup(Group group) {
		groups.put(group.getId(), group);
	}
	
	
	public List<String> toPathPatternList() {
		List<String> patternList = new ArrayList<String>();
		for (Visitor visitor : visitors.values()) {
			patternList.add(visitor.toPathPattern());
		}
		return patternList;
	}
}
