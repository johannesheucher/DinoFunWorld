package components;

import java.time.LocalDateTime;

public class VisitorPathPoint {
	private final LocalDateTime date;
	private final GridCell cell;
	private final String activity;
	
	
	public LocalDateTime getDate() {
		return date;
	}
	
	
	public GridCell getCell() {
		return cell;
	}
	
	
	public String getActivity() {
		return activity;
	}
	
	
	public VisitorPathPoint(LocalDateTime date, GridCell cell, String activity) {
		this.date = date;
		this.cell = cell;
		this.activity = activity;
	}
}
