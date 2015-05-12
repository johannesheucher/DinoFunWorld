package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.imageio.ImageIO;

import components.Group;
import components.Park;
import components.Visitor;
import components.VisitorPath;
import components.VisitorPathPoint;

public class DinoFunWorld {
	
	private Park park;
	
	public static long numLines(FileReader reader) {
		BufferedReader br = new BufferedReader(reader);
		long num = br.lines().count();
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	public static void main(String[] args) {
		String visitorPath = args[0];
		String groupPath = args[1];
		
		
		Park park = new Park();
		
		importVisitors(visitorPath, park);
		importGroups(groupPath, park);
		
		// export repaired and complete visitors
		String visitorExportPath = visitorPath.substring(0, visitorPath.length() - 4) + "_repaired.csv";
		exportVisitors(visitorExportPath, park);
		
		
		// export image of pattern
//		exportPathImage(park.getVisitors().get("1621849"));
//		exportPathImage(park.getVisitors().get("1288884"));
//		exportPathImage(park.getVisitors().get("594316"));
//		
//		exportPathImage(park.getVisitors().get("241972"));
//		exportPathImage(park.getVisitors().get("710414"));
//		
//		exportPathImage(park.getVisitors().get("443367"));
//		exportPathImage(park.getVisitors().get("1755554"));
//		
//		
//		// print invalid points
//		System.out.println("Invalid points - date:");
//		for (VisitorPathPoint invalidPoint : VisitorPath.unsolvedInvalidDates) {
//			System.out.println(invalidPoint.toString());
//		}
//		System.out.println("Invalid points - position:");
//		for (int i = 0; i < VisitorPath.criticalInvalidPositions.size();) {
//			VisitorPathPoint invalidPointFrom = VisitorPath.criticalInvalidPositions.get(i++);
//			VisitorPathPoint invalidPointTo = VisitorPath.criticalInvalidPositions.get(i++);
//			
//			System.out.printf("Invalid movement of %s from %s\t\tto\t\t%s\n", invalidPointFrom.getPath().getVisitor().getId(), invalidPointFrom.toString(), invalidPointTo.toString());
//		}
		
		
		// export path patterns
//		exportPathPatterns("assets\\path_patterns" + path.substring(path.length() - 7, path.length() - 4) + ".csv", park);
	}
	
	
	private static void importVisitors(String path, Park park) {
		BufferedReader br = null;
		String cvsSplitBy = ",";
		
		try {
			final long NUM_DATA_POINTS = numLines(new FileReader(path));
			int numParsedLines = 0;
			br = new BufferedReader(new FileReader(path));
			br.readLine();
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] column = line.split(cvsSplitBy);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss", Locale.US);
				try {
					park.addVisitorActivity(column[1], LocalDateTime.parse(column[0], formatter), column[2], new Point(Integer.parseInt(column[3]), Integer.parseInt(column[4])));
				} catch (Exception e) {
					System.out.printf(">> Error: Invalid line: %s\n", line);
				}
				
				if (numParsedLines % (10e4) == 0) {
					System.out.printf("Parsed %.2f%%\t\tVisitors: %d\n", 100*numParsedLines / (float)NUM_DATA_POINTS, park.getVisitors().size());
				}
				numParsedLines++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static void importGroups(String path, Park park) {
		BufferedReader br = null;
		String cvsSplitBy = ",";
		
		try {
			final long NUM_DATA_POINTS = numLines(new FileReader(path));
			int numParsedLines = 0;
			br = new BufferedReader(new FileReader(path));
			br.readLine();
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] column = line.split(cvsSplitBy);
				try {
					String visitorId = column[0].substring(1, column[0].length() - 1);
					Visitor visitor = park.getVisitors().get(visitorId);
					String groupId = column[column.length - 1].substring(1, column[column.length - 1].length() - 1);
					Group group = park.getGroups().get(groupId);
					if (group == null) {
						group = new Group(groupId);
						park.addGroup(group);
					}
					group.addMember(visitor);
				} catch (Exception e) {
					System.out.printf(">> Error: Invalid line: %s\n", line);
				}
				
				if (numParsedLines % (10e4) == 0) {
					System.out.printf("Parsed %.2f%%\t\tGroups: %d\n", 100*numParsedLines / (float)NUM_DATA_POINTS, park.getGroups().size());
				}
				numParsedLines++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static void exportVisitors(String path, Park park) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			int numWrittenLines = 0;
			for (Visitor visitor : park.getVisitors().values()) {
				String visitorCSV = visitor.toCSV();
				bw.write(visitorCSV);
				if (numWrittenLines % (100) == 0) {
					System.out.printf("Wrote %.2f%%\n", 100*numWrittenLines / (float)park.getVisitors().size());
				}
				numWrittenLines++;
//				if (numWrittenLines > 100) {
//					break;
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static void exportPathImage(Visitor visitor) {
		final int WIDTH = 100;
		final int HEIGHT = 100;
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(new Color(255, 255, 255));
		graphics.fillRect (0, 0, image.getWidth(), image.getHeight());
		
		final LocalDateTime REFERENCE_DATE = LocalDateTime.of(2014, 6, 6, 8, 0, 0, 0);
		final long DAY_SECONDS = (22 - 8) * 60 * 60;
		for (VisitorPathPoint point : visitor.getPath().getPathPoints()) {
			long errSeconds = ChronoUnit.SECONDS.between(REFERENCE_DATE, point.getDate());
			float intensity = (float)errSeconds / DAY_SECONDS;
			int r = 0;
			int g = 0;
			int b = 0;
			if (intensity < 0.33) {
				r = (int)(intensity * 600);
			} else if (intensity < 0.66) {
				g = (int)((intensity - 0.33) * 600);
			} else {
				b = (int)((intensity - 0.66) * 600);
			}
			image.setRGB(point.getCell().getPosition().x, point.getCell().getPosition().y, (r << 16) + (g << 8) + b);
		}
		try {
			ImageIO.write(image, "PNG", new File("assets\\path_" + visitor.getId() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void exportPathPatterns(String path, Park park) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			int numWrittenLines = 0;
			for (Visitor visitor : park.getVisitors().values()) {
				String line = visitor.toPathPattern();
				bw.write(line + "\n");
				if (numWrittenLines % (100) == 0) {
					System.out.printf("Wrote %.2f%%\n", 100*numWrittenLines / (float)park.getVisitors().size());
				}
				numWrittenLines++;
//				if (numWrittenLines > 100) {
//					break;
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
