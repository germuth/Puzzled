package ca.germuth.puzzled.statistics.graph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.Graph;
import ca.germuth.puzzled.puzzle.Puzzle;
public class SolveHistory {
	private static final String name = "Solve History";
	
	public static Graph getGraph(PuzzledDatabase db, Puzzle puz){
		PuzzleDB puzDB = db.convert(puz);
		ArrayList<SolveDB> solves = db.getAllSolves( puzDB );
		
		String[] solveDuration = new String[solves.size()];
		String[] solveDate = new String[solves.size()];
		
		for(int i = 0; i < solves.size(); i++){
			SolveDB current = solves.get(i);
			int duration = current.getmDuration();
			solveDuration[i] = solveDurationToString(duration);
			
			long solveD = current.getmDateTime();
			solveDate[i] = solveDateToString( solveD );
		}
		
		return new Graph(name, "Date", "Seconds", new String[]{"Solve Duration"}, solveDate, new String[][]{ solveDuration });		
	}
	
	//TODO move to somewhere shared
	public static String solveDurationToString(int duration){
		//duration is in ms
		//int hours = duration / ( 1000 * 60 * 60);
		//int milliLeft = duration - (hours * 1000 * 60 * 60);
		
		//int minutes = milliLeft / (1000 * 60);
		//milliLeft = milliLeft - ( minutes * 1000 * 60);
		
		//int seconds = milliLeft / 1000;
		int seconds = duration / 1000;
		int milliLeft = duration - (seconds * 1000);
		
		//return hours + ":" + minutes + ":" + seconds + "." + milliLeft;
		return seconds + "." + milliLeft;
	}
	
	public static String solveDateToString( long msSince1970 ){
		Date date = new Date( msSince1970);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		return sdf.format(date);
	}
}
