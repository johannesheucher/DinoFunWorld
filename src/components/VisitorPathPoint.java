package components;

import java.time.LocalDate;

public class VisitorPathPoint {
	private final LocalDate date;
	private final GridCell cell;
	private final String activity;
	
	
	public LocalDate getDate() {
		return date;
	}
	
	
	public GridCell getCell() {
		return cell;
	}
	
	
	public String getActivity() {
		return activity;
	}
	
	
	public VisitorPathPoint(LocalDate date, GridCell cell, String activity) {
		this.date = date;
		this.cell = cell;
		this.activity = activity;
	}
}
