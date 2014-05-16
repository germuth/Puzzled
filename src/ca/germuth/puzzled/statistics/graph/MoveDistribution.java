package ca.germuth.puzzled.statistics.graph;

import java.util.ArrayList;

import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.graph.LineGraph;
import ca.germuth.puzzled.gui.graph.PieGraph;
import ca.germuth.puzzled.puzzle.Puzzle;

public class MoveDistribution {

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
		
		return new PieGraph("Puzzle Move Distribution", "Puzzle Section", "Move Count", 
				new String[]{"Cross", "First Pair", "Second Pair", "Third Pair", "Fourth Pair", "OLL", "PLL"},
				new String[]{"8", "7", "4", "3", "6", "11", "12" });		
	}
}
