package components;

import java.awt.Point;

public class GridCell {
	private final Point position;
	private Attraction attraction;
	private Boolean everOccupied;
	
	
	public Point getPosition() {
		return position;
	}
	
	
	public Attraction getAttraction() {
		return attraction;
	}
	
	
	public void setAttraction(Attraction attraction) {
		this.attraction = attraction;
	}
	
	
	public Boolean isEverOccupied() {
		return everOccupied;
	}
	
	
	public void setEverOccupied(Boolean everOccupied) {
		this.everOccupied = everOccupied;
	}
	
	
	public GridCell(Point position) {
		this.position = position;
		setEverOccupied(false);
	}
}
