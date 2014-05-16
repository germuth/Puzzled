package ca.germuth.puzzled.statistics.text;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.util.Utils;

public class Average implements TextStatisticsMeasure{

	@Override
	public int getType() {
		return PUZZLE_TYPE;
	}

	@Override
	public String getValue(Activity mActivity, Object mDBObject, int optionalParam) {
		PuzzleDB puzz = (PuzzleDB) mDBObject;
		int size = optionalParam;
		
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		ArrayList<SolveDB> solves = db.getAllSolves(puzz);
		
		if( size > solves.size() || solves.isEmpty()){
			return null;
		}
		if( size == Integer.MAX_VALUE){
			size = solves.size();
		}
		double avg = 0;
		int count = 0;
		//iterate backwards since most recent solves are at the back
		for(int i = solves.size() - 1; solves.size() - size <= i; i--){
			avg += solves.get(i).getmDuration();
			count++;
		}
		avg /= count;
		
		return Utils.solveDurationToStringSeconds((int)avg);
	}
}
