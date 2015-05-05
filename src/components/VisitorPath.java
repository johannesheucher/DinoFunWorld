package components;

import java.util.ArrayList;

public class VisitorPath {
	private ArrayList<VisitorPathPoint> pathPoints;
	private Visitor visitor;
	
	
	public ArrayList<VisitorPathPoint> getPathPoints() {
		return pathPoints;
	}
	
	
	public void addPathPoint(VisitorPathPoint pathPoint) {
		// TODO: Validate path
		pathPoints.add(pathPoint);
	}
	
	
	public Visitor getVisitor() {
		return visitor;
	}
	
	
	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}
	
	
	public VisitorPath() {
		pathPoints = new ArrayList<>(5000);
	}
}
