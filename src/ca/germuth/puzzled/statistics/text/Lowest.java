package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class Lowest implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return PUZZLE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject, int optionalParam) {
		PuzzleDB puzz = (PuzzleDB) mDBObject;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzz);
		
		if( solves.isEmpty() ){
			throw new IllegalArgumentException();
		}
		
		int min = Integer.MIN_VALUE;
		for(SolveDB s : solves){
			if( s.getmDuration() > min){
				min = s.getmDuration();
			}
		}
		
		return Utils.solveDurationToStringSeconds(min);
	}
}
