package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;

public class Percentile implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return SOLVE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject, int optionalParam) {
		SolveDB solve = (SolveDB) mDBObject;
		PuzzleDB puzz = solve.getmPuzzle();
		int size = optionalParam;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(solve.getmPuzzle());
		Collections.sort(solves, new Comparator<SolveDB>(){
			@Override
			public int compare(SolveDB lhs, SolveDB rhs) {
				Integer left = lhs.getmDuration();
				return left.compareTo(rhs.getmDuration());
			}
		});
		int solveNumber = -1;
		for(int i = 0; i < solves.size(); i++){
			if( solves.get(i).equals(solve)){
				solveNumber =  i + 1;
			}
		}
		
		int totalSolve = Integer.parseInt( 
				new TimesSolved().getValue(mActivity, puzz, optionalParam) );
		if(totalSolve == 0){
			return "N/A";
		}
		return (int)(((double)solveNumber / (double) totalSolve ) * 100.0) + "";
	}
}
