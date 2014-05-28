package ca.germuth.puzzled.statistics.graph;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import ca.germuth.puzzled.PuzzleMoveListener;
import ca.germuth.puzzled.ReplayParser;
import ca.germuth.puzzled.ReplayParser.ReplayMove;
import ca.germuth.puzzled.database.ObjectDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.graph.Graph;
import ca.germuth.puzzled.gui.graph.PieGraph;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.cube.Cube;

public class TimeDistribution implements GraphStatisticsMeasure{

	@Override
	public Graph getGraph(Activity activity, ObjectDB ob){ 
		int crossDone = 0;
		int firstPairDone = 0;
		int secondPairDone = 0;
		int thirdPairDone = 0;
		int fourthPairDone = 0;
		int OLLDone = 0;
		int PLLDone = 0;
		
		SolveDB db = (SolveDB) ob;
		ReplayParser rp = new ReplayParser(db.getmReplay());
		Iterator<ReplayMove> iterator = rp.iterator();
		
		//get a instance of the puzzle
		Cube cube = (Cube) rp.getmPuzzle();
		rp.scramble(db.getmScramble());
		
		//and all its moves
		ArrayList<PuzzleTurn> allMoves = rp.getmPuzzleMoves();
		
		int turn = 0;
		//iterate through moves of the replay
		//and test cube for completion of step at each move
		while( iterator.hasNext() ){
			turn++;
			
			ReplayMove current = iterator.next();
			String move = current.getMove();
			if( current.getTime() < 0){
				continue;
			}
			
			PuzzleTurn match = null;
			//search all moves for this particular one
			for(int i = 0; i < allMoves.size(); i++){
				if( allMoves.get(i).getmName().equals(move)){
					match = allMoves.get(i);
					break;
				}
			}
			
			PuzzleMoveListener.executePuzzleTurn(cube, match);

			if (crossDone == 0) {
				if (cube.isCrossSolved()) {
					crossDone = current.getTime();
				}
			} else if (firstPairDone == 0) {
				if (cube.pairsSolved() >= 1) {
					firstPairDone = current.getTime();
				}
			} else if (secondPairDone == 0) {
				if (cube.pairsSolved() >= 2) {
					secondPairDone = current.getTime();
				}
			} else if (thirdPairDone == 0) {
				if (cube.pairsSolved() >= 3) {
					thirdPairDone = current.getTime();
				}
			} else if (fourthPairDone == 0) {
				if (cube.pairsSolved() >= 4) {
					fourthPairDone = current.getTime();
				}
			} else if (OLLDone == 0){
				if( cube.isOLLSolved()){
					OLLDone = current.getTime();
				}
			} else{
				if( cube.isSolved()){
					PLLDone = current.getTime();
				}
			}
		}
		
		PLLDone -= OLLDone;
		OLLDone -= fourthPairDone;
		fourthPairDone -= thirdPairDone;
		thirdPairDone -= secondPairDone;
		secondPairDone -= firstPairDone;
		firstPairDone -= crossDone;
		firstPairDone -= crossDone;
		
		return new PieGraph("Puzzle Move Distribution", "Puzzle Section", "Seconds", 
				new String[]{"Cross", "First Pair", "Second Pair", "Third Pair", "Fourth Pair", "OLL", "PLL"},
				new String[]{crossDone + "", firstPairDone + "", secondPairDone + "", thirdPairDone + "", fourthPairDone + "", OLLDone + "", PLLDone + "" });	
//				new String[]{"6", "3", "4", "4", "5", "4", "2" });		
	}
}
