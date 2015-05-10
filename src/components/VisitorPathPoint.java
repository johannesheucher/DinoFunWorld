package components;

import java.time.LocalDateTime;

public class VisitorPathPoint {
	private final LocalDateTime date;
	private final GridCell cell;
	private final String activity;
	private final VisitorPath path;
	
	
	public LocalDateTime getDate() {
		return date;
	}
	
	
	public GridCell getCell() {
		return cell;
	}
	
	
	public VisitorPath getPath() {
		return path;
	}
	
	
	public String getActivity() {
		return activity;
	}
	
	
	public VisitorPathPoint(LocalDateTime date, GridCell cell, VisitorPath path, String activity) {
		this.date = date;
		this.cell = cell;
		this.path = path;
		this.activity = activity;
		cell.setEverOccupied(true);
	}
	
	
	@Override
	public String toString() {
		return String.format("PathPoint at %s\t(%d, %d)", getDate().toLocalTime().toString(), getCell().getPosition().x, getCell().getPosition().y);
	}
}
