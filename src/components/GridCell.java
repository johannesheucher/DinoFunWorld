package components;

import java.awt.Point;

public class GridCell {
	private final Point position;
	private Attraction attraction;
	
	
	public Point getPosition() {
		return position;
	}
	
	
	public Attraction getAttraction() {
		return attraction;
	}
	
	
	public void setAttraction(Attraction attraction) {
		this.attraction = attraction;
	}
	
	
	public GridCell(Point position) {
		this.position = position;
	}
}
