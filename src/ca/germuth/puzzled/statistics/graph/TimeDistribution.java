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
		//iterate through moves of the replay
		//and test cube for completion of step at each move
		while( iterator.hasNext() ){
			
			ReplayMove current = iterator.next();
			String move = current.getMove();
			
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
					if(PLLDone < 0){
						PLLDone *= -1;
					}
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
		
		//times are in ms
		//convert to sce
		double PLL = PLLDone / 1000.00;
		double OLL = OLLDone / 1000.00;
		double fourth = fourthPairDone / 1000.00;
		double third = thirdPairDone / 1000.00;
		double second = secondPairDone / 1000.00;
		double first = firstPairDone / 1000.00;
		double cross = crossDone / 1000.00;
		
		return new PieGraph("Puzzle Time Distribution", "Puzzle Section", "Seconds", 
				new String[]{"Cross", "First Pair", "Second Pair", "Third Pair", "Fourth Pair", "OLL", "PLL"},
				new String[]{cross+ "", first + "", second + "", third + "", fourth + "", OLL + "", PLL + "" });		
	}
}
