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
	
	
	public void addPathPoint(LocalDateTime date, GridCell cell, String activity) {
		VisitorPathPoint currentPoint = new VisitorPathPoint(date, cell, this, activity);
		
		// validate path
		if (pathPoints.size() > 0) {
			VisitorPathPoint previousPoint = pathPoints.get(pathPoints.size() - 1);
			if (previousPoint.getDate().isAfter(currentPoint.getDate())) {
				System.out.printf(">> Error: Invalid date at point: %s\n", currentPoint.toString());
				unsolvedInvalidDates.add(currentPoint);
			}
			int errX = previousPoint.getCell().getPosition().x - currentPoint.getCell().getPosition().x;
			int errY = previousPoint.getCell().getPosition().y - currentPoint.getCell().getPosition().y;
			long errSeconds = ChronoUnit.SECONDS.between(previousPoint.getDate(), currentPoint.getDate());
			if ((Math.abs(errX) > 1 || Math.abs(errY) > 1) && previousPoint.getActivity().equals("movement")) {
				
				if (Math.abs(errX) > 3 || Math.abs(errY) > 3 || errSeconds > 60) {
					criticalInvalidPositions.add(previousPoint);
					criticalInvalidPositions.add(currentPoint);
				} else {
					// fix path (only when visitor was moving)
					Point offset = new Point(errX < 0? -1 : errX > 0? 1 : 0, errY < 0? -1 : errY > 0? 1 : 0);
					int newX = currentPoint.getCell().getPosition().x + offset.x;
					int newY = currentPoint.getCell().getPosition().y + offset.y;
					LocalDateTime newDate = currentPoint.getDate().minusSeconds((long)(errSeconds / Math.sqrt(errX*errX + errY*errY)));
					addPathPoint(newDate, visitor.getPark().getCells()[newX][newY], currentPoint.getActivity());
					System.out.printf("\t\tVisitor %s: Fixed mov from %s\tto\t%s\twith\t%s\t(%d, %d)\n", visitor.getId(), previousPoint, currentPoint, newDate.toLocalTime().toString(), newX, newY);
				}
			}
		}
		pathPoints.add(currentPoint);
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
