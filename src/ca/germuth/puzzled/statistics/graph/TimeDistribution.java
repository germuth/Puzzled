package ca.germuth.puzzled.statistics.graph;

import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.gui.graph.PieGraph;
import ca.germuth.puzzled.puzzle.Puzzle;

public class TimeDistribution {
	
	public static PieGraph getGraph(PuzzledDatabase db, Puzzle puz){
//		PuzzleDB puzDB = db.convert(puz);
//		ArrayList<SolveDB> solves = db.getAllSolves( puzDB );
		
//		String[] solveDuration = new String[solves.size()];
//		String[] solveDate = new String[solves.size()];
//		
//		for(int i = 0; i < solves.size(); i++){
//			SolveDB current = solves.get(i);
//			int duration = current.getmDuration();
//			//solveDuration[i] = solveDurationToString(duration);
//			
//			long solveD = current.getmDateTime();
//			//solveDate[i] = solveDateToString( solveD );
//		}
		
		return new PieGraph("Puzzle Move Distribution", "Puzzle Section", "Seconds", 
				new String[]{"Cross", "First Pair", "Second Pair", "Third Pair", "Fourth Pair", "OLL", "PLL"},
				new String[]{"6", "3", "4", "4", "5", "4", "2" });		
	}
}
