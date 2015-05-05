package components;

import java.awt.Point;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Park {
	private GridCell[][] cells;
	private Map<String, Visitor> visitors;
	
	
	public GridCell[][] getCells() {
		return cells;
	}
	
	
	public void setCells(GridCell[][] cells) {
		this.cells = cells;
	}
	
	
	public Map<String, Visitor> getVisitors() {
		return visitors;
	}
	
	
	public Park() {
		cells = new GridCell[100][100];
		visitors = new HashMap<>(8000);
	}
	
	
	public void addVisitorActivity(String visitorId, LocalDate date, String activity, Point position) {
		Visitor visitor = null;
		visitor = visitors.get(visitorId);
		if (visitor == null) {
			visitor = new Visitor(visitorId);
			visitors.put(visitorId, visitor);
		}
		
		// TODO: Validate position
		if (!activity.equals("movement") && !activity.equals("check-in") || position.x < 0 || position.x > 99 || position.y < 0 || position.y > 99) {
			System.out.printf(">> Error: Visitor %s is invalid: %s, (%d, %d)\n", visitorId, activity, position.x, position.y);
		}
		
		visitor.addPathPoint(date, cells[position.x][position.y], activity);
	}
}
