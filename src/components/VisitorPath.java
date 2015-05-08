package components;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class VisitorPath {
	private ArrayList<VisitorPathPoint> pathPoints;
	private Visitor visitor;
	
	public static ArrayList<VisitorPathPoint> criticalInvalidPositions = new ArrayList<>();
	public static ArrayList<VisitorPathPoint> unsolvedInvalidDates = new ArrayList<>();
	
	public ArrayList<VisitorPathPoint> getPathPoints() {
		return pathPoints;
	}
	
	
	public void addPathPoint(VisitorPathPoint pathPoint) {
		// validate path
		if (pathPoints.size() > 0) {
			VisitorPathPoint previousPoint = pathPoints.get(pathPoints.size() - 1);
			if (previousPoint.getDate().isAfter(pathPoint.getDate())) {
				System.out.printf(">> Error: Invalid date at point: %s\n", pathPoint.toString());
				unsolvedInvalidDates.add(pathPoint);
			}
			int errX = previousPoint.getCell().getPosition().x - pathPoint.getCell().getPosition().x;
			int errY = previousPoint.getCell().getPosition().y - pathPoint.getCell().getPosition().y;
			if ((Math.abs(errX) > 1 || Math.abs(errY) > 1) && previousPoint.getActivity().equals("movement")) {
				
				if (Math.abs(errX) > 2 || Math.abs(errY) > 2) {
					criticalInvalidPositions.add(pathPoint);
				}
				// fix path (only when visitor was moving)
				Point offset = new Point(errX < 0? -1 : errX > 0? 1 : 0, errY < 0? -1 : errY > 0? 1 : 0);
				int newX = pathPoint.getCell().getPosition().x + offset.x;
				int newY = pathPoint.getCell().getPosition().y + offset.y;
				long errSeconds = ChronoUnit.SECONDS.between(previousPoint.getDate(), pathPoint.getDate());
				LocalDateTime newDate = pathPoint.getDate().minusSeconds((long)(errSeconds / Math.sqrt(errX*errX + errY*errY)));
				VisitorPathPoint newPoint = new VisitorPathPoint(newDate, visitor.getPark().getCells()[newX][newY], pathPoint.getActivity());
				addPathPoint(newPoint);
				System.out.printf("\t\tVisitor %s: Fixed point %s\twith\t%s\n", visitor.getId(), pathPoint, newPoint);
			}
		}
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
				if (getVisitor().getPark().getCells()[x][y].isEverOccupied()) {
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
