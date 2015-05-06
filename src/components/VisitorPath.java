package components;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
	
	
	private static final long REFERENCE_SECONDS = LocalDateTime.of(2014, 6, 1, 0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
	public String toPathPattern() {
		long[][] pattern = new long[100][100];
		
		long medianTime = 0;
		for (VisitorPathPoint pathPoint : pathPoints) {
			int x = pathPoint.getCell().getPosition().x;
			int y = pathPoint.getCell().getPosition().y;
			long timeValue = pathPoint.getDate().toEpochSecond(ZoneOffset.UTC) - REFERENCE_SECONDS;
			timeValue *= timeValue;
			medianTime += timeValue;
			pattern[x][y] = timeValue;
		}
		
		// write into comma-separated-string (line by line)
		StringBuilder output = new StringBuilder(10000);
		medianTime /= pathPoints.size();
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				// leave out those cells, that are never occupied by any visitor
				if (getVisitor().getPark().isOccupied(x, y)) {
					if (pattern[x][y] != 0) {
						output.append(pattern[x][y]);
					} else {
						// replace each empty grid cell with median
						output.append(medianTime);
					}
					output.append(",");
				}
			}
		}
		output.deleteCharAt(output.length() - 1);
		
		return output.toString();
	}
}
