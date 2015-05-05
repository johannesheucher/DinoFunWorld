package components;

import java.awt.Point;

public class GridCell {
	private Point position;
	private Attraction attraction;
	
	
	public Point getPosition() {
		return position;
	}
	
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	
	public Attraction getAttraction() {
		return attraction;
	}
	
	
	public void setAttraction(Attraction attraction) {
		this.attraction = attraction;
	}
	
	
	public GridCell() {
		
	}
}
