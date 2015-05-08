package main;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import components.Park;
import components.Visitor;
import components.VisitorPath;
import components.VisitorPathPoint;

public class DinoFunWorld {
	
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
		String path = args[0];
		
		BufferedReader br = null;
		String cvsSplitBy = ",";
		
		Park park = new Park();
		
		
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
		
		
		// print invalid points
		System.out.println("Invalid points - date:");
		for (VisitorPathPoint invalidPoint : VisitorPath.unsolvedInvalidDates) {
			System.out.println(invalidPoint.toString());
		}
		System.out.println("Invalid points - position:");
		for (VisitorPathPoint invalidPoint : VisitorPath.criticalInvalidPositions) {
			System.out.println(invalidPoint.toString());
		}
		
		
		// export path patterns
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("assets\\path_patterns" + path.substring(path.length() - 7, path.length() - 4) + ".csv"));
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
