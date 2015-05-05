package components;

import java.awt.Point;

public class Attraction {
	private Point position;
	
	
	public Point getPosition() {
		return position;
	}
	
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	
	public Attraction(Point position) {
		setPosition(position);
	}
}
